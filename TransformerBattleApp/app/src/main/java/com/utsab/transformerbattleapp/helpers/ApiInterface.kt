package com.utsab.transformerbattleapp.helpers

import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.models.TransformerRequest
import com.utsab.transformerbattleapp.models.TransformerResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {


    @GET(Constants.TOKEN_URL)
    suspend fun getApiToken(): Response<String>

    @GET(Constants.GET_TRANSFORMERS_URL)
    suspend fun getTransformers(@Header("Authorization") authorization:String): Response<TransformerResponse>

    @POST(Constants.GET_TRANSFORMERS_URL)
    suspend fun createTransformer(@Header("Authorization") authorization:String, @Body transformer: Transformer): Response<Transformer>

    @GET(Constants.UPDATE_TRANSFORMERS_URL)
    suspend fun getTransformerModelById(
        @Header("Authorization") authorization:String,
        @Path("transformerId") id:String
    ): Response<Transformer>

    @PUT(Constants.GET_TRANSFORMERS_URL)
    suspend fun updateTransformer(
        @Header("Authorization") authorization:String,
        @Body body: Transformer
    ): Response<Transformer>

    @DELETE(Constants.UPDATE_TRANSFORMERS_URL)
    suspend fun deleteTransformer(
        @Header("Authorization") authorization:String,
        @Path("transformerId") id:String
    ): Response<Any>

}