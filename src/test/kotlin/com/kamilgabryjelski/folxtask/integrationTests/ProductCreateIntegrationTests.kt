package com.kamilgabryjelski.folxtask.integrationTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
class ProductCreateIntegrationTests: ProductIntegrationTests() {
    @Test
    @Transactional
    fun createProduct() {
        val name = "new_product"
        assertFalse(productService.findByName(name).isPresent)

        val product = Product(name = name, status = ProductStatus.INSTOCK)
        val builder: MockHttpServletRequestBuilder = put(createURLWithPort(UriConstants.CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        mockMvc.perform(builder).andExpect(status().isCreated)

        assertTrue(productService.findByName(name).isPresent)
    }

    @Test
    @Transactional
    fun createProduct_NameExists() {
        val name = productService.findAll()[0].name
        assertTrue(productService.findByName(name).isPresent)

        val product = Product(name = name, status = ProductStatus.INSTOCK)
        val builder: MockHttpServletRequestBuilder = put(createURLWithPort(UriConstants.CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        mockMvc.perform(builder)
                .andExpect(status().isConflict)
                .andExpect(status().reason(HttpStatusReasonConstants.NAMEEXISTS))
    }

    @Test
    @Transactional
    fun createProduct_Withdrawn() {
        val product = Product(status = ProductStatus.WITHDRAWN)
        val builder: MockHttpServletRequestBuilder = put(createURLWithPort(UriConstants.CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        mockMvc.perform(builder)
                .andExpect(status().isBadRequest)
                .andExpect(status().reason(HttpStatusReasonConstants.WITHDRAWN))
    }

    @Test
    @Transactional
    fun createProduct_IDExists() {
        val id = productService.findAll()[0].id
        assertTrue(productService.findByID(id).isPresent)

        val product = Product(id = id, status = ProductStatus.INSTOCK)
        val builder: MockHttpServletRequestBuilder = put(createURLWithPort(UriConstants.CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        mockMvc.perform(builder)
                .andExpect(status().isConflict)
                .andExpect(status().reason(HttpStatusReasonConstants.IDEXISTS))
    }
}
