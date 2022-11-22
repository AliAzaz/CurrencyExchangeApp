package com.currencyexchange.app.local

import com.currencyexchange.app.db.ExchangeDAO
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.model.RatesModel
import com.currencyexchange.app.network.DefaultBaseRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface BaseLocalGeneralDataSource {
    suspend fun getLocalLatestExchangeRates(): Flow<List<RatesModel>>
    suspend fun insertLatestExchangeRates(lstExchangeRates: List<RatesModel>)
    suspend fun insertCurrencies(lstCurrencies: List<CurrencyModel>)
    fun getLocalLatestCurrencyCodes(): Flow<List<CurrencyModel>>
}

class DefaultLocalGeneralDataSource @Inject constructor(private val exchangeDAO: ExchangeDAO) :
    BaseLocalGeneralDataSource, DefaultBaseRemoteDataSource() {

    override suspend fun getLocalLatestExchangeRates(): Flow<List<RatesModel>> {
        return exchangeDAO.getLatestExchangeRates()
    }

    override fun getLocalLatestCurrencyCodes(): Flow<List<CurrencyModel>> {
        return exchangeDAO.getExchangeCurrencyCodes()
    }

    override suspend fun insertLatestExchangeRates(lstExchangeRates: List<RatesModel>) {
        exchangeDAO.saveExchangeRatesData(lstExchangeRates)
    }

    override suspend fun insertCurrencies(lstCurrencies: List<CurrencyModel>) {
        exchangeDAO.saveCurrenciesData(lstCurrencies)
    }

}