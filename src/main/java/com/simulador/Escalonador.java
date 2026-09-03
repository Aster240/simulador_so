package com.simulador;

import java.util.LinkedList;
import java.util.Random;

public class Escalonador {
    private final GeradorDeProcessos gerador;
    private final AlgoritmoEscalonamento algoritmo;
    private final int quantum;
    private final double chanceNovoProcesso;
    private final LinkedList<Processo> filaProntos;
    private final Random random;

    public Escalonador(GeradorDeProcessos gerador, String algoritmo, int quantum, double chanceNovoProcesso) {
        this(gerador, AlgoritmoEscalonamento.fromString(algoritmo), quantum, chanceNovoProcesso, new Random());
    }

    public Escalonador(GeradorDeProcessos gerador, AlgoritmoEscalonamento algoritmo, int quantum, double chanceNovoProcesso) {
        this(gerador, algoritmo, quantum, chanceNovoProcesso, new Random());
    }

    public Escalonador(GeradorDeProcessos gerador, AlgoritmoEscalonamento algoritmo, int quantum, double chanceNovoProcesso, Random random) {
        if (gerador == null) {
            throw new IllegalArgumentException("Gerador não pode ser nulo.");
        }
        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum deve ser maior que zero.");
        }
        if (chanceNovoProcesso < 0.0 || chanceNovoProcesso > 1.0) {
            throw new IllegalArgumentException("Chance de novo processo deve estar entre 0.0 e 1.0.");
        }
        this.gerador = gerador;
        this.algoritmo = algoritmo;
        this.quantum = quantum;
        this.chanceNovoProcesso = chanceNovoProcesso;
        this.filaProntos = new LinkedList<>();
        this.random = random != null ? random : new Random();
    }

    public AlgoritmoEscalonamento getAlgoritmo() {
        return algoritmo;
    }

    public int getQuantum() {
        return quantum;
    }

    public double getChanceNovoProcesso() {
        return chanceNovoProcesso;
    }

    public int getTotalEmFila() {
        return filaProntos.size();
    }

    public void adicionarProcesso(Processo processo) {
        if (processo != null) {
            filaProntos.addLast(processo);
        }
    }

    public void devolverProcesso(Processo processo) {
        if (processo != null) {
            filaProntos.addLast(processo);
        }
    }

    public void tentarReceberNovoProcesso() {
        if (random.nextDouble() < chanceNovoProcesso) {
            Processo novoProcesso = gerador.criarProcesso();
            adicionarProcesso(novoProcesso);
            System.out.println("  [Escalonador] + Chegou novo processo: " + novoProcesso);
        }
    }

    public Processo obterProximoProcesso() {
        tentarReceberNovoProcesso();

        if (filaProntos.isEmpty()) {
            return null;
        }

        if (algoritmo == AlgoritmoEscalonamento.FCFS || algoritmo == AlgoritmoEscalonamento.ROUND_ROBIN) {
            return filaProntos.pollFirst();
        } else if (algoritmo == AlgoritmoEscalonamento.SJF) {
            Processo menorProcesso = filaProntos.get(0);
            for (Processo p : filaProntos) {
                if (p.getQuantidadeInstrucoes() < menorProcesso.getQuantidadeInstrucoes()) {
                    menorProcesso = p;
                }
            }
            filaProntos.remove(menorProcesso);
            return menorProcesso;
        }

        return null;
    }
}
