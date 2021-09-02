package com.budgitry.service.controller

import com.budgitry.service.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("getBanks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should retrieve all banks`() {
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].account_number") { value("123") }
                }
        }
    }

    @Nested
    @DisplayName("getBank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            //given
            val accountNumber = "1234"

            //when/then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value(0.1)}
                    jsonPath("$.default_transaction_fee") { value(17) }
                }
        }
        @Test
        fun `should return Not Found if the account number does not exist`(){
            //given
            val accountNumber = "does_not_exist"

            //when
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("addBank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddBank {
        @Test
        fun `should add the new bank`(){
            //given
            val newBank = Bank("2587", 3.01, 1)
            //when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            //then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.account_number") { value(newBank.accountNumber) }
                    jsonPath("$.trust") { value(newBank.trust) }
                    jsonPath("$.default_transaction_fee") { value(newBank.transactionFee) }
                }
        }

        @Test
        fun `should return BAD REQUEST if given account number already exists`(){
            //given
            val invalidBank = Bank("1234", 1.0, 1)
            //when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            //then
            performPost
                .andDo{ print() }
                .andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("patchBank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchBank {

        @Test
        fun `should update an existing bank`(){
            //given
            val updateBank = Bank("1234", 1.0, 1)
            //when
            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updateBank)}
            //then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updateBank))
                    }
                }
            mockMvc.get("$baseUrl/${updateBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updateBank)) } }
        }

        @Test
        fun `should return NOT FOUND if given account number does not exists`(){
            //given
            val invalidBank = Bank("NotAnAccount", 1.0, 1)
            //when
            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            //then
            performPatch
                .andDo{ print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("deleteBank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {

        @Test
        @DirtiesContext
        fun `should remove existing bank`(){
            //given
            val accountNumber = "1234"
            //when
            val deleteBank = mockMvc.delete("$baseUrl/$accountNumber")
            //then
            deleteBank
                .andDo{ print() }
                .andExpect {
                    status { isAccepted() }
                    content { string(accountNumber) }
                }
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo{ print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND if given account number does not exists`(){
            //given
            val invalidAccount = "NotAnAccount"
            //when
            val deleteBank = mockMvc.delete("$baseUrl/$invalidAccount")
            //then
            deleteBank
                .andDo{ print() }
                .andExpect { status { isNotFound() } }
        }
    }
}
