package com.simulador;

import java.util.Objects;

public class Processo {
    private final int id;
    private final int instrucoesTotais;
    private int quantidadeInstrucoes;

    public Processo(int id, int quantidadeInstrucoes) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do processo deve ser positivo.");
        }
        if (quantidadeInstrucoes <= 0) {
            throw new IllegalArgumentException("Quantidade de instruções deve ser maior que zero.");
        }
        this.id = id;
        this.instrucoesTotais = quantidadeInstrucoes;
        this.quantidadeInstrucoes = quantidadeInstrucoes;
    }

    public int getId() {
        return id;
    }

    public int getQuantidadeInstrucoes() {
        return quantidadeInstrucoes;
    }

    public int getInstrucoesTotais() {
        return instrucoesTotais;
    }

    public void executarPasso() {
        if (quantidadeInstrucoes > 0) {
            quantidadeInstrucoes--;
        }
    }

    public boolean estaFinalizado() {
        return quantidadeInstrucoes == 0;
    }

    @Override
    public String toString() {
        return String.format("[PID %02d | Restante: %02d/%02d]", id, quantidadeInstrucoes, instrucoesTotais);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processo processo = (Processo) o;
        return id == processo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
