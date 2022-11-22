package com.currencyexchange.app.network

import com.currencyexchange.app.di.module.NetworkApiModuleTest
import com.currencyexchange.app.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class BackendApiTest : NetworkApiModuleTest<BackendApi>() {

    private lateinit var apiService: BackendApi

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        apiService = createService(BackendApi::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun `test getLatestExchangeRates() returns Latest Exchange Information`() = runBlocking {
        // Given
        enqueueResponse("/exchange_currencies_response.json")

        // Invoke
        val response = apiService.getLatestExchangeRates()
        val responseBody = requireNotNull(response.body())
        mockWebServer.takeRequest()

        // Then
        MatcherAssert.assertThat(
            responseBody.disclaimer,
            CoreMatchers.`is`("Usage subject to terms: https://openexchangerates.org/terms")
        )
        MatcherAssert.assertThat(
            responseBody.license,
            CoreMatchers.`is`("https://openexchangerates.org/license")
        )
        MatcherAssert.assertThat(responseBody.timestamp, CoreMatchers.`is`(1669132800))
        MatcherAssert.assertThat(responseBody.base, CoreMatchers.`is`("USD"))
    }


}