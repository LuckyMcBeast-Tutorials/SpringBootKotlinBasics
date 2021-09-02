package com.budgitry.service.service

import com.budgitry.service.datasource.BankDataSource
import com.budgitry.service.model.Bank
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class BankService(@Qualifier("mock") private val dataSource: BankDataSource) {

    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()

    fun getBank(accountNumber: String): Bank {
        return dataSource.retrieveBank(accountNumber)
    }

    fun addBank(bank: Bank): Bank {
        return dataSource.createBank(bank)
    }

    fun updateBank(bank: Bank): Bank {
        return dataSource.patchBank(bank)
    }

    fun removeBank(accountNumber: String): Any {
        return dataSource.deleteBank(accountNumber)
    }
}