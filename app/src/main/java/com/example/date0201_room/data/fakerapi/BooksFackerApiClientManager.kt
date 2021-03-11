package com.example.date0201_room.data.fakerapi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 管理 Retrofit
 */
object BooksFackerApiClientManager {
    // base url: https://fakerapi.it/api/v1/books?_quantity=1
    // 注意: 末端已經以 "/" 結尾, API interface 參數不必以 "/"開頭.(e.g. @GET("post/{id})).
    private const val baseUrl = "https://fakerapi.it/api/v1/"

    // okHttpClient:
    // Retrofit 可搭配 OkHttp, 在此設定自己的 okHttpClient.
    private val okHttpClient: OkHttpClient
        get() {
            val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val builder = OkHttpClient.Builder().apply {
                addInterceptor(httpLoggingInterceptor)
            }

            return builder.build()
        }


    // retrofit:
    private val retrofit: Retrofit
        get() {
            val builder = Retrofit.Builder().apply {
                baseUrl(baseUrl)
                addConverterFactory(GsonConverterFactory.create())
                client(okHttpClient)
            }

            return builder.build()
        }


    // Api Service
    val apiService: IBooksFackerApiService = retrofit.create(IBooksFackerApiService::class.java)

} // end object BooksFackerApiClientManager.