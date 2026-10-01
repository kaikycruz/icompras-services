package io.github.kaikycruz.pedidos.validator;

import feign.FeignException;
import io.github.kaikycruz.pedidos.client.ClientesClient;
import io.github.kaikycruz.pedidos.client.ProdutosClient;
import io.github.kaikycruz.pedidos.client.representation.ClienteRepresentation;
import io.github.kaikycruz.pedidos.client.representation.ProdutoRepresentation;
import io.github.kaikycruz.pedidos.model.ItemPedido;
import io.github.kaikycruz.pedidos.model.Pedido;
import io.github.kaikycruz.pedidos.model.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validate(Pedido pedido){
        validateCliente(pedido.getCodigoCliente());
        pedido.getItens().forEach(this::validateProdutos);
    }

    private void validateProdutos(ItemPedido itens) {
        try{
            ResponseEntity<ProdutoRepresentation> byId = produtosClient.findById(itens.getCodigoProduto());
            ProdutoRepresentation produto = byId.getBody();

            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome());
        }catch (FeignException.NotFound e){

            log.error("Produto de código {} não encontrado", itens.getCodigoProduto());

            var message = String.format("Produto de código %d não encontrado", itens.getCodigoProduto());
            throw new ValidationException("codigoProduto", message);
        }
    }

    private void validateCliente(Long codigoCliente) {
        try {
            ResponseEntity<ClienteRepresentation> byId = clientesClient.findById(codigoCliente);
            ClienteRepresentation cliente = byId.getBody();

            log.info("Cliente de código {} encontrado: {}", cliente.codigo(), cliente.nome());
        }catch (FeignException.NotFound e) {

            log.error("Cliente de código {} não encontrado", codigoCliente);

            var message = String.format("Cliente de código %d não encontrado", codigoCliente);
            throw new ValidationException("codigoCliente", message);
        }

    }
}
