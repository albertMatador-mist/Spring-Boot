package com.example.assados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Column(nullable = false)
    private String cliente;

    @NotBlank(message = "Nome do atendente é obrigatório")
    @Column(nullable = false)
    private String atendente;

    @NotBlank(message = "Prato é obrigatório")
    @Column(nullable = false)
    private String prato;

    @Column
    private String acompanhamento;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    @Column(nullable = false)
    private Double quantidade;

    @Column
    private String observacoes;

    @NotBlank(message = "Status é obrigatório")
    @Column(nullable = false)
    private String status;

    // Data e hora do pedido — preenchida automaticamente ao criar
    @Column(nullable = false)
    private LocalDateTime dataPedido = LocalDateTime.now();

    // ── Construtores ─────────────────────────────────────────

    public Pedido() {}

    public Pedido(String cliente, String atendente, String prato,
                  String acompanhamento, Double quantidade,
                  String observacoes, String status) {
        this.cliente        = cliente;
        this.atendente      = atendente;
        this.prato          = prato;
        this.acompanhamento = acompanhamento;
        this.quantidade     = quantidade;
        this.observacoes    = observacoes;
        this.status         = status;
        this.dataPedido     = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getAtendente() { return atendente; }
    public void setAtendente(String atendente) { this.atendente = atendente; }

    public String getPrato() { return prato; }
    public void setPrato(String prato) { this.prato = prato; }

    public String getAcompanhamento() { return acompanhamento; }
    public void setAcompanhamento(String acompanhamento) { this.acompanhamento = acompanhamento; }

    public Double getQuantidade() { return quantidade; }
    public void setQuantidade(Double quantidade) { this.quantidade = quantidade; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", cliente='" + cliente + "', prato='" + prato + "', status='" + status + "', data='" + dataPedido + "'}";
    }
}