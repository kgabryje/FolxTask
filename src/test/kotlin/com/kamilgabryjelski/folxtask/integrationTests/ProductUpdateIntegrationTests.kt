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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
class ProductUpdateIntegrationTests: ProductIntegrationTests() {
    @Test
    @Transactional
    fun testUpdateProduct_NewName() {
        val productOnServer = productService.findAll()[0]
        val id = productOnServer.id
        val newProductName = "updated_product_name"

        val newProduct = Product(id, newProductName, 234F, ProductStatus.WITHDRAWN)

        val builder: MockHttpServletRequestBuilder = post(createURLWithPort(UriConstants.UPDATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
        mockMvc.perform(builder).andExpect(status().isOk)

        assertTrue(productService.findByID(id).get() == newProduct)
    }

    @Test
    @Transactional
    fun testUpdateProduct_NameExists_SameID() {
        val productOnServer = productService.findAll()[0]
        val id = productOnServer.id
        val name = productOnServer.name

        val newProduct = Product(id, name, 234F, ProductStatus.WITHDRAWN)

        val builder: MockHttpServletRequestBuilder = post(createURLWithPort(UriConstants.UPDATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
        mockMvc.perform(builder).andExpect(status().isOk)

        assertTrue(productService.findByID(id).get() == newProduct)
        assertTrue(productService.findByName(name).get() == newProduct)
    }

    @Test
    @Transactional
    fun testUpdateProduct_IDNotFound() {
        val id = -1L
        val newProduct = Product(id = id)

        assertFalse(productService.findByID(id).isPresent)

        val builder: MockHttpServletRequestBuilder = post(createURLWithPort(UriConstants.UPDATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
        mockMvc.perform(builder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    @Transactional
    fun testUpdateProduct_NameExists_DifferentID() {
        val productOnServer1 = productService.findAll()[0]
        val productOnServer2 = productService.findAll()[1]
        val id = productOnServer1.id
        val name = productOnServer2.name

        val newProduct = Product(id, name, 111F, ProductStatus.OUTOFSTOCK)

        assertFalse(productOnServer1 == productOnServer2)

        val builder: MockHttpServletRequestBuilder = post(createURLWithPort(UriConstants.UPDATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
        mockMvc.perform(builder)
                .andExpect(status().isConflict)
                .andExpect(status().reason(HttpStatusReasonConstants.NAMEEXISTS))
    }
}
