package io.github.kaikycruz.pedidos.controller;

import io.github.kaikycruz.pedidos.dto.AdicaoNovoPagamentoDTO;
import io.github.kaikycruz.pedidos.dto.NovoPedidoDTO;
import io.github.kaikycruz.pedidos.mappers.PedidoMapper;
import io.github.kaikycruz.pedidos.model.ErroResposta;
import io.github.kaikycruz.pedidos.model.Pedido;
import io.github.kaikycruz.pedidos.model.exception.ItemNaoEncontradoException;
import io.github.kaikycruz.pedidos.model.exception.ValidationException;
import io.github.kaikycruz.pedidos.publisher.DetalhePedidoMapper;
import io.github.kaikycruz.pedidos.publisher.representation.DetalhePedidoRepresentation;
import io.github.kaikycruz.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;
    private final DetalhePedidoMapper detalhePedidoMapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody NovoPedidoDTO dto) {
        try {
            var pedido = mapper.mapper(dto);
            var novoPedido = service.create(pedido);
            return ResponseEntity.ok(novoPedido.getCodigo());

        } catch (ValidationException e) {
            var erro = new ErroResposta("Erro de validação", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("pagamentos")
    public ResponseEntity<Object> adicionarPagamento(@RequestBody AdicaoNovoPagamentoDTO dto) {
        try {
            service.adicionarNovoPagamento(dto.codigoPedido(), dto.dados(), dto.tipoPagamento());
            return ResponseEntity.noContent().build();
        } catch (ItemNaoEncontradoException e) {
            var erro = new ErroResposta("Item não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("{codigo}")
    public ResponseEntity<DetalhePedidoRepresentation> detalhesPedido(@PathVariable("codigo") Long codigo) {
        return service.carregarDadosPedido(codigo)
                .map(detalhePedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
