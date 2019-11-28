package com.example.myapplication.api

import com.example.myapplication.model.ResponseTopRatedMovie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit



/**
 * Created by Neeta on 11/24/2019.
 */
interface ApiInterface {

    @GET("3/movie/top_rated?")
    fun getTopRatingData(@Query("api_key") apikey: String, @Query("language") lang: String, @Query("page") page:Number): Call<ResponseTopRatedMovie>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/")
                    .build()

            return retrofit.create(ApiInterface::class.java);
        }
    }
}