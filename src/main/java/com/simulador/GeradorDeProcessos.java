package com.simulador;

import java.util.Random;

public class GeradorDeProcessos {
    private int contadorId;
    private final int minInstrucoes;
    private final int maxInstrucoes;
    private final Random random;

    public GeradorDeProcessos() {
        this(10, 50, new Random());
    }

    public GeradorDeProcessos(int minInstrucoes, int maxInstrucoes) {
        this(minInstrucoes, maxInstrucoes, new Random());
    }

    public GeradorDeProcessos(int minInstrucoes, int maxInstrucoes, Random random) {
        if (minInstrucoes <= 0 || maxInstrucoes < minInstrucoes) {
            throw new IllegalArgumentException("Intervalo de instruções inválido.");
        }
        this.contadorId = 0;
        this.minInstrucoes = minInstrucoes;
        this.maxInstrucoes = maxInstrucoes;
        this.random = random != null ? random : new Random();
    }

    public Processo criarProcesso() {
        contadorId++;
        int instrucoes = random.nextInt(maxInstrucoes - minInstrucoes + 1) + minInstrucoes;
        return new Processo(contadorId, instrucoes);
    }

    public int getContadorId() {
        return contadorId;
    }

    public int getMinInstrucoes() {
        return minInstrucoes;
    }

    public int getMaxInstrucoes() {
        return maxInstrucoes;
    }
}
