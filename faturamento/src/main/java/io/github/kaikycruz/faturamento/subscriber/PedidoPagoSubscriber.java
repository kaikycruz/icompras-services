package io.github.kaikycruz.faturamento.subscriber;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kaikycruz.faturamento.mapper.PedidoMapper;
import io.github.kaikycruz.faturamento.model.Pedido;
import io.github.kaikycruz.faturamento.services.GeradorNotaFiscalService;
import io.github.kaikycruz.faturamento.subscriber.representation.DetalhePedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PedidoPagoSubscriber {

    private final ObjectMapper mapper;
    private final GeradorNotaFiscalService geradorNotaFiscalService;
    private final PedidoMapper pedidoMapper;


    @KafkaListener(groupId = "icompras-faturamento", topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listener(String json) {
        try {
            log.info("Recebendo pedido para faturamento: {}", json);
            var representation = mapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            geradorNotaFiscalService.gerar(pedido);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem do tópico de pedidos pagos");
            throw new RuntimeException(e);
        }
    }


}
