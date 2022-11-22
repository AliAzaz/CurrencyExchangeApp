package com.currencyexchange.app.db

import androidx.room.*
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.model.RatesModel
import com.currencyexchange.app.utils.CONSTANTS
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllExchangeRates(exchangeRates: List<RatesModel>)

    @Query("DELETE FROM ${CONSTANTS.EXCHANGE_RATES_TABLE}")
    fun deleteAllExchangeRates()

    @Transaction
    fun saveExchangeRatesData(exchangeRates: List<RatesModel>) {
        deleteAllExchangeRates()
        insertAllExchangeRates(exchangeRates)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCurrencies(exchangeRates: List<CurrencyModel>)

    @Query("DELETE FROM ${CONSTANTS.CURRENCIES_TABLE}")
    fun deleteAllCurrencies()

    @Transaction
    fun saveCurrenciesData(exchangeRates: List<CurrencyModel>) {
        deleteAllCurrencies()
        insertAllCurrencies(exchangeRates)
    }

    @Query("SELECT * FROM ${CONSTANTS.EXCHANGE_RATES_TABLE}")
    fun getLatestExchangeRates(): Flow<List<RatesModel>>

    @Query("SELECT * FROM ${CONSTANTS.CURRENCIES_TABLE}")
    fun getExchangeCurrencyCodes(): Flow<List<CurrencyModel>>

}