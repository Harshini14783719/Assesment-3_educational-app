package com.jcu.educationapp.data.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TriviaApiService {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int = 17,
        @Query("type") type: String = "multiple"
    ): Response<TriviaResponseDto>

    @GET
    suspend fun getRandomQuote(@Url url: String = "https://dummyjson.com/quotes/random"): Response<QuoteDto>

    @GET
    suspend fun getRandomFact(@Url url: String = "https://uselessfacts.jsph.pl/api/v2/facts/random"): Response<FactDto>

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
