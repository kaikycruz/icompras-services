package io.github.kaikycruz.pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "io.github.kaikycruz.pedidos.client")
public class ClientsConfig {

}
