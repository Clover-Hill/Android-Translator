package com.newera.neo_translator

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api : TranslateApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApi::class.java)
    }

}