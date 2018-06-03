package com.kamilgabryjelski.folxtask.server

import com.kamilgabryjelski.folxtask.constants.UriConstants
import com.kamilgabryjelski.folxtask.exceptions.*
import com.kamilgabryjelski.folxtask.model.Product
import com.kamilgabryjelski.folxtask.model.ProductStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class ProductController {
    @Autowired
    lateinit var productService: ProductService

    @Transactional
    @RequestMapping(UriConstants.CREATE, method = [RequestMethod.PUT])
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody product: Product) {
        when {
            product.status == ProductStatus.WITHDRAWN -> throw WithdrawnProductNotAllowed()
            productService.findByID(product.id).isPresent -> throw IDAlreadyExists()
            productService.findByName(product.name).isPresent -> throw NameAlreadyExists()
            else -> productService.createOrUpdate(product)
        }
    }

    @Transactional
    @RequestMapping(UriConstants.READALL, method = [RequestMethod.GET])
    fun readAllProducts(): List<Product> = productService.findAll() as List<Product>

    @Transactional
    @RequestMapping(UriConstants.READBYID, method = [RequestMethod.GET])
    fun readProductByID(@RequestParam(value = "id") id: Long): Product =
            productService.findByID(id).takeIf { it.isPresent }?.get() ?: throw NoSuchProduct()

    @Transactional
    @RequestMapping(UriConstants.READBYNAME, method = [RequestMethod.GET])
    fun readProductByName(@RequestParam(value = "name") name: String): Product =
            productService.findByName(name).takeIf { it.isPresent }?.get() ?: throw NoSuchProduct()

    @Transactional
    @RequestMapping(UriConstants.UPDATE, method = [RequestMethod.POST])
    fun updateProduct(@RequestBody product: Product) {
        if (!productService.findByID(product.id).isPresent)
            throw IDNotFound()

        productService.findByName(product.name).takeUnless {
            it.isPresent && it.get().id != product.id
        } ?: throw NameAlreadyExists()

        productService.createOrUpdate(product)
    }

    @Transactional
    @RequestMapping(UriConstants.DELETEBYID, method = [RequestMethod.DELETE])
    fun deleteProductByID(@RequestParam(value = "id") id: Long) =
            when {
                !productService.findByID(id).isPresent -> throw NoSuchProduct()
                else -> productService.deleteByID(id)
            }


    @Transactional
    @RequestMapping(UriConstants.DELETEBYNAME, method = [RequestMethod.DELETE])
    fun deleteProductByName(@RequestParam(value = "name") name: String) =
            when {
                !productService.findByName(name).isPresent -> throw NoSuchProduct()
                else -> productService.deleteByName(name)
            }
}
