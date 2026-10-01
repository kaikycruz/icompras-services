package io.github.kaikycruz.clientes.service;

import io.github.kaikycruz.clientes.model.Cliente;
import io.github.kaikycruz.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Cliente save (Cliente cliente) {
        return repository.save(cliente);
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Optional<Cliente> findById (Long id) {
        return repository.findById(id);
    }
}
