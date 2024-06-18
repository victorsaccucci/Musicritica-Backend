package com.Musicritica.VictorArthurGabriel.entity;

import lombok.Data;

@Data
public class MapeamentoNotas {
    private double nota;
    private long quantidade;

    public MapeamentoNotas(double nota, long quantidade) {
        this.nota = nota;
        this.quantidade = quantidade;
    }
}
