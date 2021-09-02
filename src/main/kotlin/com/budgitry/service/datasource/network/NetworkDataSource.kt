@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package com.budgitry.service.datasource.network

import com.budgitry.service.datasource.BankDataSource
import com.budgitry.service.datasource.network.dto.BankList
import com.budgitry.service.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException


@Repository("network")
class NetworkDataSource(@Autowired private val restTemplate: RestTemplate) : BankDataSource {


    override fun retrieveBanks(): Collection<Bank> {
        val response : ResponseEntity<BankList> =
            restTemplate.getForEntity("http://54.193.31.159/banks")
        return response.body?.results
            ?: throw IOException("Could not fetch banks from the network")
    }

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun patchBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String): String {
        TODO("Not yet implemented")
    }
}