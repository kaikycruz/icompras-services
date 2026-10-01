package io.github.kaikycruz.faturamento.controller;


import io.github.kaikycruz.faturamento.bucket.BucketFile;
import io.github.kaikycruz.faturamento.bucket.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {

    private final BucketService service;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try(InputStream is = file.getInputStream()) {

            MediaType type = MediaType.parseMediaType(file.getContentType());
            var bucketFile = new BucketFile(file.getOriginalFilename(), is, type, file.getSize());
            service.upload(bucketFile);

            return ResponseEntity.ok("Arquivo enviado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<String> getUrl(@RequestParam("fileName") String fileName) {
        try {
            String url = service.getUrl(fileName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao obter URL do arquivo: " + e.getMessage());
        }
    }
}
