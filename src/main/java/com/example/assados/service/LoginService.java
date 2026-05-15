package com.example.assados.service;

import com.example.assados.model.Login;
import com.example.assados.repository.LoginRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    private final EmailService emailService;
    private final LoginRepository repository;
    private final BCryptPasswordEncoder encoder;

    public LoginService(EmailService emailService, LoginRepository repository, BCryptPasswordEncoder encoder) {
        this.emailService = emailService;
        this.repository = repository;
        this.encoder = encoder;
    }

    // ── CADASTRAR usuário com senha criptografada ───0──────────
    public Login cadastrar(Login usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + usuario.getEmail());
        }
        if (usuario.getPerfil() == null || usuario.getPerfil().isBlank()) {
            usuario.setPerfil("GARCOM");
        }
        // Criptografa a senha com BCrypt antes de salvar
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    // ── LOGIN — valida email e senha ──────────────────────────
    public Login autenticar(String email, String senhaDigitada) {
        Login usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Compara senha digitada com o hash salvo no banco
        if (!encoder.matches(senhaDigitada, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        return usuario;
    }

    // ── LISTAR todos os usuários ──────────────────────────────
    public List<Login> listar() {
        return repository.findAll();
    }

    // ── BUSCAR por ID ─────────────────────────────────────────
    public Login buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado. ID: " + id));
    }

    // ── DELETAR ───────────────────────────────────────────────
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    // ── ADICIONA no LoginService.java ────────────────────────────
// 1. Injeta o EmailService no construtor:

// private final EmailService emailService;
//
// public LoginService(LoginRepository repository,
//                     BCryptPasswordEncoder encoder,
//                     EmailService emailService) {
//     this.repository   = repository;
//     this.encoder      = encoder;
//     this.emailService = emailService;
// }

// 2. Adiciona este método no final da classe:

    public void esqueceuSenha(String email) {
        Login usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado."));

        // Gera senha temporária de 8 caracteres
        String novaSenha = gerarSenhaAleatoria();

        // Salva a nova senha criptografada no banco
        usuario.setSenha(encoder.encode(novaSenha));
        repository.save(usuario);

        // Envia o email com a senha temporária
        emailService.enviarNovaSenha(email, usuario.getNome(), novaSenha);
    }

    private String gerarSenhaAleatoria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}