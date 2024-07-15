package com.teste.pratico.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// RN-7: Agendamentos devem ter os seguintes campos: Data: Data do agendamento,
// Número: Número do agendamento, Motivo: Motivação para o agendamento,
// Solicitante: Pessoa que solicitou o agendamento.

@Entity
@Table(name = "agendamento")
@Data
@NoArgsConstructor
public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data = new Date();

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Solicitante solicitante;

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", data=" + data +
                ", numero='" + numero + '\'' +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
