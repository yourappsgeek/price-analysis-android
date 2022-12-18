package com.shopping.app.ui.main.search.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.shopping.app.R
import com.shopping.app.data.model.Product
import com.shopping.app.databinding.ItemProductSearchBinding
import com.shopping.app.utils.Constants
import kotlin.random.Random

class SearchAdapter(
      private val navController: NavController,
      private val productList: List<Product>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private lateinit var binding: ItemProductSearchBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding = ItemProductSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    fun goProductDetails(product: Product?){

        navController.navigate(
            R.id.action_searchFragment_to_productDetailsFragment,
            Bundle().apply {
                putSerializable(Constants.PRODUCT_MODEL_NAME, product)
            })

    }

    inner class SearchViewHolder(private val binding: ItemProductSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            product.apply {
                val random = Random.nextInt(4,13)
                deliveryFee = if ((8..13).contains(random))
                    "Free" else "\$$random.0"
                val price = inPrice * 0.012
                usPrice = if ((price) <= 0.0) 210.0 else price
                hasInstallments = arrayOf("Yes", "No").random()
                rating = Random.nextDouble(2.5, 5.0).toFloat()
            }
            binding.dataHolder = product
            binding.searchAdapter = this@SearchAdapter
            binding.executePendingBindings()
        }

    }

    override fun getItemCount(): Int = productList.size

}