package com.igor.Vendas.rest.controller;

import com.igor.Vendas.domain.entity.ItemPedido;
import com.igor.Vendas.domain.entity.Pedido;
import com.igor.Vendas.domain.enums.StatusPedido;
import com.igor.Vendas.domain.repository.ItemPedidos;
import com.igor.Vendas.domain.repository.Pedidos;
import com.igor.Vendas.exception.RegrasDeNegocioException;
import com.igor.Vendas.rest.dto.AtualizacaoStatusPedidoDTO;
import com.igor.Vendas.rest.dto.InformacaoItemPedidoDTO;
import com.igor.Vendas.rest.dto.InformacoesPedidoDTO;
import com.igor.Vendas.rest.dto.PedidoDTO;
import com.igor.Vendas.service.PedidosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidosService service;

    public PedidoController(PedidosService service, Pedidos itemPedidos) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save( @RequestBody @Valid PedidoDTO dto ){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("/busca/{id}")
    @ResponseStatus(FOUND)
    public InformacoesPedidoDTO buscar( @PathVariable Integer id ){
        return service.obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow( () -> new RegrasDeNegocioException("Id invalido"));
    }

    @GetMapping("/busca/todos")
    @ResponseStatus(FOUND)
    public List<InformacoesPedidoDTO> listando(){
        return service.obterTodosPedidos()
                .stream()
                .map( p -> converter(p) )
                .collect(Collectors.toList());
    }

    @PatchMapping("/atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody @Valid AtualizacaoStatusPedidoDTO dto){
           String novoStatus = dto.getNovoStatus();
           service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
    }

    public InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO.builder()
                .cpf(pedido.getCliente().getCpf())
                .total(pedido.getTotal())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .nomeCliente(pedido.getCliente().getNome())
                .codigo(pedido.getId()).build();
    }

    public List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                itemPedido -> InformacaoItemPedidoDTO.builder()
                        .produtoUnitario(itemPedido.getProduto().getPreco())
                        .descricaoProduto(itemPedido.getProduto().getDescricao())
                        .quantidade(itemPedido.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
