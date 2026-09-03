package com.simulador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EscalonadorTest {

    private GeradorDeProcessos gerador;

    @BeforeEach
    void setUp() {
        // Gerador simples para testes com chance 0 de novos processos automáticos
        gerador = new GeradorDeProcessos(10, 20);
    }

    @Test
    @DisplayName("Deve escalonar via FCFS (First Come, First Served)")
    void testEscalonamentoFCFS() {
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.FCFS, 5, 0.0);

        Processo p1 = new Processo(1, 15);
        Processo p2 = new Processo(2, 5);
        Processo p3 = new Processo(3, 10);

        escalonador.adicionarProcesso(p1);
        escalonador.adicionarProcesso(p2);
        escalonador.adicionarProcesso(p3);

        assertEquals(3, escalonador.getTotalEmFila());

        assertEquals(p1, escalonador.obterProximoProcesso());
        assertEquals(p2, escalonador.obterProximoProcesso());
        assertEquals(p3, escalonador.obterProximoProcesso());
        assertNull(escalonador.obterProximoProcesso());
    }

    @Test
    @DisplayName("Deve escalonar via SJF (Shortest Job First)")
    void testEscalonamentoSJF() {
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.SJF, 5, 0.0);

        Processo pLong = new Processo(1, 30);
        Processo pShort = new Processo(2, 5);
        Processo pMedium = new Processo(3, 15);

        escalonador.adicionarProcesso(pLong);
        escalonador.adicionarProcesso(pShort);
        escalonador.adicionarProcesso(pMedium);

        // O processo com menor instrução deve vir primeiro
        assertEquals(pShort, escalonador.obterProximoProcesso());
        assertEquals(pMedium, escalonador.obterProximoProcesso());
        assertEquals(pLong, escalonador.obterProximoProcesso());
        assertNull(escalonador.obterProximoProcesso());
    }

    @Test
    @DisplayName("Deve devolver processo para o fim da fila no Round Robin")
    void testDevolverProcessoRoundRobin() {
        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.ROUND_ROBIN, 4, 0.0);

        Processo p1 = new Processo(1, 10);
        Processo p2 = new Processo(2, 10);

        escalonador.adicionarProcesso(p1);
        escalonador.adicionarProcesso(p2);

        Processo retirado = escalonador.obterProximoProcesso();
        assertEquals(p1, retirado);

        escalonador.devolverProcesso(retirado);
        // Agora p2 deve ser o proximo da fila, seguido de p1 devolvido
        assertEquals(p2, escalonador.obterProximoProcesso());
        assertEquals(p1, escalonador.obterProximoProcesso());
    }

    @Test
    @DisplayName("Deve adicionar novos processos caso a chance de chegada seja ativada")
    void testTentativaChegadaNovoProcesso() {
        // Random que sempre retorna 0.0 (menor que 0.5 de chance)
        Random fakeRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.0;
            }
        };

        Escalonador escalonador = new Escalonador(gerador, AlgoritmoEscalonamento.FCFS, 5, 0.5, fakeRandom);
        assertEquals(0, escalonador.getTotalEmFila());

        Processo obtido = escalonador.obterProximoProcesso();
        assertNotNull(obtido);
        assertEquals(1, obtido.getId());
    }

    @Test
    @DisplayName("Deve validar argumentos invalidos no construtor")
    void testArgumentosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Escalonador(null, "FCFS", 5, 0.2));
        assertThrows(IllegalArgumentException.class, () -> new Escalonador(gerador, "INVALID_ALGO", 5, 0.2));
        assertThrows(IllegalArgumentException.class, () -> new Escalonador(gerador, "FCFS", 0, 0.2));
        assertThrows(IllegalArgumentException.class, () -> new Escalonador(gerador, "FCFS", 5, -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Escalonador(gerador, "FCFS", 5, 1.5));
    }
}
