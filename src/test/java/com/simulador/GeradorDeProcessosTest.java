package com.simulador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDeProcessosTest {

    @Test
    @DisplayName("Deve criar processos com PIDs sequenciais e instrucoes dentro do limite")
    void testCriarProcessoValido() {
        GeradorDeProcessos gerador = new GeradorDeProcessos(5, 15);

        Processo p1 = gerador.criarProcesso();
        assertEquals(1, p1.getId());
        assertTrue(p1.getQuantidadeInstrucoes() >= 5 && p1.getQuantidadeInstrucoes() <= 15);

        Processo p2 = gerador.criarProcesso();
        assertEquals(2, p2.getId());
        assertTrue(p2.getQuantidadeInstrucoes() >= 5 && p2.getQuantidadeInstrucoes() <= 15);
    }

    @Test
    @DisplayName("Deve lancar excecao para limites invalidos de instrucao")
    void testLimitesInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new GeradorDeProcessos(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new GeradorDeProcessos(20, 10));
    }

    @Test
    @DisplayName("Deve gerar processos determinísticos com Random semeado")
    void testGeradorDeterministico() {
        Random random = new Random(42);
        GeradorDeProcessos gerador = new GeradorDeProcessos(10, 50, random);

        Processo p = gerador.criarProcesso();
        assertNotNull(p);
        assertEquals(1, p.getId());
    }
}
