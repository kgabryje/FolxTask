package com.kamilgabryjelski.folxtask.server

import com.kamilgabryjelski.folxtask.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {
    @Autowired
    lateinit var productRepository: ProductRepository

    fun createOrUpdate(product: Product) = productRepository.save(product)

    fun findAll() = productRepository.findAll() as List<Product>
    fun findByID(id: Long) = productRepository.findById(id)
    fun findByName(name: String) = productRepository.findProductByName(name)

    fun deleteByID(id: Long) = productRepository.deleteById(id)
    fun deleteByName(name: String) = productRepository.deleteProductByName(name)
}
