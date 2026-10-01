package io.github.kaikycruz.pedidos.dto;

import io.github.kaikycruz.pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(
        Long codigoPedido,
        String dados,
        TipoPagamento tipoPagamento
) {
}
