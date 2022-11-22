package com.currencyexchange.app.utils

import com.currencyexchange.app.model.RatesModel

object MockKTestUtil {

    fun createSampleRatesModel(id: Int, code: String, amount: Double) =
        RatesModel(id = id, code = code, amount = amount)

    fun getSampleListRatesModel(): List<RatesModel> {
        return arrayListOf(
            createSampleRatesModel(1, "USD", 1.0),
            createSampleRatesModel(2, "PKR", 223.0),
            createSampleRatesModel(3, "SAR", 3.0),
        )
    }

}