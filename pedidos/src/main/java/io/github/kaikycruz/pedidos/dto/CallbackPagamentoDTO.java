package io.github.kaikycruz.pedidos.dto;

public record CallbackPagamentoDTO(
        Long codigo,
        String chavePagamento,
        boolean status,
        String observacoes) {
}
