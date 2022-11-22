package com.currencyexchange.app.remote

import com.currencyexchange.app.model.ExchangeRatesModel
import com.currencyexchange.app.network.BackendApi
import com.currencyexchange.app.network.DefaultBaseRemoteDataSource
import com.currencyexchange.app.network.NetworkResponseResult
import com.google.gson.JsonObject
import javax.inject.Inject


interface BaseRemoteGeneralDataSource {
    suspend fun getLatestExchangeRates(): NetworkResponseResult<ExchangeRatesModel>
    suspend fun getCurrencies(): NetworkResponseResult<JsonObject>
}

class DefaultRemoteGeneralDataSource @Inject constructor(private val backendApi: BackendApi) :
    BaseRemoteGeneralDataSource, DefaultBaseRemoteDataSource() {

    override suspend fun getCurrencies(): NetworkResponseResult<JsonObject> {
        return getResults {
            backendApi.getCurrencies()
        }
    }

    override suspend fun getLatestExchangeRates(): NetworkResponseResult<ExchangeRatesModel> {
        return getResults {
            backendApi.getLatestExchangeRates()
        }
    }

}