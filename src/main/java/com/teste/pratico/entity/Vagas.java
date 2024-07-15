package com.teste.pratico.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// RN-5: Vagas devem ter os seguintes campos: Início: Data início, Fim: Data fim, Quantidade: Quantidade de vagas em vigência.

@Entity
@Table(name = "vagas")
@Data
@NoArgsConstructor
public class Vagas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "inicio", nullable = false)
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fim", nullable = false)
    private Date fim;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Override
    public String toString() {
        return "Vagas{" +
                "id=" + id +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", quantidade=" + quantidade +
                '}';
    }

    // Metodo para decrementar a quantidade de vagas quando um agendamento é feito
    public void decrementQuantidade() {
        if (this.quantidade > 0) {
            this.quantidade--;
        }
    }
}
