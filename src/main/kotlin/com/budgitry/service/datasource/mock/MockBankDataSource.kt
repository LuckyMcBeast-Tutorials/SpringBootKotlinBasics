package com.budgitry.service.datasource.mock

import com.budgitry.service.datasource.BankDataSource
import com.budgitry.service.model.Bank
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository("mock")
class MockBankDataSource : BankDataSource{
    val banks = mutableListOf(
        Bank("123", 3.14, 1),
        Bank("134", 17.0, 137),
        Bank("1234", 0.1, 17),
        )

    override fun retrieveBanks(): Collection<Bank> {
        return banks
    }

    override fun retrieveBank(accountNumber: String): Bank {
        return banks.firstOrNull() { it.accountNumber == accountNumber}
            ?: throw NoSuchElementException("Could not find bank with account number $accountNumber")
    }

    override fun createBank(bank: Bank): Bank {
        if(banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists.")
        }
        banks.add(bank)
        return bank
    }

    override fun patchBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull() {it.accountNumber == bank.accountNumber }
            ?: throw NoSuchElementException("Could not find bank with account number $bank.accountNumber")
        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun deleteBank(accountNumber: String): String {
        val currentBank = banks.firstOrNull() {it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find bank with account number $accountNumber")
        banks.remove(currentBank)
        return accountNumber
    }

}