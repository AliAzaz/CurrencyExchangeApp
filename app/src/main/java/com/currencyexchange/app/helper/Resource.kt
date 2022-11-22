package com.currencyexchange.app.helper

import com.currencyexchange.app.network.ViewError

sealed interface Resource<out T> {
    data class Success<T>(val data: T? = null) : Resource<T>
    object Loading : Resource<Nothing>
    data class Error(val viewError: ViewError) : Resource<Nothing>
}