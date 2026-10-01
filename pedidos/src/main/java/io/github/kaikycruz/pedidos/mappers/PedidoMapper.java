package io.github.kaikycruz.pedidos.mappers;

import io.github.kaikycruz.pedidos.dto.ItemPedidoDTO;
import io.github.kaikycruz.pedidos.dto.NovoPedidoDTO;
import io.github.kaikycruz.pedidos.model.ItemPedido;
import io.github.kaikycruz.pedidos.model.Pedido;
import io.github.kaikycruz.pedidos.model.enums.StatusPedido;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    ItemPedidoMapper ITEM_PEDIDO_MAPPER = Mappers.getMapper(ItemPedidoMapper.class);

    @Mapping(source = "itens", target = "itens", qualifiedByName = "mapItens")
    @Mapping(source = "dadosPagamento", target = "dadosPagamento")
    Pedido mapper(NovoPedidoDTO novoPedidoDTO);

    @Named("mapItens")
    default List<ItemPedido> mapItens (List<ItemPedidoDTO> dtos) {
        return dtos
                .stream()
                .map(ITEM_PEDIDO_MAPPER::mapper)
                .toList();
    }

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido) {
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(LocalDateTime.now());

        var total = calcularTotal(pedido);
        pedido.setTotal(total);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
    }

    private static BigDecimal calcularTotal(Pedido pedido) {
        return pedido.getItens()
                .stream()
                .map(item -> item.getValorUnitario()
                        .multiply(BigDecimal
                                .valueOf(item.getQuantidade()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add).abs();
    }
}
