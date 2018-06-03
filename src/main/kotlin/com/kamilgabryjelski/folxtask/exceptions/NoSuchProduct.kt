package com.kamilgabryjelski.folxtask.exceptions

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = HttpStatusReasonConstants.NOSUCHPRODUCT)
class NoSuchProduct(message: String = ""): Exception(message)
