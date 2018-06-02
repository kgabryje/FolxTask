package com.kamilgabryjelski.folxtask.unitTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamilgabryjelski.folxtask.server.ProductController
import com.kamilgabryjelski.folxtask.server.ProductRepository
import com.kamilgabryjelski.folxtask.server.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(value = [(ProductController::class)], secure = false)
open class RestControllerTests {
    @MockBean
    @Autowired
    protected lateinit var productRepository: ProductRepository

    @Autowired
    protected lateinit var productService: ProductService

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @TestConfiguration
    class ProductServiceConfig {
        @Bean
        fun productService(): ProductService {
            return ProductService()
        }
    }
}