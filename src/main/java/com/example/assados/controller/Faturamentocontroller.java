package com.example.assados.controller;

import com.example.assados.service.Faturamentoservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/faturamento")
@CrossOrigin(origins = "*")
public class Faturamentocontroller {

    private final Faturamentoservice service;

    public Faturamentocontroller(Faturamentoservice service) {
        this.service = service;
    }

    // ── GET /api/faturamento ──────────────────────────────────
    // Parâmetros opcionais:
    // ?atendente=Carlos&dia=15&mes=5&ano=2026
    // ?mes=5&ano=2026
    // ?ano=2026
    @GetMapping
    public ResponseEntity<Map<String, Object>> getFaturamento(
            @RequestParam(required = false, defaultValue = "")  String atendente,
            @RequestParam(required = false, defaultValue = "0")  int dia,
            @RequestParam(required = false, defaultValue = "0")  int mes,
            @RequestParam(required = false, defaultValue = "0")  int ano) {

        // Se ano não foi informado, usa o ano atual
        if (ano == 0) ano = LocalDate.now().getYear();
        // Se mês não foi informado, usa o mês atual
        if (mes == 0) mes = LocalDate.now().getMonthValue();

        Map<String, Object> resultado = service.calcularFaturamento(atendente, dia, mes, ano);
        return ResponseEntity.ok(resultado);
    }
}