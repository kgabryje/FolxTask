package com.kamilgabryjelski.folxtask

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import com.kamilgabryjelski.folxtask.server.ProductController
import com.kamilgabryjelski.folxtask.server.ProductRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
@WebMvcTest(value = [(ProductController::class)], secure = false)
class RestControllerUpdateTest {
    @MockBean
    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun testUpdateProduct() {
        val id: Long = 1
        val name = "prod"
        val productOnServer: Optional<Product> = Optional.of(Product(id, name))
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(productOnServer)
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.post(UriConstants.UPDATE)
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

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.post(UriConstants.UPDATE)
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
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound)
    }

    @Test
    fun testUpdateProduct_NameExists_DifferentID() {
        val id: Long = 1
        val name = "prod"
        val productOnServer: Optional<Product> = Optional.of(Product(id, name))
        val updatedProduct = Product(id, name, 123F, ProductStatus.INSTOCK)

        Mockito.`when`(productRepository.findById(id)).thenReturn(productOnServer)
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(id+1, name)))

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.post(UriConstants.UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct))
        mockMvc.perform(requestBuilder).andExpect(status().isConflict)
    }
}
