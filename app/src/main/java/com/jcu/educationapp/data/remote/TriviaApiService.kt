package com.jcu.educationapp.data.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int = 17, // Science & Nature default
        @Query("type") type: String = "multiple"
    ): Response<TriviaResponseDto>

    companion object {
        private const val BASE_URL = "https://opentdb.com/"

        fun create(): TriviaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TriviaApiService::class.java)
        }
    }
}
