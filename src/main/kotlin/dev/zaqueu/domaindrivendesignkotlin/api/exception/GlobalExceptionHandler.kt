package dev.zaqueu.domaindrivendesignkotlin.api.exception

import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.BadRequestException
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.InternalServerErrorException
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.InvalidArgumentException
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

data class ErrorResponse(
    val status: Int,
    val message: String?
)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidArgumentException::class)
    fun handleInvalidArgumentException(ex: InvalidArgumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.message)
        return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalErrorException(
        ex: InternalServerErrorException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
