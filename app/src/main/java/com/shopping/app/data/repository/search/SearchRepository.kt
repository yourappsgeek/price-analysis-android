package com.shopping.app.data.repository.search

import com.shopping.app.data.model.Product
import com.shopping.app.data.model.ProductResponse
import retrofit2.Call

interface SearchRepository {

    fun getProducts(): Call<List<Product>>

    fun getProductsByCategory(category: String): Call<ProductResponse>

    fun getCategories(): Call<List<String>>

}