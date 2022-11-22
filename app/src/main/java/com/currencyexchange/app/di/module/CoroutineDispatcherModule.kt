package com.currencyexchange.app.di.module

import com.currencyexchange.app.helper.DispatcherHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatcher() = DispatcherHelper()

}