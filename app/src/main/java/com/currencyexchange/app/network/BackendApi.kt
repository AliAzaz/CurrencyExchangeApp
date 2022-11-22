package com.currencyexchange.app.network

import com.currencyexchange.app.model.ExchangeRatesModel
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author AliAzazAlam on 11/20/2022.
 */
interface BackendApi {

    @GET(ApiRoutes.GET_EXCHANGE_DATA)
    suspend fun getLatestExchangeRates(): Response<ExchangeRatesModel>

    @GET(ApiRoutes.GET_CURRENCIES_DATA)
    suspend fun getCurrencies(): Response<JsonObject>

}