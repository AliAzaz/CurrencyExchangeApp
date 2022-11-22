package com.currencyexchange.app.di.module

import android.content.Context
import com.currencyexchange.app.db.ExchangeAppDatabase
import com.currencyexchange.app.db.ExchangeDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author ali azaz on 11/22/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideExchangeAppDatabase(@ApplicationContext context: Context): ExchangeAppDatabase {
        return ExchangeAppDatabase.invoke(context)
    }

    @Singleton
    @Provides
    fun getDAO(vehicleAppDatabase: ExchangeAppDatabase): ExchangeDAO {
        return vehicleAppDatabase.exchangeDao()
    }

}