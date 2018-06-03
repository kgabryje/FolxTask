package com.kamilgabryjelski.folxtask.exceptions

import com.kamilgabryjelski.folxtask.constants.HttpStatusReasonConstants
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT, reason = HttpStatusReasonConstants.NAMEEXISTS)
class NameAlreadyExists(message: String = ""): Exception(message)
