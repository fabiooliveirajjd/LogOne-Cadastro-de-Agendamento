package com.teste.pratico.service;

import com.teste.pratico.entity.Solicitante;
import com.teste.pratico.repository.SolicitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitanteService {

    private final SolicitanteRepository solicitanteRepository;

    //Salvar solicitante
    public Solicitante save(Solicitante solicitante) {
        return solicitanteRepository.save(solicitante);
    }

    //Listar solicitantes
    public List<Solicitante> findAll() {
        return solicitanteRepository.findAll();
    }

    //Buscar solicitante por id
    public Solicitante findById(Long id) {
        return solicitanteRepository.findById(id).orElse(null);
    }
}
