package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductReadUnitTests: ProductControllerUnitTests() {
    @Test
    fun testReadAllProducts() {
        val mockProductList: List<Product> = listOf(
                Product(1, "prod1", 123F, ProductStatus.INSTOCK),
                Product(2, "prod2", 456F),
                Product(3, status = ProductStatus.OUTOFSTOCK)
                )
        Mockito.`when`(productRepository.findAll()).thenReturn(mockProductList)

        val requestBuilder: RequestBuilder = get(UriConstants.READALL)
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()

        val returnedMockProductList: List<Product> = objectMapper.readValue(
                result.response.contentAsString,
                objectMapper.typeFactory.constructCollectionType(List::class.java, Product::class.java)
        )
        assertEquals(mockProductList, returnedMockProductList)
    }

    @Test
    fun testReadAllProducts_Empty() {
        val mockProductList: List<Product> = emptyList()
        Mockito.`when`(productRepository.findAll()).thenReturn(mockProductList)

        val requestBuilder: RequestBuilder = get(UriConstants.READALL)
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()

        val resultList = objectMapper.readValue(result.response.contentAsString, List::class.java)
        assertEquals(mockProductList, resultList)
    }

    @Test
    fun testReadByID() {
        val id = anyLong()
        val mockProduct: Optional<Product> = Optional.of(Product(id, "prod", 123F, ProductStatus.INSTOCK))
        Mockito.`when`(productRepository.findById(id)).thenReturn(mockProduct)

        val requestBuilder: RequestBuilder = get(UriConstants.READBYID).param("id", id.toString())
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()

        val returnProduct: Product = objectMapper.readValue(result.response.contentAsString, Product::class.java)
        assertEquals(mockProduct.get(), returnProduct)
    }

    @Test
    fun testReadByID_IDNotFound() {
        val id = anyLong()
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = get(UriConstants.READBYID).param("id", id.toString())
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    fun testReadByName() {
        val name = anyString()
        val mockProduct: Optional<Product> = Optional.of(Product(1L, name, 123F, ProductStatus.INSTOCK))
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(mockProduct)

        val requestBuilder: RequestBuilder = get(UriConstants.READBYNAME).param("name", name)
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()

        val returnProduct: Product = objectMapper.readValue(result.response.contentAsString, Product::class.java)
        assertEquals(mockProduct.get(), returnProduct)
    }

    @Test
    fun testReadByName_NoSuchProduct() {
        val name = anyString()
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = get(UriConstants.READBYNAME).param("name", name)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }
}
