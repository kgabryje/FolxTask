package com.kamilgabryjelski.folxtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT, reason = "Product name must be unique")
class NameAlreadyExists(message: String = ""): Exception(message)
