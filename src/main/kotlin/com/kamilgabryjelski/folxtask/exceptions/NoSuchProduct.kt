package com.kamilgabryjelski.folxtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "Requested product not in database")
class NoSuchProduct(message: String = ""): Exception(message)
