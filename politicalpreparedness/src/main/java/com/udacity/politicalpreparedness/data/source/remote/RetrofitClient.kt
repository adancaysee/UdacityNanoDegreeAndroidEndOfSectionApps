package com.udacity.politicalpreparedness.data.source.remote

import DateAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.politicalpreparedness.BuildConfig
import com.udacity.politicalpreparedness.data.source.remote.jsonadapter.ElectionAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"


private val moshi = Moshi.Builder()
    .add(DateAdapter())
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

fun getRetrofitClient(): Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(getOkHttpClient())
    .baseUrl(BASE_URL)
    .build()


private fun getOkHttpClient() = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val url = chain
            .request()
            .url()
            .newBuilder()
            .addQueryParameter("key", BuildConfig.POLITICAL_PREPAREDNESS_GOOGLE_API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }.build()

