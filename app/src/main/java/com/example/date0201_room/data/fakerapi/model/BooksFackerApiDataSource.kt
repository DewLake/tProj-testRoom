package com.example.date0201_room.data.fakerapi.model

import android.util.Log
import com.example.date0201_room.data.fakerapi.BooksFackerApiClientManager
import retrofit2.Call
import retrofit2.Response

/**
 * 實際與 API 接口 互動, 對應 API Service.
 *  送出請求(Request)/接收回覆(Response)
 */
object BooksFackerApiDataSource {
    // TAG
    val TAG = "[TAG]-${BooksFackerApiDataSource::class.simpleName}"

    // api service
    private val api = BooksFackerApiClientManager.apiService


    /** 取得資料 */
    fun GetBooks(
        quantity: String,
        onDataReadyCallback: (response: BooksResponse) -> Unit
    ) {
        api.GetBooks(quantity).enqueue(object : retrofit2.Callback<BooksResponse> {
            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                Log.d(TAG, "onResponse()...")
                onDataReadyCallback.invoke(response.body() as BooksResponse)
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                Log.d(TAG, "onResponse()... ${t.localizedMessage}")
            }
        }

        ) // end .enqueue().
    } // end GetBooks().

} // end object BooksFackerApiDataSource.