package com.teste.pratico.repository;

import com.teste.pratico.entity.Vagas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VagasRepository extends JpaRepository<Vagas, Long> {

    // Consulta vagas por data de inicio
    List<Vagas> findByInicio(Date inicio);

    // Consulta dinamica para listar vagas por data de inicio e fim usando JPQL
    @Query("SELECT SUM(v.quantidade) FROM Vagas v WHERE v.inicio <= :dataFim AND v.fim >= :dataInicio")
    Integer sumVagasByDateRange(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);
}