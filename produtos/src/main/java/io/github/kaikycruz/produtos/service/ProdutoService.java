package io.github.kaikycruz.produtos.service;

import io.github.kaikycruz.produtos.model.Produto;
import io.github.kaikycruz.produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Produto save(Produto produto) {
        return repository.save(produto);
    }

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Optional<Produto> findById(Long codigo) {
        return repository.findById(codigo);
    }
}
