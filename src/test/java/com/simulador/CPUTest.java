package com.simulador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {

    private GeradorDeProcessos gerador;

    @BeforeEach
    void setUp() {
        gerador = new GeradorDeProcessos(5, 10);
    }

    @Test
    @DisplayName("Deve executar ciclos ate finalizar um processo no algoritmo FCFS")
    void testExecucaoCompletaFCFS() {
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.FCFS, 5, 0.0);
        Processo p1 = new Processo(1, 2);
        escalonador.adicionarProcesso(p1);

        CPU cpu = new CPU(escalonador, 0);

        // Ciclo 1: Seleciona p1 e executa 1 passo (restante 1)
        cpu.executarCiclo();
        assertEquals(1, cpu.getCicloAtual());
        assertNotNull(cpu.getProcessoAtual());
        assertEquals(1, cpu.getProcessoAtual().getQuantidadeInstrucoes());

        // Ciclo 2: Executa 1 passo (restante 0) -> processo finaliza e processoAtual vira null
        cpu.executarCiclo();
        assertEquals(2, cpu.getCicloAtual());
        assertNull(cpu.getProcessoAtual());
        assertTrue(p1.estaFinalizado());
    }

    @Test
    @DisplayName("Deve realizar preempcao no algoritmo Round Robin ao esgotar o quantum")
    void testPreempcaoRoundRobin() {
        int quantum = 2;
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.ROUND_ROBIN, quantum, 0.0);

        Processo p1 = new Processo(1, 5);
        escalonador.adicionarProcesso(p1);

        CPU cpu = new CPU(escalonador, 0);

        // Ciclo 1: Quantum gasto 1/2
        cpu.executarCiclo();
        assertEquals(1, cpu.getQuantumGasto());
        assertNotNull(cpu.getProcessoAtual());

        // Ciclo 2: Quantum gasto 2/2 -> Preempcao acionada, processo devolvido a fila e processoAtual vira null
        cpu.executarCiclo();
        assertNull(cpu.getProcessoAtual());
        assertEquals(1, escalonador.getTotalEmFila());

        // Ciclo 3: Pega p1 novamente da fila e executa 1 passo (restante 2)
        cpu.executarCiclo();
        assertNotNull(cpu.getProcessoAtual());
        assertEquals(1, cpu.getProcessoAtual().getId());
        assertEquals(2, cpu.getProcessoAtual().getQuantidadeInstrucoes());
    }

    @Test
    @DisplayName("Deve manter a CPU em estado ocioso quando nao ha processos")
    void testCpuOciosa() {
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.FCFS, 5, 0.0);
        CPU cpu = new CPU(escalonador, 0);

        cpu.executarCiclo();
        assertEquals(1, cpu.getCicloAtual());
        assertNull(cpu.getProcessoAtual());
    }

    @Test
    @DisplayName("Deve lancar excecao para escalonador nulo")
    void testCpuEscalonadorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new CPU(null, 100));
    }
}
