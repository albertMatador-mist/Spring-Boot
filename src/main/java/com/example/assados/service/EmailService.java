package com.example.assados.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarNovaSenha(String destinatario, String nome, String novaSenha) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom("assadosmelhor@gmail.com");
        mensagem.setTo(destinatario);
        mensagem.setSubject("Assados 2 Rodas — Redefinição de Senha");
        mensagem.setText(
                "Olá, " + nome + "!\n\n" +
                        "Recebemos uma solicitação de redefinição de senha para sua conta.\n\n" +
                        "Sua nova senha temporária é: " + novaSenha + "\n\n" +
                        "Por segurança, altere sua senha após o login.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe Assados 2 Rodas 🔥"
        );
        mailSender.send(mensagem);
    }
}