import random
from collections import deque
from typing import Optional
from processo import Processo
from gerador import GeradorDeProcessos


class Escalonador:

    ALGORITMOS_VALIDOS = ("FCFS", "SJF", "ROUND_ROBIN")

    def __init__(
        self,
        gerador: GeradorDeProcessos,
        algoritmo: str = "FCFS",
        quantum: int = 5,
        chance_novo_processo: float = 0.20
    ):
        algoritmo_normalizado = algoritmo.upper()
        if algoritmo_normalizado not in self.ALGORITMOS_VALIDOS:
            raise ValueError(f"Algoritmo '{algoritmo}' inválido. Escolha entre: {self.ALGORITMOS_VALIDOS}")

        self._gerador = gerador
        self._algoritmo = algoritmo_normalizado
        self._quantum = quantum
        self._chance_novo_processo = chance_novo_processo
        self._fila_prontos: deque[Processo] = deque()

    @property
    def algoritmo(self) -> str:
        return self._algoritmo

    @property
    def quantum(self) -> int:
        return self._quantum

    @property
    def total_em_fila(self) -> int:
        return len(self._fila_prontos)

    def adicionar_processo(self, processo: Processo) -> None:
        self._fila_prontos.append(processo)

    def devolver_processo(self, processo: Processo) -> None:
        self._fila_prontos.append(processo)

    def _tentar_receber_novo_processo(self) -> None:
        if random.random() < self._chance_novo_processo:
            novo_processo = self._gerador.criar_processo()
            self.adicionar_processo(novo_processo)
            print(f"  [Escalonador] + Chegou novo processo: {novo_processo}")

    def obter_proximo_processo(self) -> Optional[Processo]:
        self._tentar_receber_novo_processo()

        if not self._fila_prontos:
            return None

        # FCFS e Round Robin consom o primeiro elemento da fila (FIFO)
        if self._algoritmo in ("FCFS", "ROUND_ROBIN"):
            return self._fila_prontos.popleft()

        # SJF: seleciona o processo que possui a menor quantidade de instruções restantes
        elif self._algoritmo == "SJF":
            menor_processo = min(self._fila_prontos, key=lambda p: p.quantidade_instrucoes)
            self._fila_prontos.remove(menor_processo)
            return menor_processo

        return None