package com.utsab.transformerbattleapp.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.helpers.TransformersComparator
import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.models.TransformerResponse
import com.utsab.transformerbattleapp.models.commonModels.Result
import com.utsab.transformerbattleapp.repositories.TransformerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for AddTransformerActivity and TransformerListActivity
 * Using Hilt for Injecting repo, viewmodel, apiinterface
 * @author Utsab Malakar
 */
@HiltViewModel
class TransformerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transformerRepo: TransformerRepo
) : ViewModel() {


    //region private mutable objects initialization
    private var _transformers = MutableLiveData<List<Transformer>>()
    private var _userToken = MutableLiveData<String>()
    //endregion private mutable objects initialization

    //region observables mutable objects initialization
    var autobots = MutableLiveData<ArrayList<Transformer>>()
    var decepticons = MutableLiveData<ArrayList<Transformer>>()
    var transformer = MutableLiveData<Transformer>()
    val winner = MutableLiveData<String>()
    //endregion observables mutable objects initialization


    var transformers = _transformers


//    region Live data for api event responses
    var isCreated = MutableLiveData<Boolean>()
    var isUpdatingModel = MutableLiveData<Boolean>()
    var isModelUpdated = MutableLiveData<Boolean>()
    var isModelDeleted = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var errorOccured = MutableLiveData<Boolean>()
    //endregion Live data for api event responses


    init {
        initLiveDatas()
        getApiKey()

    }

    private fun initLiveDatas() {
        isCreated.value = false
        isUpdatingModel.value = false
        isModelUpdated.value = false
        isModelDeleted.value = false
        isLoading.value = false
        errorOccured.value = false
    }

    private fun getApiKey() {
        if (_userToken.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _userToken.value = transformerRepo.getTokenFromPref().data.toString()
                getTransformersList()
            }
        } else getTransformersList()
    }

    //region methods for ListActivity
    fun getTransformerModelById() {
        isUpdatingModel.postValue(true)
        viewModelScope.launch {
            transformerRepo.getTransformerById(_userToken.value, transformer.value!!.id).collect {
                if (it.status == Result.Status.SUCCESS) {
                    val transformerTemp = it.data as Transformer
                    transformer.postValue(transformerTemp)
                }
            }
        }
    }
    fun createAutobot() {
        var autobot = Transformer()
        autobot.team = Constants.TEAM_AUTOBOT
        this.transformer.value = autobot
    }

    fun createDecepticon() {
        var decepticon = Transformer()
        decepticon.team = Constants.TEAM_DECEPTICON
        this.transformer.value = decepticon
    }

    fun startBattle() {
        val result = TransformersComparator.decideWinner(autobots.value!!, decepticons.value!!)
        if (!result.isNullOrEmpty()) {
            val survivingAutobots = result[Constants.TEAM_AUTOBOT]
            val survivingDecepticons = result[Constants.TEAM_DECEPTICON]
            val draw = result[Constants.DRAW]

            when {
                (survivingAutobots!!.size > survivingDecepticons!!.size
                        && survivingAutobots.size > draw!!.size) -> {
                    this.winner.postValue(Constants.TEAM_AUTOBOT)
                }
                (survivingDecepticons.size > survivingAutobots.size
                        && survivingDecepticons.size > draw!!.size
                        ) -> {
                    this.winner.postValue(Constants.TEAM_DECEPTICON)
                }
                else -> {
                    this.winner.postValue(Constants.DRAW)
                }
            }
        } else this.winner.postValue(Constants.DRAW)
        Log.e("WINNER", result.toString())

    }
    //endregion methods for ListActivity

    //region methods for AddActivity
    fun getTransformersList() {
        val savedTransformers = getTransformerModel()
        /*
        * Checking if model is presend in saved handle
        * */
        if (savedTransformers.isNotEmpty()) {
            this.transformers.postValue(savedTransformers)
        }
        viewModelScope.launch {

            transformerRepo.getTransformers(_userToken.value).collect {
                if (it.status == Result.Status.SUCCESS) {

                    var autobotsList = arrayListOf<Transformer>()
                    var decepticonsList = arrayListOf<Transformer>()


                    val transformerTemp = it.data as TransformerResponse
                    _transformers.value = transformerTemp.transformers

                    transformers.postValue(_transformers.value)

                    /**
                     * Adding autobot and decepticon to their respective list
                     * then updating the live data object for each list
                     */
                    _transformers.value!!.forEach { new ->
                        when (new.team) {
                            Constants.TEAM_AUTOBOT -> {
                                autobotsList.add(new)
                            }
                            else -> {
                                decepticonsList.add(new)
                            }
                        }
                    }
                    /**
                     * Sorting each list on basis of overall rating
                     */
                    autobotsList.sortByDescending { autobot ->
                        autobot.getRating()
                    }
                    decepticonsList.sortByDescending { autobot ->
                        autobot.getRating()
                    }

                    autobots.postValue(autobotsList)
                    decepticons.postValue(decepticonsList)
                    setTransformerModel()


                }else if (it.status == Result.Status.ERROR){
                    errorOccured.postValue(true)
                }
            }
        }
    }

    fun deleteTransformer() {
        viewModelScope.launch {
            transformerRepo.deleteTransformer(_userToken.value, transformer.value!!.id).collect {
                when (it.status) {
                    Result.Status.SUCCESS -> {
                        isLoading.postValue(false)
                        isModelDeleted.postValue(true)
                    }
                    Result.Status.LOADING -> {
                        isLoading.postValue(true)
                    }
                    else -> {
                        isLoading.postValue(false)
                        errorOccured.postValue(true)
                    }
                }
            }
        }
    }

    fun updateTransformer() {
        viewModelScope.launch {
            transformerRepo.updateTransformer(_userToken.value, transformer.value!!).collect {
                when (it.status) {
                    Result.Status.SUCCESS -> {
                        isLoading.postValue(false)
                        isModelUpdated.postValue(true)
                    }
                    Result.Status.LOADING -> {
                        isLoading.postValue(true)
                    }
                    else -> {
                        isLoading.postValue(false)
                    }
                }
            }
        }
    }

    fun createTransformer() {
        viewModelScope.launch {
            transformerRepo.createTransformer(_userToken.value.toString(), transformer.value!!)
                .collect {
                    when (it.status) {
                        Result.Status.SUCCESS -> {
                            isLoading.postValue(false)
                            isCreated.postValue(true)
                        }
                        Result.Status.LOADING -> {
                            isLoading.postValue(true)
                        }
                        else -> {
                            isLoading.postValue(false)
                        }
                    }
                }
        }
    }

    private fun setTransformerModel() {
        savedStateHandle.set(Constants.TRANSFORMER_MODEL, Gson().toJson(transformers.value))
    }

    private fun getTransformerModel(): List<Transformer> {
        val saved: String? = savedStateHandle.get(Constants.TRANSFORMER_MODEL)
        if (saved != null) {
            return Gson().fromJson(
                saved,
                object : TypeToken<List<Transformer>>() {}.type
            ) as List<Transformer>
        }
        return arrayListOf()

    }
    //endregion methods for AddActivity




}