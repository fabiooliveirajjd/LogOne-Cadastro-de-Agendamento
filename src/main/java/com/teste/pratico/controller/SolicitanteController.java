package com.teste.pratico.controller;

import com.teste.pratico.entity.Solicitante;
import com.teste.pratico.service.SolicitanteService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionScope
@Data
@RequiredArgsConstructor
public class SolicitanteController {

    private final SolicitanteService solicitanteService;

    private List<Solicitante> solicitanteList = new ArrayList<>();
    private Solicitante solicitante = new Solicitante();

    @PostConstruct
    public void init() {
        solicitanteList = solicitanteService.findAll();
    }

    // RN-2: Permitir o cadastro de solicitantes.
    public void saveSolicitante() {
        try {
            // Validação do nome não pode ser nulo ou vazio
            if (solicitante.getNome() == null || solicitante.getNome().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar solicitante", "Nome é obrigatório"));
                return;
            }

            solicitanteService.save(solicitante);
            solicitanteList.clear();
            solicitanteList = solicitanteService.findAll();
            solicitanteList.add(solicitante);
            solicitante = new Solicitante();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Solicitante cadastrado com sucesso!"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar solicitante", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar solicitante", e.getMessage()));
        }
    }
}
