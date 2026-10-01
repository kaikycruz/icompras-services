package io.github.kaikycruz.produtos.repository;

import io.github.kaikycruz.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
