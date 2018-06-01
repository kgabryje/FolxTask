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
class RestControllerCreateTests {
    @MockBean
    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

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
