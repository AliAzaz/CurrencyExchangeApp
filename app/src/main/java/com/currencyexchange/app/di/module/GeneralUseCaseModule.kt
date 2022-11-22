package com.currencyexchange.app.di.module

import com.currencyexchange.app.di.repository.GeneralDataSource
import com.currencyexchange.app.viewmodel.usecases.ConvertedExchangeRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeneralUseCaseModule {

    @Provides
    @Singleton
    fun provideLocalExchangeRateUseCase(
        generalDataSource: GeneralDataSource
    ) = ConvertedExchangeRatesUseCase(generalDataSource)

}