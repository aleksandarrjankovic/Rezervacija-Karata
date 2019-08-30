package com.example.kupovinakarata.Retrofit

import android.content.Context
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {
    private var instance: Retrofit?=null

    fun getInstance(context: Context):Retrofit{

        val sharedPreferences = context.getSharedPreferences("IP", Context.MODE_PRIVATE)
        val BASE_URL = sharedPreferences.getString("adress", null)

        if(instance == null)
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return instance!!
    }
}