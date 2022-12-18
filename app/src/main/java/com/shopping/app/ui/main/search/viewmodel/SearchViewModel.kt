package com.shopping.app.ui.main.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.Product
import com.shopping.app.data.model.ProductResponse
import com.shopping.app.data.repository.search.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    private lateinit var productList:List<Product>

    private var _searchLiveData = MutableLiveData<DataState<List<Product>?>>()
    val searchLiveData: LiveData<DataState<List<Product>?>>
        get() = _searchLiveData

    fun getProductsByCategory(query: String){

        _searchLiveData.postValue(DataState.Loading())
        searchRepository.getProductsByCategory(query).enqueue(object: Callback<ProductResponse>{

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {

                        productList = it.data
                        _searchLiveData.postValue(DataState.Success(productList))

                    } ?: kotlin.run {
                        _searchLiveData.postValue(DataState.Error("Data Empty"))
                    }
                } else {
                    _searchLiveData.postValue(DataState.Error(response.message()))
                }

            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _searchLiveData.postValue(DataState.Error(t.message.toString()))
            }

        })

    }

}