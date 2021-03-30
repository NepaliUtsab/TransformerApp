package com.utsab.transformerbattleapp.repositories.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.models.commonModels.Error
import com.utsab.transformerbattleapp.models.commonModels.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.NullPointerException
import javax.inject.Inject

class TransformersLocalDataSource @Inject constructor(@ApplicationContext val context: Context) {

    private var PRIVATE_MODE = 0
    private var PREF_NAME = "TransformersSharedPref"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    private val editor = sharedPreferences.edit()

    private val gson = Gson()

    fun setUserToken(string: String){
        editor.putString(Constants.USER_TOKEN, string)
        editor.commit()
    }

     fun getUserToken(): Result<String>{
        val userToken = sharedPreferences.getString(Constants.USER_TOKEN, "")

        return if(userToken.isNullOrEmpty())
            Result.error(Constants.NO_USER_TOKEN, Error(101, ""))
        else
            Result.success(userToken)
    }
}