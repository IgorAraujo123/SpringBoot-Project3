package com.igor.Vendas.rest;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private List<String> erros;

    public ApiErrors(List<String> erros) {
        this.erros = erros;
    }

    public ApiErrors(String messageError) {
        this.erros = Arrays.asList(messageError);
    }
}
