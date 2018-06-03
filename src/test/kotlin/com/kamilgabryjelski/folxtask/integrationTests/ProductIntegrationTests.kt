package com.kamilgabryjelski.folxtask.integrationTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import com.kamilgabryjelski.folxtask.server.ProductRepository
import com.kamilgabryjelski.folxtask.server.ProductService
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ProductIntegrationTests {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    protected lateinit var productService: ProductService

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    protected lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        val product1 = Product(name = "prod1", price = 123F, status = ProductStatus.INSTOCK)
        val product2 = Product(name = "prod2", price = 456F, status = ProductStatus.OUTOFSTOCK)
        val product3 = Product(name = "prod3", price = 789F, status = ProductStatus.WITHDRAWN)
        productService.createOrUpdate(product1)
        productService.createOrUpdate(product2)
        productService.createOrUpdate(product3)

        mockMvc = webAppContextSetup(webApplicationContext).build()
    }

    protected fun createURLWithPort(uri: String): String {
        return "http://localhost:$port$uri"
    }
}
