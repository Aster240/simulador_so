package com.simulador;

public class Main {
    public static void main(String[] args) {
        String algoritmoEscolhido = "SJF";
        int valorQuantum = 4;
        double chanceChegadaProcesso = 0.20;
        long delayClockMs = 100; // 0.1s = 100ms
        int processosIniciais = 3;

        GeradorDeProcessos gerador = new GeradorDeProcessos();

        Escalonador escalonador = new Escalonador(
                gerador,
                algoritmoEscolhido,
                valorQuantum,
                chanceChegadaProcesso
        );

        // Carga inicial para a CPU
        for (int i = 0; i < processosIniciais; i++) {
            escalonador.adicionarProcesso(gerador.criarProcesso());
        }

        CPU cpu = new CPU(escalonador, delayClockMs);
        cpu.iniciar();
    }
}
