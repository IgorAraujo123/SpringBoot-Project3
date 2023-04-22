package com.igor.Vendas.rest.controller;

import com.igor.Vendas.domain.entity.Produto;
import com.igor.Vendas.domain.repository.Produtos;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvar( @RequestBody @Valid Produto produto ){
        return produtos.save(produto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Integer id ){
       produtos.findById(id).map(produto -> {
           produtos.delete(produto);
           return produto;
       }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado"));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Produto update( @PathVariable Integer id, @RequestBody @Valid Produto produto ){
        return produtos.findById(id).map(produtoAchado -> {
            produto.setId( produtoAchado.getId() );
            produtos.save(produto);
            return produtoAchado;
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado"));
    }

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Produto> list( Produto filter ){
        ExampleMatcher exampleMatcher = ExampleMatcher
                                            .matching()
                                            .withIgnoreCase()
                                            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filter,exampleMatcher);
        return produtos.findAll(example);
    }
}
