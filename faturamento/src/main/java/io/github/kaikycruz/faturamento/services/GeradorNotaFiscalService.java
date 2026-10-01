package io.github.kaikycruz.faturamento.services;

import io.github.kaikycruz.faturamento.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GeradorNotaFiscalService {

    public void gerar(Pedido pedido) {
      log.info("Gerando nota fiscal para o pedido: {}", pedido.codigo());
    }
}
