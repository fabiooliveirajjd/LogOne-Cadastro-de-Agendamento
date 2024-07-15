package com.teste.pratico.service;

import com.teste.pratico.entity.Agendamento;
import com.teste.pratico.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    //Salvar agendamento
    public void save(Agendamento agendamento) {
        agendamentoRepository.save(agendamento);
    }

    //Listar agendamentos
    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }

    //Buscar agendamento por periodo
    public List<Agendamento> findByDateRange(Date dataInicio, Date dataFim) {
        return agendamentoRepository.findByDataBetween(dataInicio, dataFim);
    }

    //Listar agendamento por periodo e solicitante
    public List<Agendamento> findByDateRangeAndSolicitante(Date dataInicio, Date dataFim, Long solicitanteId) {
        return agendamentoRepository.findByDataBetweenAndSolicitanteId(dataInicio, dataFim, solicitanteId);
    }

    // Calcula a quantidade de agendamentos por solicitante e periodo
    public List<Object[]> countAgendamentosBySolicitanteAndDateRange(Date dataInicio, Date dataFim) {
        return agendamentoRepository.countAgendamentosBySolicitanteAndDateRange(dataInicio, dataFim);
    }

    // Calcular quantidade de agendamentos por solicitante
    public Long countAgendamentosBySolicitante(Date dataInicio, Date dataFim, Long solicitanteId) {
        return agendamentoRepository.countAgendamentosBySolicitante(dataInicio, dataFim, solicitanteId);
    }

}
