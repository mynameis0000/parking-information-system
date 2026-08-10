package com.example.battery.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String
)

@RestControllerAdvice
class GlobalExceptionHandler {

    // 잘못된 인자 전달 시 (예: 존재하지 않는 거치대 ID) -> 400 Bad Request
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = e.message ?: "잘못된 요청입니다."
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // 상태 이상 발생 시 (예: 재고 부족, 빈 슬롯 없음) -> 409 Conflict
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = e.message ?: "처리할 수 없는 상태입니다."
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }
}