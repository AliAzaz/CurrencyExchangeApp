package com.currencyexchange.app.di.repository

import com.currencyexchange.app.helper.IDispatcher
import com.currencyexchange.app.helper.Resource
import com.currencyexchange.app.local.BaseLocalGeneralDataSource
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.model.RatesModel
import com.currencyexchange.app.network.ErrorStateMapper
import com.currencyexchange.app.network.NetworkResponseResult
import com.currencyexchange.app.remote.BaseRemoteGeneralDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author AliAzazAlam on 11/20/2022.
 */
interface GeneralDataSource {

    suspend fun getLatestCurrenciesFromRemote(): Flow<Resource<Unit>>

    suspend fun getLatestExchangeRatesFromRemote(): Flow<Resource<Unit>>

    suspend fun getLatestExchangeRatesFromLocal(): Flow<List<RatesModel>>

    fun getLocalLatestCurrencyCodes(): Flow<List<CurrencyModel>>
}

class GeneralRepository @Inject constructor(
    private val remoteGeneralDataSource: BaseRemoteGeneralDataSource,
    private val localGeneralDataSource: BaseLocalGeneralDataSource,
    private val errorStateMapper: ErrorStateMapper,
    private val dispatcher: IDispatcher
) : GeneralDataSource {

    override suspend fun getLatestCurrenciesFromRemote(): Flow<Resource<Unit>> {
        return flow {
            when (val currencies = remoteGeneralDataSource.getCurrencies()) {
                is NetworkResponseResult.Success -> {

                    val currencyLst = currencies.data.asMap()
                        .map { CurrencyModel(code = it.key, country = it.value.asString) }

                    CoroutineScope(dispatcher.dispatcherIO()).launch {
                        localGeneralDataSource.insertCurrencies(currencyLst)
                    }
                    emit(Resource.Success())
                }
                else -> emit(errorStateMapper.map(currencies))
            }
        }
    }

    override suspend fun getLatestExchangeRatesFromRemote(): Flow<Resource<Unit>> {
        return flow {
            when (val result = remoteGeneralDataSource.getLatestExchangeRates()) {
                is NetworkResponseResult.Success -> {
                    val rates = result.data.rates.asMap()
                        .map {
                            RatesModel(
                                code = it.key,
                                amount = it.value.asDouble
                            )
                        }
                    CoroutineScope(dispatcher.dispatcherIO()).launch {
                        localGeneralDataSource.insertLatestExchangeRates(rates)
                    }
                    emit(Resource.Success())
                }
                else -> emit(errorStateMapper.map(result))
            }
        }
    }

    override suspend fun getLatestExchangeRatesFromLocal(): Flow<List<RatesModel>> {
        return localGeneralDataSource.getLocalLatestExchangeRates()
    }

    override fun getLocalLatestCurrencyCodes(): Flow<List<CurrencyModel>> {
        return localGeneralDataSource.getLocalLatestCurrencyCodes()
    }

}