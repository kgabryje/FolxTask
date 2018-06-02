package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class RestControllerCreateTests: RestControllerTests() {
    @Test
    fun testCreateProduct() {
        val mockProduct = Product(1, "prod1", 123F, ProductStatus.INSTOCK)
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder).andExpect(status().isCreated)
    }

    @Test
    fun testCreateProduct_Withdrawn() {
        val mockProduct = Product()
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest)
    }

    @Test
    fun testCreateProduct_NameExists() {
        val name = "prod"
        val mockProduct = Product(name = name, status = ProductStatus.OUTOFSTOCK)
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(name = name)))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder).andExpect(status().isConflict)
    }
}
