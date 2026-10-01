package io.github.kaikycruz.produtos.controller;

import io.github.kaikycruz.produtos.model.Produto;
import io.github.kaikycruz.produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody Produto produto) {
        service.save(produto);
        return ResponseEntity.ok(produto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{codigo}")
    public ResponseEntity<Produto> findById(@PathVariable("codigo") Long codigo) {
        return service.findById(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
