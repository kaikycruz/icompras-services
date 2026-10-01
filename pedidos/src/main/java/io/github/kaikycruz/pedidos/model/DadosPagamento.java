package io.github.kaikycruz.pedidos.model;

import io.github.kaikycruz.pedidos.model.enums.TipoPagamento;
import lombok.Data;

@Data
public class DadosPagamento {
    private String dados;
    private TipoPagamento tipoPagamento;
}
