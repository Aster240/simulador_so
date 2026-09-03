package com.simulador;

public class CPU {
    private final Escalonador escalonador;
    private final long clockDelayMs;
    private int cicloAtual;
    private Processo processoAtual;
    private int quantumGasto;

    public CPU(Escalonador escalonador) {
        this(escalonador, 100); // 0.1s default
    }

    public CPU(Escalonador escalonador, long clockDelayMs) {
        if (escalonador == null) {
            throw new IllegalArgumentException("Escalonador não pode ser nulo.");
        }
        this.escalonador = escalonador;
        this.clockDelayMs = clockDelayMs;
        this.cicloAtual = 0;
        this.processoAtual = null;
        this.quantumGasto = 0;
    }

    public int getCicloAtual() {
        return cicloAtual;
    }

    public Processo getProcessoAtual() {
        return processoAtual;
    }

    public int getQuantumGasto() {
        return quantumGasto;
    }

    public Escalonador getEscalonador() {
        return escalonador;
    }

    public void executarCiclo() {
        cicloAtual++;

        if (processoAtual == null) {
            processoAtual = escalonador.obterProximoProcesso();
            quantumGasto = 0;
        }

        if (processoAtual != null) {
            processoAtual.executarPasso();
            quantumGasto++;

            String infoQuantum = (escalonador.getAlgoritmo() == AlgoritmoEscalonamento.ROUND_ROBIN)
                    ? String.format("| Quantum: %d/%d", quantumGasto, escalonador.getQuantum())
                    : "";

            System.out.printf("[Clock %04d] Executando PID %02d | Restante: %02d %s%n",
                    cicloAtual, processoAtual.getId(), processoAtual.getQuantidadeInstrucoes(), infoQuantum);

            if (processoAtual.estaFinalizado()) {
                System.out.printf("  >>> [Fim] PID %02d finalizou no clock %d!%n", processoAtual.getId(), cicloAtual);
                processoAtual = null;
            } else if (escalonador.getAlgoritmo() == AlgoritmoEscalonamento.ROUND_ROBIN
                    && quantumGasto >= escalonador.getQuantum()) {
                System.out.printf("  --- [Preempção] Quantum esgotado para PID %02d. Retornando à fila.%n", processoAtual.getId());
                escalonador.devolverProcesso(processoAtual);
                processoAtual = null;
            }
        } else {
            System.out.printf("[Clock %04d] CPU Ociosa (nenhum processo na fila)...%n", cicloAtual);
            processoAtual = escalonador.obterProximoProcesso();
        }
    }

    public void iniciar() {
        System.out.printf("=== CPU Iniciada | Política: %s | Quantum: %d ===%n",
                escalonador.getAlgoritmo(), escalonador.getQuantum());

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\n[CPU] Simulação interrompida pelo usuário.")
        ));

        while (!Thread.currentThread().isInterrupted()) {
            executarCiclo();
            try {
                Thread.sleep(clockDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("\n[CPU] Simulação interrompida.");
                break;
            }
        }
    }
}
