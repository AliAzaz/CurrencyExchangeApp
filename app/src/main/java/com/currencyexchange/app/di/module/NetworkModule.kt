package com.currencyexchange.app.di.module

import android.content.Context
import com.currencyexchange.app.network.DefaultErrorStateMapper
import com.currencyexchange.app.network.ErrorStateMapper
import com.currencyexchange.app.utils.DefaultStringResourceManager
import com.currencyexchange.app.utils.StringResourceManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideStringResManager(@ApplicationContext context: Context): StringResourceManager =
        DefaultStringResourceManager(context)


    @Provides
    @Singleton
    fun provideErrorStateMapper(
        stringResourceManager: StringResourceManager,
        gson: Gson
    ): ErrorStateMapper =
        DefaultErrorStateMapper(stringResourceManager, gson)

}