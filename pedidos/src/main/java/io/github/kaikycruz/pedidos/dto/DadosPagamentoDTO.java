package io.github.kaikycruz.pedidos.dto;

import io.github.kaikycruz.pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(
        String dados,
        TipoPagamento tipoPagamento
) {
}
