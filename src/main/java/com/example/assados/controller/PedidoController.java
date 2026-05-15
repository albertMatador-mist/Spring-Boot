package com.example.assados.controller;

import com.example.assados.model.Pedido;
import com.example.assados.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // GET /api/pedidos?texto=joao&status=Pronto
    @GetMapping
    public ResponseEntity<List<Pedido>> listar(
            @RequestParam(required = false, defaultValue = "") String texto,
            @RequestParam(required = false, defaultValue = "") String status) {

        return ResponseEntity.ok(service.listarFiltrado(texto, status));
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /api/pedidos
    @PostMapping
    public ResponseEntity<Pedido> inserir(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.inserir(pedido));
    }

    // PUT /api/pedidos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Pedido pedido) {

        return ResponseEntity.ok(service.atualizar(id, pedido));
    }

    // PATCH /api/pedidos/{id}/status  —  body: { "status": "Pronto" }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String novoStatus = body.get("status");
        if (novoStatus == null || novoStatus.isBlank())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(service.atualizarStatus(id, novoStatus));
    }

    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", ex.getMessage()));
    }
}
