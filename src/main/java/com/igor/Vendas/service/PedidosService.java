package com.igor.Vendas.service;

import com.igor.Vendas.domain.entity.ItemPedido;
import com.igor.Vendas.domain.entity.Pedido;
import com.igor.Vendas.domain.enums.StatusPedido;
import com.igor.Vendas.rest.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidosService {
    Pedido salvar( PedidoDTO dto );
    Optional<Pedido> obterPedidoCompleto( Integer id );

    List<Pedido> obterTodosPedidos();

    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
