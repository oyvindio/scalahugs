package com.oyvindio.sh.exception


case class InvalidResponseException(message: String = null,
                                    cause: Throwable = null)
  extends RuntimeException(message, cause)