package com.kamilgabryjelski.folxtask.client

import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class ProductClient: CommandLineRunner{
    @Autowired
    lateinit var restTemplate: RestTemplate
    val hostURI = "http://localhost:8080"
    val logger = LoggerFactory.getLogger(ProductClient::class.java)


    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    override fun run(vararg args: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createProduct(name: String, price: Float, status: ProductStatus) {
        val product = Product(name = name, price = price, status = status)
        val uri = "$hostURI/createProduct"
        restTemplate.put(uri, product)
    }

    fun readAllProducts(): List<Product>? {
        val uri = "$hostURI/readAll"
        val products: List<Product>? = restTemplate.getForObject(uri, List::class.java) as? List<Product>
        return products
    }

    fun readProductByID(id: Long): Product? {
        val uri = "$hostURI/readByID?id=$id"
        val product: Product? = restTemplate.getForObject(uri, Product::class.java)
        return product
    }

    fun readProductByName(name: String): Product? {
        val uri = "$hostURI/readByName?name=$name"
        val product: Product? = restTemplate.getForObject(uri, Product::class.java)
        return product
    }

    fun updateProduct(id: Long, name: String = "", price: Float = 0F, status: ProductStatus = ProductStatus.WITHDRAWN) {
        val uri = "$hostURI/updateProduct"
        val product = Product(id, name, price, status)
        restTemplate.postForLocation(uri, product)
    }

    fun deleteProductByID(id: Long) {
        val uri = "$hostURI/deleteByID?id=$id"
        restTemplate.delete(uri)
    }

    fun deleteProductByName(name: String) {
        val uri = "$hostURI/deleteByID?name=$name"
        restTemplate.delete(uri)
    }
}