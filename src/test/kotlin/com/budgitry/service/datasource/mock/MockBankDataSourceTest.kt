package com.budgitry.service.datasource.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MockBankDataSourceTest {

    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`() {
        //when
        val banks = mockDataSource.retrieveBanks()
        //then
        assertThat(banks.size).isGreaterThanOrEqualTo(3)

    }

    @Test
    fun `should provide some mock data`() {
        //when
        val banks = mockDataSource.retrieveBanks()

        //then
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).allMatch { it.trust != 0.0 }
        assertThat(banks).allMatch { it.transactionFee != 0 }
    }

    @Test
    fun `should have unique account numbers`() {
        //when
        val banks = mockDataSource.retrieveBanks()
        var previousAccountNumber = ""
        //then
        for (bank in banks) {
            assertThat(bank.accountNumber).isNotEqualTo(previousAccountNumber)
            previousAccountNumber = bank.accountNumber
        }
    }
}