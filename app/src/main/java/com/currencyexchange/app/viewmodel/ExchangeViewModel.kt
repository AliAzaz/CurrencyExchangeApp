package com.currencyexchange.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyexchange.app.R
import com.currencyexchange.app.di.repository.GeneralDataSource
import com.currencyexchange.app.helper.DispatcherHelper
import com.currencyexchange.app.helper.ExchangeUIStates
import com.currencyexchange.app.helper.Resource
import com.currencyexchange.app.model.CurrencyMapper
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.utils.StringResourceManager
import com.currencyexchange.app.utils.launchItemInCoroutine
import com.currencyexchange.app.utils.launchItemInScope
import com.currencyexchange.app.viewmodel.usecases.ConvertedExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

/**
 * @author AliAzazAlam on 11/20/2022.
 */
interface IExchangeViewModel {

    fun showSuccessSnackBar(): SharedFlow<String>

    fun showErrorSnackBar(): SharedFlow<String>


    fun retryConnection()

    fun fetchConvertedExchangeRatesFromLocalDB(
        code: String,
        amount: Double,
        currencyLst: MutableList<CurrencyModel>
    )

    fun fetchLocalCurrencyCodes()

}


@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeRepository: GeneralDataSource,
    private val convertedExchangeRatesUseCase: ConvertedExchangeRatesUseCase,
    private val stringResourceManager: StringResourceManager,
    private val dispatcher: DispatcherHelper
) : ViewModel(), IExchangeViewModel {

    private val _successSnackBarStates = MutableSharedFlow<String>(replay = 0)
    private val _errorSnackBarStates = MutableSharedFlow<String>(replay = 0)

    val convertedExchangeResult = MutableStateFlow<List<CurrencyMapper>>(arrayListOf())

    val currencyCodes: StateFlow<ExchangeUIStates<List<CurrencyModel>>> =
        exchangeRepository.getLocalLatestCurrencyCodes().map { ExchangeUIStates.Success(it) }
            .stateIn(
                scope = viewModelScope,
                initialValue = ExchangeUIStates.Loading,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    /*
    * Show snackbar if success would occur
    * */
    override fun showSuccessSnackBar(): SharedFlow<String> {
        return _successSnackBarStates
    }

    /*
    * Show snackbar if error would occur
    * */
    override fun showErrorSnackBar(): SharedFlow<String> {
        return _errorSnackBarStates
    }

    /*
    * Observed function for initiate searching
    * */
    override fun fetchConvertedExchangeRatesFromLocalDB(
        code: String,
        amount: Double,
        currencyLst: MutableList<CurrencyModel>
    ) {
        launchItemInScope {
            convertedExchangeRatesUseCase.invoke(code, amount).collect {
                when (it) {
                    is Resource.Error -> {}
                    Resource.Loading -> {
                        _successSnackBarStates.launchItemInCoroutine(
                            dispatcher.dispatcherMain(),
                            stringResourceManager.getString(R.string.converting_currency)
                        )
                    }
                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty())
                            _successSnackBarStates.launchItemInCoroutine(
                                dispatcher.dispatcherMain(),
                                stringResourceManager.getString(R.string.no_rates_found)
                            )
                        else {
                            val combine = it.data.map {
                                CurrencyMapper(
                                    it.code,
                                    currencyLst.find { item -> item.code == it.code }?.country
                                        ?: StringUtils.EMPTY,
                                    it.amount
                                )
                            }
                            convertedExchangeResult.emit(combine)
                        }
                    }
                }
            }
        }
    }

    override fun fetchLocalCurrencyCodes() {
        launchItemInScope {
            exchangeRepository.getLatestCurrenciesFromRemote().collect {
                if (it is Resource.Error) {
                    _errorSnackBarStates.launchItemInCoroutine(
                        dispatcher.dispatcherMain(),
                        it.viewError.message
                    )
                } else if (it is Resource.Success) {
                    exchangeRepository.getLatestExchangeRatesFromRemote().collect {
                        if (it is Resource.Error) {
                            _errorSnackBarStates.launchItemInCoroutine(
                                dispatcher.dispatcherMain(),
                                it.viewError.message
                            )
                        }
                    }
                }
            }
        }
    }

    /*
    * Retry connection if internet is not available
    * */
    override fun retryConnection() {
        fetchLocalCurrencyCodes()
    }

}