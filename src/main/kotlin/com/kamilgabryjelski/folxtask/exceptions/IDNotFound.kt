package com.kamilgabryjelski.folxtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST, reason = "ID must be specified")
class IDNotFound(message: String = ""): Exception(message)
