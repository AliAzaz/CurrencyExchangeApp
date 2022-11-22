package com.currencyexchange.app.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


fun <T> MutableLiveData<T>.launchItemInCoroutine(iDispatcher: CoroutineDispatcher, data: T) {
    CoroutineScope(iDispatcher).launch {
        postValue(data)
    }
}

fun <T> MutableSharedFlow<T>.launchItemInCoroutine(iDispatcher: CoroutineDispatcher, data: T) {
    CoroutineScope(iDispatcher).launch {
        emit(data)
    }
}

fun Fragment.launchItemInScope(item: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            item.invoke()
        }
    }
}

fun ViewModel.launchItemInScope(item: suspend () -> Unit) {
    viewModelScope.launch {
        item.invoke()
    }
}