package com.kamilgabryjelski.folxtask.exceptions

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST, reason = HttpStatusReasonConstants.IDEXISTS)
class IDAlreadyExists(message: String = ""): Exception(message)
