package com.currencyexchange.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.currencyexchange.app.R
import com.currencyexchange.app.base.FragmentBase
import com.currencyexchange.app.databinding.FragmentExchangeConverterBinding
import com.currencyexchange.app.helper.ExchangeUIStates
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.utils.hideKeyboard
import com.currencyexchange.app.utils.launchItemInScope
import com.currencyexchange.app.utils.showSnackBar
import com.currencyexchange.app.utils.visible
import com.currencyexchange.app.viewmodel.ExchangeViewModel
import org.apache.commons.lang3.StringUtils


class ExchangeConverterFragment : FragmentBase() {

    private val viewModel: ExchangeViewModel by activityViewModels()
    private lateinit var bi: FragmentExchangeConverterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentExchangeConverterBinding.inflate(inflater, container, false).apply {
            bi = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observingItems()

        bi.edAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                bi.exchangeConvertedList = arrayListOf()
                bi.edAmount.hideKeyboard()
            }
            false
        }

        bi.edAmount.addTextChangedListener {
            bi.dropdownCurrency.setText(StringUtils.EMPTY)
        }

        bi.inputAmount.setEndIconOnClickListener {
            bi.edAmount.setText(StringUtils.EMPTY)
            bi.exchangeConvertedList = arrayListOf()
            bi.edAmount.hideKeyboard()
            bi.dropdownCurrency.setText(StringUtils.EMPTY)
        }

        bi.dropdownCurrency.setOnItemClickListener { parent, _, position, _ ->
            if (position >= 0 && !bi.edAmount.text.isNullOrEmpty()) {
                bi.edAmount.hideKeyboard()
                viewModel.fetchConvertedExchangeRatesFromLocalDB(
                    bi.dropdownCurrency.text.toString(),
                    bi.edAmount.text.toString().toDouble(),
                    bi.currencyLst as MutableList<CurrencyModel>
                )
            }
        }

    }

    private fun observingItems() {

        launchItemInScope {
            viewModel.showSuccessSnackBar().collect {
                bi.nestedScrollView.showSnackBar(message = it)
            }
        }

        launchItemInScope {
            viewModel.showErrorSnackBar().collect {
                bi.nestedScrollView.showSnackBar(message = it, action = "Re-Sync Exchange Rates") {
                    viewModel.retryConnection()
                }
            }
        }

        launchItemInScope {
            viewModel.convertedExchangeResult.collect {
                bi.exchangeConvertedList = it
                bi.productList.visible()
            }
        }

        launchItemInScope {
            viewModel.currencyCodes.collect {
                when (it) {
                    ExchangeUIStates.Loading -> {
                        bi.nestedScrollView.showSnackBar(message = getString(R.string.loading_app))
                    }
                    is ExchangeUIStates.Success -> {
                        if (it.data.isNullOrEmpty()) viewModel.fetchLocalCurrencyCodes()
                        else bi.currencyLst = it.data
                    }
                }
            }
        }

    }
}