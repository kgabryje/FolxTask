package com.kamilgabryjelski.folxtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "ID not found")
class IDNotFound(message: String = ""): Exception(message)
