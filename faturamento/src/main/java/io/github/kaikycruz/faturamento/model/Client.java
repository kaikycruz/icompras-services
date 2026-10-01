package io.github.kaikycruz.faturamento.model;

public record Client(
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String bairro,
        String email,
        String telefone
) {
}
