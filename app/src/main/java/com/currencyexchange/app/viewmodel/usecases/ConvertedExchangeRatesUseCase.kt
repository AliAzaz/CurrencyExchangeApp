package com.currencyexchange.app.viewmodel.usecases

import com.currencyexchange.app.di.repository.GeneralDataSource
import com.currencyexchange.app.helper.Resource
import com.currencyexchange.app.model.RatesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author AliAzazAlam on 11/20/2022.
 */
class ConvertedExchangeRatesUseCase @Inject constructor(
    private val repository: GeneralDataSource
) {
    operator fun invoke(
        code: String,
        amount: Double
    ): Flow<Resource<List<RatesModel>>> = flow {
        emit(Resource.Loading)
        repository.getLatestExchangeRatesFromLocal().collect {
            val currencyRate = it.find { item -> item.code == code }
            val convertedRate = amount.div(currencyRate!!.amount)
            val result = it.filter { it.code != code }
                .map { item -> item.copy(amount = convertedRate.times(item.amount)) }
            emit(Resource.Success(result))
        }
    }
}