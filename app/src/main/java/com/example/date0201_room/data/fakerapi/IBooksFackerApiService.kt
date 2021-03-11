package com.example.date0201_room.data.fakerapi

import com.example.date0201_room.data.fakerapi.model.BooksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Used by Retrofit to turn HTTP API into the interface.
 */
interface IBooksFackerApiService {

    // https://fakerapi.it/api/v1/books?_quantity=1
    @GET("books")
    fun GetBooks(@Query("_quantity") q: String): Call<BooksResponse>
}