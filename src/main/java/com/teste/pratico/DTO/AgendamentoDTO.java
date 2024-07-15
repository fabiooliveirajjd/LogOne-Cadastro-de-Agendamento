package com.teste.pratico.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Dto para retornar o total de agendamentos por solicitante

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AgendamentoDTO {
    private String solicitante;
    private Long totalAgendamentos;
    private Integer totalVagas;
    private double percentual;
}
