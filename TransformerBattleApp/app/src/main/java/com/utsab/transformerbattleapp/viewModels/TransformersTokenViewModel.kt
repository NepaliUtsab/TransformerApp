package com.utsab.transformerbattleapp.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.models.commonModels.Result
import com.utsab.transformerbattleapp.repositories.TransformerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransformersTokenViewModel @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        private val transformerRepo: TransformerRepo
        ): ViewModel() {

    private val TAG = TransformersTokenViewModel::class.java.simpleName

    private val _apiKey = MutableLiveData<String>()
    val apiKey = _apiKey

    var isFirstTime = MutableLiveData<Boolean>()

    init {
        isFirstTime.value = true
        getApiKey()
    }

    fun refreshToken(){
        getApiKey()
    }

    private fun setApiKey(userToken: String){
        savedStateHandle.set(Constants.USER_TOKEN, userToken)
    }

    private fun getUserToken(): String? {
        return savedStateHandle.get(Constants.USER_TOKEN)
    }

    private fun getApiKey() {
        val userToken = getUserToken()
        Log.e(TAG, userToken.toString())
        if (userToken == null) {
            if(transformerRepo.getTokenFromPref().status == Result.Status.ERROR) isFirstTime.postValue(true)
            else isFirstTime.postValue(false)

            viewModelScope.launch {
                transformerRepo.getApiToken().collect {
                    apiKey.value = it.data.toString()
                    setApiKey(it.data.toString())
                    Log.e(TAG, "KEY FROM API STATE")
                }
            }
        }else{
            isFirstTime.postValue(false)
        }
    }
}