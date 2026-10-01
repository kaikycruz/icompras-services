package io.github.kaikycruz.pedidos.service;

import io.github.kaikycruz.pedidos.client.ClientesClient;
import io.github.kaikycruz.pedidos.client.ProdutosClient;
import io.github.kaikycruz.pedidos.client.ServicoBancarioClient;
import io.github.kaikycruz.pedidos.client.representation.ClienteRepresentation;
import io.github.kaikycruz.pedidos.client.representation.ProdutoRepresentation;
import io.github.kaikycruz.pedidos.model.DadosPagamento;
import io.github.kaikycruz.pedidos.model.ItemPedido;
import io.github.kaikycruz.pedidos.model.Pedido;
import io.github.kaikycruz.pedidos.model.enums.StatusPedido;
import io.github.kaikycruz.pedidos.model.enums.TipoPagamento;
import io.github.kaikycruz.pedidos.model.exception.ItemNaoEncontradoException;
import io.github.kaikycruz.pedidos.publisher.PagamentoPublisher;
import io.github.kaikycruz.pedidos.repository.ItemPedidoRepository;
import io.github.kaikycruz.pedidos.repository.PedidoRepository;
import io.github.kaikycruz.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient apiClientes;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher publisher;


    @Transactional
    public Pedido create(Pedido pedido){
        validator.validate(pedido);

        save(pedido);
        sendPaymentRequest(pedido);

        return pedido;
    }

    private void sendPaymentRequest(Pedido pedido) {
        String chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void save(Pedido pedido) {
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    public void atualizarStatusPagamento(
            Long codigoPedido, String chavePagamento, boolean status, String observacoes) {

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if (pedidoEncontrado.isEmpty()) {
            var mensagem = String.format("Pedido de código %d não encontrado ou chave de pagamento inválida", codigoPedido);
            log.error(mensagem);
        }

        Pedido pedido = pedidoEncontrado.get();
        if (status) {
            publicarPedidoPago(pedido);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }
        pedidoRepository.save(pedido);
    }

    private void publicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarItensPedido(pedido);
        publisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento (Long codigoPedido, String dadosCartao, TipoPagamento tipo) {
        Optional<Pedido> byId = pedidoRepository.findById(codigoPedido);
        if (byId.isEmpty()) {
         throw new ItemNaoEncontradoException("Pedido de código " + codigoPedido + " não encontrado");
        }

        Pedido pedido = byId.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setDados(dadosCartao);
        dadosPagamento.setTipoPagamento(tipo);

        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando processamento");

        String novaChavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);

        pedidoRepository.save(pedido);
    }


    public Optional<Pedido> carregarDadosPedido(Long codigoPedido) {
        Optional<Pedido> byId = pedidoRepository.findById(codigoPedido);
        byId.ifPresent(this::carregarDadosCliente);
        byId.ifPresent(this::carregarItensPedido);
        return byId;
    }

    private void carregarDadosCliente(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        ResponseEntity<ClienteRepresentation> byId = apiClientes.findById(codigoCliente);
        pedido.setDadosCliente(byId.getBody());
    }

    private void carregarItensPedido(Pedido pedido) {
       List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
       pedido.setItens(itens);
       pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido item) {
        Long codigoProduto = item.getCodigoProduto();
        ResponseEntity<ProdutoRepresentation> byId = apiProdutos.findById(codigoProduto);
        item.setNome(byId.getBody().nome());
    }

}
