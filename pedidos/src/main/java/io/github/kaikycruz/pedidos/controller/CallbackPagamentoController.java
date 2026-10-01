package io.github.kaikycruz.pedidos.controller;

import io.github.kaikycruz.pedidos.dto.CallbackPagamentoDTO;
import io.github.kaikycruz.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/callback-pagamentos")
@RequiredArgsConstructor
public class CallbackPagamentoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<Object> atualizarStatusPagamento(
            @RequestBody CallbackPagamentoDTO body,
            @RequestHeader(required = true, name = "apiKey") String apiKey) {

        service.atualizarStatusPagamento(
                body.codigo(),
                body.chavePagamento(),
                body.status(),
                body.observacoes());

        return ResponseEntity.ok().build();
    }
}
