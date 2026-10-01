package io.github.kaikycruz.clientes.repository;

import io.github.kaikycruz.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
