package com.igor.Vendas.rest.controller;

import com.igor.Vendas.exception.PedidoNaoEncontradoException;
import com.igor.Vendas.exception.RegrasDeNegocioException;
import com.igor.Vendas.rest.ApiErrors;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegrasDeNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegrasDeNegociosException(RegrasDeNegocioException ex){
        String messagemError = ex.getMessage();
        return new ApiErrors(messagemError);
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> stringStream = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        return new ApiErrors(stringStream);
    }

}
