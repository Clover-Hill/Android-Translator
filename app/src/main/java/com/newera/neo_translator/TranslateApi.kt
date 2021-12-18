package com.newera.neo_translator

import com.newera.neo_translator.data.WordItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi
{
    @GET("api?signType=v3&to=zh-CHS&from=en&appKey=${Constants.appKey}")
    suspend fun getTranslate(@Query("q") q : String,
                     @Query("salt") salt : String,
                     @Query("sign") sign : String,
                     @Query("curtime") curtime : String): Response<WordItem>
}