package com.kamilgabryjelski.folxtask.constants

class HttpStatusReasonConstants {
    companion object {
        const val NOSUCHPRODUCT = "Requested product not in database"
        const val IDNOTFOUND = "ID not found"
        const val NAMEEXISTS = "Product name must be unique"
        const val IDEXISTS = "Product ID must be unique"
        const val WITHDRAWN = "Can't create a product with status: Withdrawn"
    }
}
