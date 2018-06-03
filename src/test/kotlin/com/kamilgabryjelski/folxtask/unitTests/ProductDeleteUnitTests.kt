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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductDeleteUnitTests: ProductControllerUnitTests() {
    @Test
    fun testDeleteByID() {
        val id = anyLong()
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.of(Product(id)))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYID}?id=$id")
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun testDeleteByID_NoSuchProduct() {
        val id = anyLong()
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYID}?id=$id")
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }

    @Test
    fun testDeleteByName() {
        val name = anyString()
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.of(Product(name = name)))
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYNAME}?name=$name")
        mockMvc.perform(requestBuilder).andExpect(status().isOk)
    }

    @Test
    fun deleteByName_NoSuchProduct() {
        val name = anyString()
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())
        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.delete("${UriConstants.DELETEBYNAME}?name=$name")
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }
}
