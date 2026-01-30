package com.authenticator.authenticator_api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserGlobalExceptionHandler {
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(e: BadRequestException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message ?: "Erro ao receber informações")
        problem.title = "Erro ao receber informações"
        return problem
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: NotFoundException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "Não encontrado")
        problem.title = "Recurso não encontrado"
        return problem
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.message ?: "Não autorizado")
        problem.title = "Não autorizado"
        return problem
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(e: ForbiddenException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message ?: "Sem permissão")
        problem.title = "Sem permissão"
        return problem
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: ConflictException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message ?: "Conflito")
        problem.title = "Conflito"
        return problem
    }
}