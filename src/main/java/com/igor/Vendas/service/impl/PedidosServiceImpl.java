package com.igor.Vendas.service.impl;

import com.igor.Vendas.domain.entity.Cliente;
import com.igor.Vendas.domain.entity.ItemPedido;
import com.igor.Vendas.domain.entity.Pedido;
import com.igor.Vendas.domain.entity.Produto;
import com.igor.Vendas.domain.enums.StatusPedido;
import com.igor.Vendas.domain.repository.Clientes;
import com.igor.Vendas.domain.repository.ItemPedidos;
import com.igor.Vendas.domain.repository.Pedidos;
import com.igor.Vendas.domain.repository.Produtos;
import com.igor.Vendas.exception.PedidoNaoEncontradoException;
import com.igor.Vendas.exception.RegrasDeNegocioException;
import com.igor.Vendas.rest.dto.ItemPedidoDTO;
import com.igor.Vendas.rest.dto.PedidoDTO;
import com.igor.Vendas.service.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidosServiceImpl implements PedidosService {

    private final Pedidos reposotory;
    private final Clientes clientesRepository;
    private final ItemPedidos itemsRepository;
    private final Produtos produtosRepository;

    @Override
    @Transactional
    public Pedido salvar( PedidoDTO dto ) {
        Integer idClientes = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idClientes)
                .orElseThrow( () -> new RegrasDeNegocioException( "Codigo de cliente invalido" ));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = converterItems(pedido,dto.getItems());
        reposotory.save(pedido);
        itemsRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto( Integer id ) {
        return reposotory.findByIdFetchItens(id);
    }

    @Override
    public List<Pedido> obterTodosPedidos() {
        return reposotory.findAll();
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        reposotory.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return reposotory.save(pedido);
        }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> itens){
        if (itens.isEmpty()){
            throw new RegrasDeNegocioException("Não é possivel realizar um pedido sem items");
        }

        return itens.stream().map(dto ->{
               Integer idProduto = dto.getProduto();
               Produto produto = produtosRepository
                       .findById(idProduto)
                       .orElseThrow(() -> new RegrasDeNegocioException("Codigo de produto invalido: " + idProduto));

               ItemPedido itemPedido = new ItemPedido();
               itemPedido.setQuantidade(dto.getQuantidade());
               itemPedido.setPedido(pedido);
               itemPedido.setProduto(produto);
               return itemPedido;
        }).collect(Collectors.toList());
    }
}
