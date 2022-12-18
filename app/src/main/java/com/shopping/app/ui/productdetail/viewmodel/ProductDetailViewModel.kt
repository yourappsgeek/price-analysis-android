package com.shopping.app.ui.productdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.shopping.app.R
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.Product
import com.shopping.app.data.model.ProductBasket
import com.shopping.app.data.repository.basket.BasketRepository

class ProductDetailViewModel(private val basketRepository: BasketRepository) : ViewModel() {

    val productCountLiveData = MutableLiveData<Int>()

    private var _addBasketLiveData = MutableLiveData<DataState<Boolean>>()
    val addBasketLiveData: LiveData<DataState<Boolean>>
        get() = _addBasketLiveData

    init {
        setDefaultCount()
    }

    private fun setDefaultCount(){
        productCountLiveData.value = 1
    }

}