package com.igor.Vendas.domain.repository;

import com.igor.Vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Produtos extends JpaRepository<Produto, Integer > {
}
