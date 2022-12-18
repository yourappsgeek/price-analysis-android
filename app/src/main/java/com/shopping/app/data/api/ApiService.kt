package com.shopping.app.data.api

import com.shopping.app.data.model.Product
import com.shopping.app.data.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("search")
    fun getProductsByCategory(
        @Query("product") query: String,
        @Query("api_key") key: String = "o4EHJ2Nn6A5wmef5tkaDU9AHXbmGxpqemlB",
    ): Call<ProductResponse>

}