package com.shopping.app.ui.basket

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopping.app.R
import com.shopping.app.databinding.FragmentBasketBinding
import com.shopping.app.utils.Constants

class BasketFragment : BottomSheetDialogFragment() {

    private lateinit var bnd: FragmentBasketBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        preferences = requireContext().getSharedPreferences("APP_PREF_1", Context.MODE_PRIVATE)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        bnd.basketFragment = this
        val freeDel = preferences.getBoolean("free", false)
        val hasInst = preferences.getBoolean("inst", false)
        val maxPrice = preferences.getFloat("price", 50.0f)
        val maxRating = preferences.getFloat("rating", 5.0f)

        bnd.freeD.isChecked = freeDel
        bnd.gasInstalments.isChecked = hasInst
        bnd.price.setText("\$$maxPrice")
        bnd.rbRate.rating = maxRating
    }

    fun clearTheBasket() {

        preferences.edit().apply {
            putBoolean("free", bnd.freeD.isChecked)
            putBoolean("inst", bnd.gasInstalments.isChecked)
            putFloat("price", bnd.price.text.toString().substring(1).toFloat())
            putFloat("rating", bnd.rbRate.rating)
        }.apply()

        this.dismiss()
    }

}