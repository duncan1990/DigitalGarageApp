package com.ahmety.digitalgarageapp


import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface ApiService {
    @PUT("/hash/ahmet-yilmaz-73292")
    suspend fun putGuid(@Body requestBody: RequestBody): Response<JSONModel>
}