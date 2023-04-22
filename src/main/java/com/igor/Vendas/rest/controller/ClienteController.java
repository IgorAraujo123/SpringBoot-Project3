package com.igor.Vendas.rest.controller;

import com.igor.Vendas.domain.entity.Cliente;
import com.igor.Vendas.domain.repository.Clientes;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes/")
public class ClienteController {

    private final Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }
    @GetMapping( "buscarId/{id}" )
    public Cliente getClienteById(@PathVariable Integer id ){
        return clientes
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save( @RequestBody @Valid Cliente cliente ){
        return clientes.save(cliente);
    }

    @DeleteMapping( "delete/{id}" )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Integer id ){
        clientes.findById(id)
                .map(cliente ->{
                    clientes.delete(cliente);
                    return cliente;
                }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @PutMapping( "update/{id}" )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Cliente update( @PathVariable Integer id, @RequestBody @Valid Cliente cliente ){
        return clientes
                .findById(id)
                .map( ClientesExixtentes ->{
                    cliente.setId( ClientesExixtentes.getId() );
                    clientes.save(cliente);
                    return cliente;
                }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado" ));
    }

    @GetMapping("buscar")
    public List<Cliente> buscarCliente(Cliente filtro ){
        ExampleMatcher exampleMatcher = ExampleMatcher
                                            .matching()
                                            .withIgnoreCase()
                                            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro,exampleMatcher);
        return clientes.findAll(example);
    }

}
