package com.kamilgabryjelski.folxtask.unitTests

import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
class ProductReadUnitUnitTests: ProductControllerUnitTests() {
    @Test
    fun testReadAllProducts() {
        val mockProductList: List<Product> = listOf(
                Product(1, "prod1", 123F, ProductStatus.INSTOCK),
                Product(2, "prod2", 456F),
                Product(3, status = ProductStatus.OUTOFSTOCK)
                )
        Mockito.`when`(productRepository.findAll()).thenReturn(mockProductList)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get(UriConstants.READALL)
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()
        val expected = """
            [{id: 1, name: prod1, price: 123, status: INSTOCK},
            {id: 2, name: prod2, price: 456, status: WITHDRAWN},
            {id: 3, name: "", price: 0, status: OUTOFSTOCK}]
            """
                .trimIndent()

        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }

    @Test
    fun testReadAllProducts_Empty() {
        val mockProductList: List<Product> = emptyList()
        Mockito.`when`(productRepository.findAll()).thenReturn(mockProductList)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get(UriConstants.READALL)
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()
        val expected = "[]"

        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }

    @Test
    fun testReadByID() {
        val id: Long = 1
        val mockProduct: Optional<Product> = Optional.of(Product(1, "prod1", 123F, ProductStatus.INSTOCK))
        Mockito.`when`(productRepository.findById(id)).thenReturn(mockProduct)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("${UriConstants.READBYID}?id=$id")
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()
        val expected = "{id: 1, name: prod1, price: 123, status: INSTOCK}"
        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }

    @Test
    fun testReadByID_IDNotFound() {
        val id: Long = 4
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("${UriConstants.READBYID}?id=$id")
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound)
    }

    @Test
    fun testReadByName() {
        val name = "prod1"
        val mockProduct: Optional<Product> = Optional.of(Product(1, "prod1", 123F, ProductStatus.INSTOCK))
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(mockProduct)

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("${UriConstants.READBYNAME}?name=$name")
        val result: MvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk).andReturn()
        val expected = "{id: 1, name: prod1, price: 123, status: INSTOCK}"
        JSONAssert.assertEquals(expected, result.response.contentAsString, false)
    }

    @Test
    fun testReadByName_NoSuchProduct() {
        val name = "prod4"
        Mockito.`when`(productRepository.findProductByName(name)).thenReturn(Optional.empty())

        val requestBuilder: RequestBuilder = MockMvcRequestBuilders.get("${UriConstants.READBYNAME}?name=$name")
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound)
    }
}
