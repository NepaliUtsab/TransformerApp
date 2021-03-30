package com.utsab.transformerbattleapp.repositories

import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.models.TransformerResponse
import com.utsab.transformerbattleapp.models.commonModels.Result
import kotlinx.coroutines.flow.Flow

/**
 * Interface class for Repositories
 */
interface TransformersRepoInterface {

    suspend fun createTransformer(userToken: String, transformer: Transformer): Flow<Result<Transformer>>

    suspend fun getTransformers(token:String?):Flow<Result<TransformerResponse>>

    suspend fun getApiToken(): Flow<Result<String>>

    suspend fun getTokenFromApi():Result<String>

    suspend fun deleteTransformer(userToken: String?, id:String):Flow<Result<Boolean>>

    suspend fun updateTransformer(userToken: String?, transformer:Transformer):Flow<Result<Boolean>>

    suspend fun getTransformerById(userToken: String?, id:String):Flow<Result<Transformer>>
}