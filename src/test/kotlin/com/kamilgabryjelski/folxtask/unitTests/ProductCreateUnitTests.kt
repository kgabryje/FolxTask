package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductCreateUnitTests: ProductControllerUnitTests() {
    @Test
    fun testCreateProduct() {
        val mockProduct = Product(1, "prod1", 123F, ProductStatus.INSTOCK)
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        val requestBuilder: RequestBuilder = put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder).andExpect(status().isCreated)
    }

    @Test
    fun testCreateProduct_Withdrawn() {
        val mockProduct = Product()
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        val requestBuilder: RequestBuilder = put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest)
                .andExpect(status().reason(HttpStatusReasonConstants.WITHDRAWN))
    }

    @Test
    fun testCreateProduct_NameExists() {
        val name = anyString()
        val mockProduct = Product(name = name, status = ProductStatus.OUTOFSTOCK)
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(name = name)))
        val requestBuilder: RequestBuilder = put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict)
                .andExpect(status().reason(HttpStatusReasonConstants.NAMEEXISTS))
    }

    @Test
    fun testCreateProduct_IDExists() {
        val id = anyLong()
        val mockProduct = Product(id = id, status = ProductStatus.INSTOCK)
        val mockProductJSON = objectMapper.writeValueAsString(mockProduct)

        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.of(Product(id = id)))
        val requestBuilder: RequestBuilder = put(UriConstants.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockProductJSON)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest)
                .andExpect(status().reason(HttpStatusReasonConstants.IDEXISTS))
    }
}
