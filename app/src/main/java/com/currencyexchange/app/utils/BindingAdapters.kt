package com.currencyexchange.app.utils

import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencyexchange.app.R
import com.currencyexchange.app.adapters.GenericListAdapter
import com.currencyexchange.app.model.CurrencyMapper
import com.currencyexchange.app.model.CurrencyModel


object BindingAdapters : BaseObservable() {

    @BindingAdapter("loadCurrencyList")
    @JvmStatic
    fun loadCurrencyList(txt: AutoCompleteTextView, currencyCode: List<CurrencyModel>?) {
        currencyCode?.map { it.code }?.let {
            txt.setAdapter(
                ArrayAdapter(
                    txt.context,
                    android.R.layout.simple_list_item_activated_1,
                    it
                )
            )
        }
    }

    @SuppressLint("ResourceType")
    @BindingAdapter("loadExchangeConvertRates")
    @JvmStatic
    fun loadExchangeConvertRates(
        exchangeRecycler: RecyclerView,
        exchangeRates: List<CurrencyMapper>?
    ) {
        exchangeRates?.let {
            val adapter: GenericListAdapter<CurrencyMapper> =
                GenericListAdapter(R.layout.currency_view)
            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            exchangeRecycler.adapter = adapter
            adapter.productItems = it as ArrayList<CurrencyMapper>
        }
    }

    @BindingAdapter("amountFormat")
    @JvmStatic
    fun loadCurrencyList(txt: AppCompatTextView, amount: Double) {
        txt.text = "%.2f".format(amount)
    }

}