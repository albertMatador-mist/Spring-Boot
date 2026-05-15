package com.example.assados.repository;

import com.example.assados.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatus(String status);

    List<Pedido> findByClienteContainingIgnoreCase(String cliente);

    List<Pedido> findByAtendenteContainingIgnoreCase(String atendente);

    List<Pedido> findByPratoContainingIgnoreCase(String prato);

    // Busca combinada com filtro de texto e status
    @Query("""
        SELECT p FROM Pedido p
        WHERE (:texto IS NULL OR :texto = ''
               OR LOWER(p.cliente)   LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(p.atendente) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(p.prato)     LIKE LOWER(CONCAT('%', :texto, '%')))
          AND (:status IS NULL OR :status = '' OR p.status = :status)
        ORDER BY p.id DESC
    """)
    List<Pedido> buscarFiltrado(@Param("texto") String texto,
                                @Param("status") String status);

    // ── FATURAMENTO ───────────────────────────────────────────

    // Pedidos retirados em um período
    List<Pedido> findByStatusAndDataPedidoBetween(String status,
                                                  LocalDateTime inicio,
                                                  LocalDateTime fim);

    // Pedidos retirados por atendente em um período
    List<Pedido> findByStatusAndAtendenteAndDataPedidoBetween(String status,
                                                              String atendente,
                                                              LocalDateTime inicio,
                                                              LocalDateTime fim);

    // Lista todos os atendentes distintos
    @Query("SELECT DISTINCT p.atendente FROM Pedido p ORDER BY p.atendente")
    List<String> findAtendentesDistintos();
}