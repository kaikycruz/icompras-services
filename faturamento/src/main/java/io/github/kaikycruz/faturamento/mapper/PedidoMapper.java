package io.github.kaikycruz.faturamento.mapper;

import io.github.kaikycruz.faturamento.model.Client;
import io.github.kaikycruz.faturamento.model.ItemPedido;
import io.github.kaikycruz.faturamento.model.Pedido;
import io.github.kaikycruz.faturamento.subscriber.representation.DetalheItemPedidoRepresentation;
import io.github.kaikycruz.faturamento.subscriber.representation.DetalhePedidoRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public Pedido map (DetalhePedidoRepresentation representation) {
        Client cliente = new Client(
                representation.nome(),
                representation.cpf(),
                representation.logradouro(),
                representation.numero(),
                representation.bairro(),
                representation.email(),
                representation.telefone()
        );

        List<ItemPedido> itens = representation.itens().stream()
                .map(this::mapItem).toList();

        return new Pedido(
                representation.codigo(),
                cliente,
                representation.dataPedido(),
                representation.total(),
                itens
        );
    }


    private ItemPedido mapItem(DetalheItemPedidoRepresentation representation) {
        return new ItemPedido(
                representation.codigoProduto(),
                representation.nome(),
                representation.valorUnitario(),
                representation.quantidade(),
                representation.total()
        );
    }

}
