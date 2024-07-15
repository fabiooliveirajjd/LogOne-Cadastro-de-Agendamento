package com.teste.pratico.service;

import com.teste.pratico.entity.Vagas;
import com.teste.pratico.repository.VagasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VagasService {

    private final VagasRepository vagasRepository;

    //Salvar vagas
    public Vagas save(Vagas vagas) {
        return vagasRepository.save(vagas);
    }

    //Listar vagas por data de inicio
    public List<Vagas> findByInicio(Date data) {
        return vagasRepository.findByInicio(data);
    }

    //Listar vagas por data
    public List<Vagas> findByData(Date data) {
        return findByInicio(data);
    }

    //Listar vagas por data de inicio e fim
    public Integer sumVagasByDateRange(Date dataInicio, Date dataFim) {
        return vagasRepository.sumVagasByDateRange(dataInicio, dataFim);
    }

}
