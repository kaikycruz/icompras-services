package io.github.kaikycruz.faturamento.model;

import java.math.BigDecimal;
import java.util.List;

public record Pedido(
        Long codigo,
        Client cliente,
        String data,
        BigDecimal total,
        List<ItemPedido> itens
) {
}
