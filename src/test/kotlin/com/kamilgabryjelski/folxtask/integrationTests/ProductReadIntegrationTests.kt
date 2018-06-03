package com.kamilgabryjelski.folxtask.integrationTests

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
class ProductReadIntegrationTests: ProductIntegrationTests() {
    @Test
    @Transactional
    fun testReadAll() {
        val expectedOnServer = productService.findAll()

        val builder: MockHttpServletRequestBuilder = get(createURLWithPort(UriConstants.READALL))
        val result = mockMvc.perform(builder).andExpect(status().isOk).andReturn()

        val returnedProductList: List<Product> = objectMapper.readValue(
                result.response.contentAsString,
                objectMapper.typeFactory.constructCollectionType(List::class.java, Product::class.java)
        )

        assertEquals(expectedOnServer, returnedProductList)
    }

    @Test
    @Transactional
    fun testReadByID() {
        val expectedOnServer = productService.findAll()[0]
        val id = expectedOnServer.id

        val builder: MockHttpServletRequestBuilder =
                get(createURLWithPort(UriConstants.READBYID)).param("id", id.toString())
        val result = mockMvc.perform(builder).andExpect(status().isOk).andReturn()

        assertEquals(expectedOnServer, objectMapper.readValue(result.response.contentAsString, Product::class.java))
    }

    @Test
    @Transactional
    fun testReadByID_NoSuchProduct() {
        val id = -1L

        val builder: MockHttpServletRequestBuilder =
                get(createURLWithPort(UriConstants.READBYID)).param("id", id.toString())
        mockMvc.perform(builder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.IDNOTFOUND))
    }

    @Test
    @Transactional
    fun testReadByName() {
        val expectedOnServer = productService.findAll()[0]
        val name = expectedOnServer.name

        val builder: MockHttpServletRequestBuilder =
                get(createURLWithPort(UriConstants.READBYNAME)).param("name", name)
        val result = mockMvc.perform(builder).andExpect(status().isOk).andReturn()

        assertEquals(expectedOnServer, objectMapper.readValue(result.response.contentAsString, Product::class.java))
    }

    @Test
    @Transactional
    fun testReadByName_NoSuchProduct() {
        val name = "no_such_product"

        val builder: MockHttpServletRequestBuilder =
                get(createURLWithPort(UriConstants.READBYNAME)).param("name", name)
        mockMvc.perform(builder)
                .andExpect(status().isNotFound)
                .andExpect(status().reason(HttpStatusReasonConstants.NOSUCHPRODUCT))
    }
}
