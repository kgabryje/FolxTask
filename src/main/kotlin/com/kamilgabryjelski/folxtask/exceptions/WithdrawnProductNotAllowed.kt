package com.kamilgabryjelski.folxtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST, reason = "Can't create a product with status: Withdrawn")
class WithdrawnProductNotAllowed(message: String = ""): Exception(message)
