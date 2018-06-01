package com.kamilgabryjelski.folxtask.server

import com.kamilgabryjelski.folxtask.exceptions.IDNotFound
import com.kamilgabryjelski.folxtask.exceptions.NameAlreadyExists
import com.kamilgabryjelski.folxtask.exceptions.NoSuchProduct
import com.kamilgabryjelski.folxtask.exceptions.WithdrawnProductNotAllowed
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProductController {
    @Autowired
    lateinit var productRepository: ProductRepository

    @Transactional
    @RequestMapping("/createProduct", method = [RequestMethod.PUT])
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody product: Product) =
        when {
            product.status == ProductStatus.WITHDRAWN -> throw WithdrawnProductNotAllowed()
            productRepository.findProductByName(product.name).isPresent -> throw NameAlreadyExists()
            else -> productRepository.save(product)
        }

    @Transactional
    @RequestMapping("/readAll", method = [RequestMethod.GET])
    fun readAllProducts(): List<Product> = productRepository.findAll() as List<Product>

    @Transactional
    @RequestMapping("/readByID", method = [RequestMethod.GET])
    fun readProductByID(@RequestParam(value = "id") id: Long): Product =
            productRepository.findById(id).takeIf { it.isPresent }?.get() ?: throw NoSuchProduct()

    @Transactional
    @RequestMapping("/readByName", method = [RequestMethod.GET])
    fun readProductByName(@RequestParam(value = "name") name: String): Product =
            productRepository.findProductByName(name).takeIf { it.isPresent }?.get() ?: throw NoSuchProduct()

    @Transactional
    @RequestMapping("/updateProduct", method = [RequestMethod.POST])
    fun updateProduct(@RequestBody product: Product): ResponseEntity<Unit> {
        if (!productRepository.findById(product.id).isPresent)
            throw IDNotFound()

        productRepository.findProductByName(product.name).takeUnless {
            it.isPresent && it.get().id != product.id
        } ?: throw NameAlreadyExists()

        productRepository.save(product)
        return ResponseEntity.ok().build()
    }

    @Transactional
    @RequestMapping("/deleteByID", method = [RequestMethod.DELETE])
    fun deleteProductByID(@RequestParam(value = "id") id: Long) =
            when {
                !productRepository.findById(id).isPresent -> throw NoSuchProduct()
                else -> productRepository.deleteById(id)
            }


    @Transactional
    @RequestMapping("/deleteByName", method = [RequestMethod.DELETE])
    fun deleteProductByName(@RequestParam(value = "name") name: String) =
            when {
                !productRepository.findProductByName(name).isPresent -> throw NoSuchProduct()
                else -> productRepository.deleteProductByName(name)
            }
}
