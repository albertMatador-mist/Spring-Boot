package com.example.assados.repository;

import com.example.assados.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    // Busca usuário pelo email (usado no login)
    Optional<Login> findByEmail(String email);

    // Verifica se email já existe (usado no cadastro)
    boolean existsByEmail(String email);
}