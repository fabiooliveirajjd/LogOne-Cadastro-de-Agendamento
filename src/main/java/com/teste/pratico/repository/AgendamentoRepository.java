package com.teste.pratico.repository;

import com.teste.pratico.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Busca agendamentos entre a data de início e fim
    List<Agendamento> findByDataBetween(Date dataInicio, Date dataFim);

    // Busca agendamentos entre a data de início e fim para um solicitante específico
    List<Agendamento> findByDataBetweenAndSolicitanteId(Date dataInicio, Date dataFim, Long solicitanteId);

    // Consulta a quantidade de agendamentos por solicitante e período usando JPQL
    @Query("SELECT a.solicitante.id, COUNT(a) FROM Agendamento a WHERE a.data BETWEEN :dataInicio AND :dataFim GROUP BY a.solicitante.id")
    List<Object[]> countAgendamentosBySolicitanteAndDateRange(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

    // Consulta a quantidade de agendamentos por solicitante e período usando JPQL
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.data BETWEEN :dataInicio AND :dataFim AND a.solicitante.id = :solicitanteId")
    Long countAgendamentosBySolicitante(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("solicitanteId") Long solicitanteId);

}