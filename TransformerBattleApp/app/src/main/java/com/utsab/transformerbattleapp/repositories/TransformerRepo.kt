package com.utsab.transformerbattleapp.repositories

import android.content.Context
import android.util.Log
import com.utsab.transformerbattleapp.helpers.ApiInterface
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.helpers.STATUS_CODES
import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.models.TransformerResponse
import com.utsab.transformerbattleapp.models.commonModels.Error
import com.utsab.transformerbattleapp.models.commonModels.Result
import com.utsab.transformerbattleapp.repositories.local.TransformersLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TransformerRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val localDataSource: TransformersLocalDataSource
) : TransformersRepoInterface {

    private val TAG = TransformerRepo::class.simpleName

    override suspend fun createTransformer(
        userToken: String,
        transformer: Transformer
    ): Flow<Result<Transformer>> {
        return flow {
            emit(Result.loading())

            val response = apiInterface.createTransformer("Bearer $userToken", transformer)
            val result = if (response.isSuccessful) {
                response.body()?.let {
                    return@let Result.success(response.body())
                } ?: Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            } else {
                Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            }

            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getTransformers(token: String?): Flow<Result<TransformerResponse>> {
        return flow {

            emit(Result.loading())

            val response = apiInterface.getTransformers("Bearer $token")
            val result = if (response.isSuccessful) {
                response.body()?.let {
                    return@let Result.success(response.body())
                } ?: Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            } else {
                Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            }

            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getApiToken(): Flow<Result<String>> {
        return flow {
            emit(Result.loading())

            var result = getTokenFromPref()
            if (result.status == Result.Status.ERROR) {
                result = getTokenFromApi()
                Log.e(TAG, "Token from Api ${result.data}")
                if (result.status == Result.Status.SUCCESS) {
                    localDataSource.setUserToken(result.data.toString())
                }
            } else {
                Log.e(TAG, "Token from Preference ${result.data}")
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTokenFromApi(): Result<String> {
        return try {
            val response = apiInterface.getApiToken()
            if (response.isSuccessful) {

                response.body()?.let {

                    return@let Result.success(it)

                } ?: Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))

            } else return Result.error(
                Constants.UNKNOWN_FAILURE,
                Error(STATUS_CODES.UNKNOWN_ERROR.code)
            )

        } catch (e: Exception) {
            Result.error(
                Constants.NO_INTERNET_CONNECTION,
                Error(STATUS_CODES.CONNECTION_ERROR_CODE.code)
            )
        }
    }

    override suspend fun deleteTransformer(userToken: String?, id: String): Flow<Result<Boolean>> {
        return flow {

            emit(Result.loading())

            val response = apiInterface.deleteTransformer("Bearer $userToken", id)
            val result = if (response.code() == STATUS_CODES.DELETED_SUCCESSFULLY.code) {

                Result.success(true)

            } else {
                Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            }

            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateTransformer(
        userToken: String?,
        transformer: Transformer
    ): Flow<Result<Boolean>> {
        return flow {

            emit(Result.loading())

            val response = apiInterface.updateTransformer("Bearer $userToken", transformer)
            val result = if (response.isSuccessful) {
                response.body()?.let {
                    return@let Result.success(true)
                } ?: Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            } else {
                Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            }

            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTransformerById(
        userToken: String?,
        id: String
    ): Flow<Result<Transformer>> {
        return flow {

            emit(Result.loading())

            val response = apiInterface.getTransformerModelById("Bearer $userToken", id)
            val result = if (response.isSuccessful) {
                response.body()?.let {
                    return@let Result.success(response.body())
                } ?: Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            } else {
                Result.error(Constants.UNKNOWN_FAILURE, Error(STATUS_CODES.UNKNOWN_ERROR.code))
            }

            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    fun getTokenFromPref(): Result<String> {
        return localDataSource.getUserToken()
    }


}