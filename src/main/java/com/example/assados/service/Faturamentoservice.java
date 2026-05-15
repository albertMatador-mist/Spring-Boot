package com.example.assados.service;

import com.example.assados.model.Pedido;
import com.example.assados.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class Faturamentoservice {

    private final PedidoRepository repository;

    public Faturamentoservice(PedidoRepository repository) {
        this.repository = repository;
    }

    // ── PREÇOS ────────────────────────────────────────────────
    private static final Map<String, Double> PRECO_PRATO = Map.of(
            "Frango Assado",         25.0,
            "Carne Assada (Bovina)", 45.0,
            "Carne Suína Assada",    35.0
    );

    private static final Map<String, Double> PRECO_ACOMP = Map.of(
            "Salada de Batata", 10.0,
            "Maionese",          5.0,
            "Nenhum",            0.0
    );

    // ── CALCULAR VALOR DE UM PEDIDO ───────────────────────────
    public double calcularValor(Pedido p) {
        double precoPrato = PRECO_PRATO.getOrDefault(p.getPrato(), 0.0);
        double precoAcomp = PRECO_ACOMP.getOrDefault(p.getAcompanhamento(), 0.0);
        return (precoPrato * p.getQuantidade()) + precoAcomp;
    }

    // ── FATURAMENTO FILTRADO ──────────────────────────────────
    public Map<String, Object> calcularFaturamento(String atendente, int dia, int mes, int ano) {

        // Define o período de busca
        LocalDateTime inicio;
        LocalDateTime fim;

        if (dia > 0) {
            // Filtro por dia específico
            inicio = LocalDateTime.of(ano, mes, dia, 0, 0, 0);
            fim    = LocalDateTime.of(ano, mes, dia, 23, 59, 59);
        } else if (mes > 0) {
            // Filtro por mês inteiro
            inicio = LocalDateTime.of(ano, mes, 1, 0, 0, 0);
            fim    = inicio.plusMonths(1).minusSeconds(1);
        } else {
            // Filtro por ano inteiro
            inicio = LocalDateTime.of(ano, 1, 1, 0, 0, 0);
            fim    = LocalDateTime.of(ano, 12, 31, 23, 59, 59);
        }

        // Busca pedidos retirados no período
        List<Pedido> pedidos;
        if (atendente != null && !atendente.isBlank()) {
            pedidos = repository.findByStatusAndAtendenteAndDataPedidoBetween(
                    "Retirado", atendente, inicio, fim);
        } else {
            pedidos = repository.findByStatusAndDataPedidoBetween("Retirado", inicio, fim);
        }

        // Calcula total geral
        double totalGeral = pedidos.stream().mapToDouble(this::calcularValor).sum();

        // Agrupa por atendente
        Map<String, Double> porAtendente = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            double valor = calcularValor(p);
            porAtendente.merge(p.getAtendente(), valor, Double::sum);
        }

        // Monta lista de pedidos com valor calculado
        List<Map<String, Object>> listaPedidos = new ArrayList<>();
        for (Pedido p : pedidos) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id",             p.getId());
            item.put("cliente",        p.getCliente());
            item.put("atendente",      p.getAtendente());
            item.put("prato",          p.getPrato());
            item.put("acompanhamento", p.getAcompanhamento());
            item.put("quantidade",     p.getQuantidade());
            item.put("observacoes",    p.getObservacoes());
            item.put("status",         p.getStatus());
            item.put("dataPedido",     p.getDataPedido().toString());
            item.put("valor",          calcularValor(p));
            listaPedidos.add(item);
        }

        // Monta resposta
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("totalGeral",   totalGeral);
        resultado.put("porAtendente", porAtendente);
        resultado.put("pedidos",      listaPedidos);
        resultado.put("atendentes",   repository.findAtendentesDistintos());

        return resultado;
    }
}