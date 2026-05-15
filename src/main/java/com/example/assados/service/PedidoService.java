package com.example.assados.service;

import com.example.assados.model.Pedido;
import com.example.assados.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public List<Pedido> listarFiltrado(String texto, String status) {
        return repository.buscarFiltrado(texto, status);
    }

    public Pedido buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado. ID: " + id));
    }

    public Pedido inserir(Pedido pedido) {
        validar(pedido);
        pedido.setId(null);
        if (pedido.getStatus() == null || pedido.getStatus().isBlank()) {
            pedido.setStatus("Aguardando");
        }
        return repository.save(pedido);
    }

    public Pedido atualizar(Long id, Pedido dadosNovos) {
        Pedido existente = buscarPorId(id);
        validar(dadosNovos);

        existente.setCliente(dadosNovos.getCliente());
        existente.setAtendente(dadosNovos.getAtendente());
        existente.setPrato(dadosNovos.getPrato());
        existente.setAcompanhamento(dadosNovos.getAcompanhamento());
        existente.setQuantidade(dadosNovos.getQuantidade());
        existente.setObservacoes(dadosNovos.getObservacoes());
        existente.setStatus(dadosNovos.getStatus());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    public Pedido atualizarStatus(Long id, String novoStatus) {
        validarStatus(novoStatus);
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return repository.save(pedido);
    }

    private void validar(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().isBlank())
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        if (pedido.getAtendente() == null || pedido.getAtendente().isBlank())
            throw new IllegalArgumentException("Nome do atendente é obrigatório.");
        if (pedido.getPrato() == null || pedido.getPrato().isBlank())
            throw new IllegalArgumentException("Prato é obrigatório.");
        if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0)
            throw new IllegalArgumentException("Quantidade em kg deve ser maior que zero.");
        if (pedido.getStatus() != null && !pedido.getStatus().isBlank())
            validarStatus(pedido.getStatus());
    }

    private void validarStatus(String status) {
        List<String> statusValidos = List.of("Aguardando", "Preparando", "Pronto", "Retirado");
        if (!statusValidos.contains(status))
            throw new IllegalArgumentException(
                "Status inválido: '" + status + "'. Use: " + statusValidos
            );
    }
}
