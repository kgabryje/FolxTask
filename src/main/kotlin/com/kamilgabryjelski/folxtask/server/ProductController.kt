package com.kamilgabryjelski.folxtask.server

import com.kamilgabryjelski.folxtask.exceptions.IDNotSpecified
import com.kamilgabryjelski.folxtask.exceptions.NameAlreadyExists
import com.kamilgabryjelski.folxtask.exceptions.WithdrawnProductNotAllowed
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ProductController {
    @Autowired
    lateinit var productRepository: ProductRepository

    @Transactional
    @RequestMapping("/createProduct", method = [RequestMethod.PUT])
    fun createProduct(@RequestBody product: Product) {
        if (product.status == ProductStatus.WITHDRAWN)
            throw WithdrawnProductNotAllowed()
        if (productRepository.findProductByName(product.name).isPresent)
            throw NameAlreadyExists()
        productRepository.save(product)
    }

    @Transactional
    @RequestMapping("/readAll", method = [RequestMethod.GET])
    fun readAllProducts(): List<Product> {
        return productRepository.findAll() as List<Product>
    }
    @Transactional
    @RequestMapping("/readByID", method = [RequestMethod.GET])
    fun readProductByID(@RequestParam(value = "id") id: Long): Product {
        return productRepository.findById(id).get()
    }

    @Transactional
    @RequestMapping("/readByName", method = [RequestMethod.GET])
    fun readProductByName(@RequestParam(value = "name") name: String): Product {
        return productRepository.findProductByName(name).get()
    }

    @Transactional
    @RequestMapping("/updateProduct", method = [RequestMethod.POST])
    fun updateProduct(@RequestBody product: Product) {
         if (productRepository.findById(product.id).isPresent)
             productRepository.save(product)
        else
             throw IDNotSpecified()
    }

    @Transactional
    @RequestMapping("/deleteByID", method = [RequestMethod.DELETE])
    fun deleteProductByID(@RequestParam(value = "id") id: Long) {
        productRepository.deleteById(id)
    }

    @Transactional
    @RequestMapping("/deleteByName", method = [RequestMethod.DELETE])
    fun deleteProductByName(@RequestParam(value = "name") name: String) {
        productRepository.deleteProductByName(name)
    }
}
