package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductUpdateUnitTests: ProductControllerUnitTests() {
    @Test
    fun testUpdateProduct() {
        val id: Long = 1
        val name = "prod"
        val productOnServer: Optional<Product> = Optional.of(Product(id, name))
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(productOnServer)
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun testUpdateProduct_NameExists_SameID() {
        val id: Long = 1
        val name = "prod"
        val productOnServer: Optional<Product> = Optional.of(Product(id, name))
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(productOnServer)
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(productOnServer)

        val requestBuilder: RequestBuilder = post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun testUpdateProduct_IDNotFound() {
        val id: Long = 1
        val name = "prod"
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    fun testUpdateProduct_NameExists_DifferentID() {
        val id: Long = 1
        val name = "prod"
        val productOnServer: Optional<Product> = Optional.of(Product(id, name))
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(productOnServer)
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(id+1, name)))

        val requestBuilder: RequestBuilder = post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict)
                .andExpect(status().reason(HttpStatusReasonConstants.NAMEEXISTS))
    }
}
