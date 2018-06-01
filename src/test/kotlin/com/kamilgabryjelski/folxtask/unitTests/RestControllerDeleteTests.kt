package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.server.ProductController
import com.kamilgabryjelski.folxtask.server.ProductRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
@WebMvcTest(value = [(ProductController::class)], secure = false)
class RestControllerDeleteTests {
    @MockBean
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testDeleteByID() {
        val id: Long = 1
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.of(Product(id)))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYID}?id=$id")
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun testDeleteByID_NoSuchProduct() {
        val id: Long = 1
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYID}?id=$id")
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound)
    }

    @Test
    fun testDeleteByName() {
        val name = "prod"
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(name = name)))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYNAME}?name=$name")
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun deleteByName_NoSuchProduct() {
        val name = "prod"
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYNAME}?name=$name")
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound)
    }
}
