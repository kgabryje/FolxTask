package com.kamilgabryjelski.folxtask.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Product(
        @Id @GeneratedValue val id: Long = -1,
        @Column(unique = true, nullable = false) val name: String = "",
        @Column var price: Float = 0F,
        @Column var status: ProductStatus = ProductStatus.WITHDRAWN
)
