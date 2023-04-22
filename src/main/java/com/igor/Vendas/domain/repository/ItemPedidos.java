package com.igor.Vendas.domain.repository;

import com.igor.Vendas.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidos extends JpaRepository <ItemPedido , Integer > {
}
