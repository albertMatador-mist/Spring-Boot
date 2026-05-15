package com.example.assados.controller;

import com.example.assados.model.Login;
import com.example.assados.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    // ── POST /api/auth/cadastrar ──────────────────────────────
    // Body: { "nome": "Carlos", "email": "carlos@email.com",
    //         "senha": "123456", "perfil": "GARCOM" }
    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, Object>> cadastrar(@Valid @RequestBody Login usuario) {
        Login novo = service.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id",      novo.getId(),
                "nome",    novo.getNome(),
                "email",   novo.getEmail(),
                "perfil",  novo.getPerfil(),
                "mensagem", "Usuário cadastrado com sucesso!"
        ));
    }

    // ── POST /api/auth/login ──────────────────────────────────
    // Body: { "email": "carlos@email.com", "senha": "123456" }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Email e senha são obrigatórios"));
        }

        Login usuario = service.autenticar(email, senha);
        return ResponseEntity.ok(Map.of(
                "id",      usuario.getId(),
                "nome",    usuario.getNome(),
                "email",   usuario.getEmail(),
                "perfil",  usuario.getPerfil(),
                "mensagem", "Login realizado com sucesso!"
        ));
    }

    // ── GET /api/auth/usuarios ────────────────────────────────
    // Lista todos os usuários (só ADMIN deveria acessar)
    @GetMapping("/usuarios")
    public ResponseEntity<List<Login>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // ── DELETE /api/auth/usuarios/{id} ────────────────────────
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ── TRATAMENTO DE ERROS ───────────────────────────────────
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", ex.getMessage()));
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> esqueceuSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Informe o email!"));
        }
        service.esqueceuSenha(email);
        return ResponseEntity.ok(Map.of("mensagem", "Nova senha enviada para o email!"));
    }

}