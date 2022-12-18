package com.shopping.app.data.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import kotlin.random.Random

data class Product(
    @SerializedName("product_category")
    val category: String?,
    @SerializedName("product_image")
    val image: String,
    @SerializedName("product_lowest_price")
    val inPrice: Double,
    @SerializedName("product_title")
    val title: String,
    var deliveryFee: String = "Free Delivery",
    var usPrice: Double = 0.0,
    var hasInstallments: String = "Yes",
    var rating: Float = 5.0f
) : Serializable