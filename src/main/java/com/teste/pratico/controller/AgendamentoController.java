package com.teste.pratico.controller;

import com.teste.pratico.DTO.AgendamentoDTO;
import com.teste.pratico.entity.Agendamento;
import com.teste.pratico.entity.Solicitante;
import com.teste.pratico.entity.Vagas;
import com.teste.pratico.service.AgendamentoService;
import com.teste.pratico.service.SolicitanteService;
import com.teste.pratico.service.VagasService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Controller
@SessionScope
@ManagedBean
@RequiredArgsConstructor
public class AgendamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private final AgendamentoService agendamentoService;

    @Autowired
    private final SolicitanteService solicitanteService;

    @Autowired
    private final VagasService vagasService;

    private Solicitante solicitante;
    private Agendamento agendamento;
    private List<Solicitante> solicitantes;
    private List<Agendamento> agendamentoList;
    private List<Integer> numeros;
    private Date hoje;

    private Date dataInicio;
    private Date dataFim;
    private Long selectedSolicitanteId;
    private List<AgendamentoDTO> agendamentosPorSolicitante;

    @PostConstruct
    public void init() {
        solicitante = new Solicitante();
        solicitantes = solicitanteService.findAll();
        agendamentoList = agendamentoService.findAll();
        numeros = new ArrayList<>();
        agendamento = new Agendamento();
        agendamento.setSolicitante(new Solicitante());
        hoje = new Date();
    }

    // Atualiza a lista de solicitantes
    public void updateSolicitantes() {
        solicitantes = solicitanteService.findAll();
    }

    // Carrega os números de vagas disponíveis para a data selecionada
    public void onDataSelect(AjaxBehaviorEvent event) {
        if (agendamento.getData() != null) {
            List<Vagas> vagasList = vagasService.findByData(agendamento.getData());
            numeros.clear();
            for (Vagas vagas : vagasList) {
                for (int i = 1; i <= vagas.getQuantidade(); i++) {
                    numeros.add(i);
                }
            }
        }
    }

    // Limpa o formulário de agendamento
    public void clearForm() {
        agendamento = new Agendamento();
        agendamento.setSolicitante(new Solicitante());
        numeros.clear();
    }

    // RN-3: Permitir o cadastro de agendamentos de vagas.
    // RN-12: O sistema não deve permitir cadastros de vários agendamentos para o mesmo solicitante. O limite máximo permitido será de 25% em relação à quantidade de vagas disponíveis para o período.
    // RN-13: Todos os campos em todas as telas deverão ser obrigatórios.
    public String saveAgendamento() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (agendamento.getData() == null || agendamento.getNumero() == null || agendamento.getMotivo() == null || agendamento.getSolicitante() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Todos os campos são obrigatórios"));
            return null;
        }

        dataInicio = agendamento.getData();
        dataFim = agendamento.getData();

        if (!validateAgendamento(context)) {
            return null;
        }

        List<Vagas> vagasList = vagasService.findByData(agendamento.getData());
        if (vagasList == null || vagasList.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não há vagas disponíveis para a data selecionada"));
            return null;
        }

        Vagas vagas = vagasList.get(0);
        if (vagas.getQuantidade() <= 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não há vagas disponíveis para o número selecionado"));
            return null;
        }

        try {
            vagas.decrementQuantidade();
            vagasService.save(vagas);

            Solicitante solicitante = solicitanteService.findById(agendamento.getSolicitante().getId());
            agendamento.setSolicitante(solicitante);

            agendamentoService.save(agendamento);
            agendamentoList = agendamentoService.findAll();

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Agendamento salvo com sucesso"));

            clearForm();

            return "agendamento";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar o agendamento: " + e.getMessage()));
            return null;
        }
    }

    // RN-10: O sistema não deve permitir agendamentos para períodos onde não haja quantidade de vagas suficientes.
    // RN-11: O sistema não deve permitir cadastros de vários agendamentos para o mesmo solicitante. O limite máximo
    // permitido será de 25% em relação à quantidade de vagas disponíveis para o período.
    private boolean validateAgendamento(FacesContext context) {
        if (agendamento.getData() != null && agendamento.getSolicitante() != null) {
            Long totalAgendamentos = agendamentoService.countAgendamentosBySolicitante(dataInicio, dataFim, agendamento.getSolicitante().getId());
            Integer totalVagas = vagasService.sumVagasByDateRange(dataInicio, dataFim);

            if (totalAgendamentos >= (totalVagas * 0.25)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Limite de 25% atingido para o solicitante neste período"));
                return false;
            }
        }
        return true;
    }

    // Carrega todos os agendamentos
    public void loadAgendamentos() {
        agendamentoList = agendamentoService.findAll();
    }


    // RN-8: Consulta de agendamentos por solicitante passando o início, fim e o solicitante. Deve ser exibida a lista de solicitantes, total de agendamentos realizados, quantidade de vagas e percentual do total agendado x quantidade de vagas para o período informado.
    public void filterAgendamentosPorSolicitante() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (dataInicio == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de início é obrigatória"));
            return;
        }

        if (dataFim == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de fim é obrigatória"));
            return;
        }

        if (selectedSolicitanteId == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Solicitante é obrigatório"));
            return;
        }

        List<Object[]> resultados = agendamentoService.countAgendamentosBySolicitanteAndDateRange(dataInicio, dataFim);
        Integer totalVagas = vagasService.sumVagasByDateRange(dataInicio, dataFim);

        agendamentosPorSolicitante = new ArrayList<>();
        for (Object[] resultado : resultados) {
            Long solicitanteId = (Long) resultado[0];
            Long totalAgendamentos = (Long) resultado[1];

            if (solicitanteId.equals(selectedSolicitanteId)) {
                Solicitante solicitante = solicitanteService.findById(solicitanteId);
                double percentual = (totalAgendamentos.doubleValue() / totalVagas) * 100;

                agendamentosPorSolicitante.add(new AgendamentoDTO(solicitante.getNome(), totalAgendamentos, totalVagas, percentual));
            }
        }
        clearFilters();
    }

    // Limpa os filtros de agendamento
    public void clearFilters() {
        dataInicio = null;
        dataFim = null;
        selectedSolicitanteId = null;
    }

    // RN-4: Permitir a consulta de agendamentos por período.
    public void filterAgendamentos() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (dataInicio == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de início é obrigatória"));
            return;
        }

        if (dataFim == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de fim é obrigatória"));
            return;
        }

        if (selectedSolicitanteId == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Solicitante é obrigatório"));
            return;
        }

        if (dataInicio != null && dataFim != null && selectedSolicitanteId != null) {
            agendamentoList = agendamentoService.findByDateRangeAndSolicitante(dataInicio, dataFim, selectedSolicitanteId);
        } else if (dataInicio != null && dataFim != null) {
            agendamentoList = agendamentoService.findByDateRange(dataInicio, dataFim);
        } else {
            agendamentoList = agendamentoService.findAll();
        }
        clearFilters();
    }
}
