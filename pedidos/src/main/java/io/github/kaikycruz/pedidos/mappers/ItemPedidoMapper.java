package io.github.kaikycruz.pedidos.mappers;

import io.github.kaikycruz.pedidos.dto.ItemPedidoDTO;
import io.github.kaikycruz.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    ItemPedido mapper(ItemPedidoDTO dto);
}
