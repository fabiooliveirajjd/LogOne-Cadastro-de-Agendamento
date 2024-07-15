package com.teste.pratico.controller;

import com.teste.pratico.entity.Vagas;
import com.teste.pratico.service.VagasService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionScope
@Data
@RequiredArgsConstructor
public class VagasController {

    private final VagasService vagasService;

    private List<Vagas> vagasList = new ArrayList<>();
    private Vagas vagas = new Vagas();
    private Date hoje = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    @PostConstruct
    public void init() {
        vagasList = new ArrayList<>();
        vagas = new Vagas();
        hoje = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    // RN-1: Permitir o cadastro de vagas disponíveis.
    public void saveVagas() {
        try {
            // Validação das datas
            LocalDate hoje = LocalDate.now();
            LocalDate dataInicio = vagas.getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dataFim = vagas.getFim().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // RN-9: O sistema não deve permitir o cadastro de vagas com datas retroativas (antes da data atual).
            if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar vaga", "Data de início ou término não pode ser retroativa"));
                return;
            }

            if (dataInicio.isAfter(dataFim)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar vaga", "Data de início não pode ser após a data de término"));
                return;
            }

            vagasService.save(vagas);
            vagasList.clear();
            vagasList.add(vagas);
            vagas = new Vagas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vaga cadastrada com sucesso!"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar vaga", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar vaga", e.getMessage()));
        }
    }
}
