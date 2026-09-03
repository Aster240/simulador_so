package com.simulador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProcessoTest {

    @Test
    @DisplayName("Deve inicializar o processo com os valores corretos")
    void testInicializacaoSucesso() {
        Processo p = new Processo(1, 10);
        assertEquals(1, p.getId());
        assertEquals(10, p.getInstrucoesTotais());
        assertEquals(10, p.getQuantidadeInstrucoes());
        assertFalse(p.estaFinalizado());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("Deve lancar excecao para ID invalido")
    void testIdInvalido(int idInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Processo(idInvalido, 10));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("Deve lancar excecao para quantidade de instrucoes invalida")
    void testInstrucoesInvalidas(int instrucoesInvalidas) {
        assertThrows(IllegalArgumentException.class, () -> new Processo(1, instrucoesInvalidas));
    }

    @Test
    @DisplayName("Deve decrementar quantidade de instrucoes ao executar um passo")
    void testExecutarPasso() {
        Processo p = new Processo(1, 3);

        p.executarPasso();
        assertEquals(2, p.getQuantidadeInstrucoes());
        assertFalse(p.estaFinalizado());

        p.executarPasso();
        assertEquals(1, p.getQuantidadeInstrucoes());
        assertFalse(p.estaFinalizado());

        p.executarPasso();
        assertEquals(0, p.getQuantidadeInstrucoes());
        assertTrue(p.estaFinalizado());

        // Executar alem de 0 nao deve deixar negativo
        p.executarPasso();
        assertEquals(0, p.getQuantidadeInstrucoes());
        assertTrue(p.estaFinalizado());
    }

    @Test
    @DisplayName("Deve formatar o toString corretamente")
    void testToString() {
        Processo p = new Processo(2, 15);
        assertEquals("[PID 02 | Restante: 15/15]", p.toString());
        p.executarPasso();
        assertEquals("[PID 02 | Restante: 14/15]", p.toString());
    }

    @Test
    @DisplayName("Deve testar igualdade de processos com base no ID")
    void testEqualsHashCode() {
        Processo p1 = new Processo(1, 10);
        Processo p2 = new Processo(1, 20);
        Processo p3 = new Processo(2, 10);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
