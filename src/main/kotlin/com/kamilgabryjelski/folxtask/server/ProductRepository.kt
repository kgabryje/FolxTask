package com.kamilgabryjelski.folxtask.server

import com.kamilgabryjelski.folxtask.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository: CrudRepository<Product, Long> {
    fun findProductByName(name: String): Optional<Product>
    fun deleteProductByName(name: String)
}
