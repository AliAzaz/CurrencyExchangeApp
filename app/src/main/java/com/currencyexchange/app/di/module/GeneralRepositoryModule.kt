package com.currencyexchange.app.di.module

import com.currencyexchange.app.db.ExchangeDAO
import com.currencyexchange.app.di.repository.GeneralDataSource
import com.currencyexchange.app.di.repository.GeneralRepository
import com.currencyexchange.app.helper.DispatcherHelper
import com.currencyexchange.app.local.BaseLocalGeneralDataSource
import com.currencyexchange.app.local.DefaultLocalGeneralDataSource
import com.currencyexchange.app.network.BackendApi
import com.currencyexchange.app.network.ErrorStateMapper
import com.currencyexchange.app.remote.BaseRemoteGeneralDataSource
import com.currencyexchange.app.remote.DefaultRemoteGeneralDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeneralRepositoryModule {

    @Singleton
    @Provides
    fun provideGeneralDataSource(
        remoteGeneralDataSource: BaseRemoteGeneralDataSource,
        localGeneralDataSource: BaseLocalGeneralDataSource,
        errorStateMapper: ErrorStateMapper,
        dispatcherHelper: DispatcherHelper
    ): GeneralDataSource {
        return GeneralRepository(
            remoteGeneralDataSource,
            localGeneralDataSource,
            errorStateMapper,
            dispatcherHelper
        )
    }

    @Singleton
    @Provides
    fun provideRemoteGeneralDataSource(backendApi: BackendApi): BaseRemoteGeneralDataSource {
        return DefaultRemoteGeneralDataSource(backendApi)
    }

    @Singleton
    @Provides
    fun provideLocalGeneralDataSource(exchangeDAO: ExchangeDAO): BaseLocalGeneralDataSource {
        return DefaultLocalGeneralDataSource(exchangeDAO)
    }

}