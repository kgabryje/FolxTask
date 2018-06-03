package com.kamilgabryjelski.folxtask.unitTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamilgabryjelski.folxtask.server.ProductRepository
import com.kamilgabryjelski.folxtask.server.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
abstract class ProductControllerUnitTests {
    @MockBean
    @Autowired
    protected lateinit var productRepository: ProductRepository

    @Autowired
    protected lateinit var productService: ProductService

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper
}
