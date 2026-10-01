package io.github.kaikycruz.pedidos.repository;

import io.github.kaikycruz.pedidos.model.ItemPedido;
import io.github.kaikycruz.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}
