package com.simulador;

public enum AlgoritmoEscalonamento {
    FCFS,
    SJF,
    ROUND_ROBIN;

    public static AlgoritmoEscalonamento fromString(String algoritmo) {
        if (algoritmo == null) {
            throw new IllegalArgumentException("Algoritmo não pode ser nulo.");
        }
        try {
            return AlgoritmoEscalonamento.valueOf(algoritmo.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Algoritmo '" + algoritmo + "' inválido. Escolha entre: FCFS, SJF, ROUND_ROBIN");
        }
    }
}
