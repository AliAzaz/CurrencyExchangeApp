package com.currencyexchange.app.model

import com.google.gson.JsonObject

data class ExchangeRatesModel(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: JsonObject,
    val timestamp: Int
)