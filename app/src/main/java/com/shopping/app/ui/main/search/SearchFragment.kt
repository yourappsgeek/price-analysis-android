package com.shopping.app.ui.main.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shopping.app.R
import com.shopping.app.data.api.ApiClient
import com.shopping.app.data.model.CategoryModel
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.Product
import com.shopping.app.data.repository.search.SearchRepositoryImpl
import com.shopping.app.databinding.FragmentSearchBinding
import com.shopping.app.ui.basket.BasketFragment
import com.shopping.app.ui.loadingprogress.LoadingProgressBar
import com.shopping.app.ui.main.search.adapter.CategoryAdapter
import com.shopping.app.ui.main.search.adapter.SearchAdapter
import com.shopping.app.ui.main.search.viewmodel.SearchViewModel
import com.shopping.app.ui.main.search.viewmodel.SearchViewModelFactory

class SearchFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var bnd: FragmentSearchBinding
    private var oldData: MutableList<Product> = mutableListOf()
    private lateinit var loadingProgressBar: LoadingProgressBar
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModelFactory(
            SearchRepositoryImpl(
                ApiClient.getApiService()
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        bnd.mainMenuFragment = this
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private lateinit var preferences: SharedPreferences

    override fun onResume() {
        super.onResume()

        preferences = requireContext().getSharedPreferences("APP_PREF_1", Context.MODE_PRIVATE)
        val freeDel = preferences.getBoolean("free", false)
        val hasInst = preferences.getBoolean("inst", false)
        val maxPrice = preferences.getFloat("price", 50.0f)
        val maxRating = preferences.getFloat("rating", 5.0f)

        val del = if (freeDel) "Free" else "No"
        val inst = if (hasInst) "Yes" else "No"
        oldData.sortBy { it.usPrice <= maxPrice &&
                it.rating >= maxRating && it.deliveryFee == del && it.hasInstallments == inst }

        val searchAdapter = SearchAdapter(findNavController(), oldData)
        bnd.rvSearch.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        bnd.rvSearch.setHasFixedSize(true)
        bnd.rvSearch.adapter = searchAdapter
    }

    private fun init(){

        loadingProgressBar = LoadingProgressBar(requireContext())

        viewModel.searchLiveData.observe(viewLifecycleOwner){

            when (it) {
                is DataState.Success -> {
                    loadingProgressBar.hide()
                    it.data?.let { safeData ->

                        oldData = safeData as MutableList<Product>
                        preferences = requireContext().getSharedPreferences("APP_PREF_1", Context.MODE_PRIVATE)
                        val freeDel = preferences.getBoolean("free", false)
                        val hasInst = preferences.getBoolean("inst", false)
                        val maxPrice = preferences.getFloat("price", 50.0f)
                        val maxRating = preferences.getFloat("rating", 5.0f)

                        oldData.sortedWith(compareBy<Product> { it.usPrice }.thenBy { it.rating }.thenBy { it.deliveryFee }
                            .thenBy { it.hasInstallments })
                        val searchAdapter = SearchAdapter(findNavController(), oldData)
                        bnd.rvSearch.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        bnd.rvSearch.setHasFixedSize(true)
                        bnd.rvSearch.adapter = searchAdapter

                    } ?: run {
                        Snackbar.make(bnd.root, getString(R.string.no_data), Snackbar.LENGTH_LONG).show()
                    }
                }
                is DataState.Error -> {
                    loadingProgressBar.hide()
                    Snackbar.make(bnd.root, it.message, Snackbar.LENGTH_LONG).show()
                }
                is DataState.Loading -> {
                    loadingProgressBar.show()
                }
            }

        }

        bnd.searchView.setOnQueryTextListener(this)

    }

    private fun searchQuery(query:String?){

        bnd.searchView.clearFocus()
        if(query != null && query.isNotEmpty()){
            viewModel.getProductsByCategory(query.lowercase())
        }else{
            //viewModel.searchProducts()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchQuery(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    fun openBasket(){

        //Navigation.findNavController(bnd.root).navigate(R.id.basketFragment2)
        BasketFragment().show(parentFragmentManager, "basket")

    }

}

interface CategoryClickListener{
    fun onClickCategory(category: CategoryModel)
}