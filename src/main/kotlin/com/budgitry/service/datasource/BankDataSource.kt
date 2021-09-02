package com.budgitry.service.datasource

import com.budgitry.service.model.Bank

interface BankDataSource {

    fun retrieveBanks(): Collection<Bank>

    fun retrieveBank(accountNumber: String) : Bank

    fun createBank(bank: Bank) : Bank

    fun patchBank(bank: Bank): Bank

    fun deleteBank(accountNumber: String): String

}