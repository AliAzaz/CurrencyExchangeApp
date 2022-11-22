package com.currencyexchange.app.helper

sealed interface ExchangeUIStates<out T> {
    object Loading : ExchangeUIStates<Nothing>
    data class Success<T>(val data: T? = null) : ExchangeUIStates<T>
}