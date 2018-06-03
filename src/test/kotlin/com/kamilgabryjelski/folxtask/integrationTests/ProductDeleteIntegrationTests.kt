package com.kamilgabryjelski.folxtask.integrationTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
class ProductDeleteIntegrationTests: ProductIntegrationTests() {
    @Test
    @Transactional
    fun testDeleteProductById() {
        val id = productService.findAll()[0].id

        val builder: MockHttpServletRequestBuilder = delete(createURLWithPort("${UriConstants.DELETEBYID}?id=$id"))
        mockMvc.perform(builder).andExpect(status().isOk)

        assertFalse(productService.findByID(id).isPresent)
    }

    @Test
    @Transactional
    fun testDeleteProductById_IDNotFound() {
        val id = -1L
        assertFalse(productService.findByID(id).isPresent)

        val builder: MockHttpServletRequestBuilder = delete(createURLWithPort("${UriConstants.DELETEBYID}?id=$id"))
        mockMvc.perform(builder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    @Transactional
    fun testDeleteProductByName() {
        val name = productService.findAll()[0].name

        val builder: MockHttpServletRequestBuilder = delete(createURLWithPort("${UriConstants.DELETEBYNAME}?name=$name"))
        mockMvc.perform(builder).andExpect(status().isOk)

        assertFalse(productService.findByName(name).isPresent)
    }

    @Test
    @Transactional
    fun testDeleteProductByName_NoSuchProduct() {
        val name = "no_such_product"
        assertFalse(productService.findByName(name).isPresent)

        val builder: MockHttpServletRequestBuilder = delete(createURLWithPort("${UriConstants.DELETEBYNAME}?name=$name"))
        mockMvc.perform(builder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }
}
