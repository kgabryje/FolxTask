package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductDeleteUnitTests: ProductControllerUnitTests() {
    @Test
    fun testDeleteByID() {
        val id = anyLong()
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.of(Product(id)))
        val requestBuilder: RequestBuilder = delete(UriConstants.DELETEBYID).param("id", id.toString())
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun testDeleteByID_IDNotFound() {
        val id = anyLong()
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = delete(UriConstants.DELETEBYID).param("id", id.toString())
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    fun testDeleteByName() {
        val name = anyString()
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(name = name)))
        val requestBuilder: RequestBuilder = delete(UriConstants.DELETEBYNAME).param("name", name)
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun deleteByName_NoSuchProduct() {
        val name = anyString()
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = delete(UriConstants.DELETEBYNAME).param("name", name)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }
}
