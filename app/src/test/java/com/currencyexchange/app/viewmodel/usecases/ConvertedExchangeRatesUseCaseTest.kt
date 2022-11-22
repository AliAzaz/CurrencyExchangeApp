package com.currencyexchange.app.viewmodel.usecases

import app.cash.turbine.test
import com.currencyexchange.app.di.repository.GeneralDataSource
import com.currencyexchange.app.helper.Resource
import com.currencyexchange.app.utils.MainDispatcherRule
import com.currencyexchange.app.utils.MockKTestUtil
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class ConvertedExchangeRatesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repository: GeneralDataSource
    private lateinit var useCase: ConvertedExchangeRatesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ConvertedExchangeRatesUseCase(repository)
    }

    @Test
    fun `Test exchange rate usecase is returning updated amount RateModel object`() = runBlocking {
        //Given
        val mockRatesModel = MockKTestUtil.getSampleListRatesModel()
        val code = "USD"
        val amount = 1.0

        //When
        coEvery {
            repository.getLatestExchangeRatesFromLocal()
        } returns flowOf(mockRatesModel)

        //Invoke
        val ratesModelFlow = useCase(code, amount)

        //Then
        MatcherAssert.assertThat(ratesModelFlow, CoreMatchers.notNullValue())

        ratesModelFlow.test {
            awaitItem()
            val flowItem = (awaitItem() as Resource.Success).data

            MatcherAssert.assertThat(flowItem, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(flowItem!!.size, CoreMatchers.`is`(mockRatesModel.size - 1))
            awaitComplete()
        }
    }

}