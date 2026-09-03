import time
from typing import Optional
from processo import Processo
from escalonador import Escalonador


class CPU:

    def __init__(self, escalonador: Escalonador, clock_delay: float = 0.1):
        self._escalonador = escalonador
        self._clock_delay = clock_delay
        self._ciclo_atual = 0
        self._processo_atual: Optional[Processo] = None
        self._quantum_gasto = 0

    def iniciar(self) -> None:
        print(f"=== CPU Iniciada | Política: {self._escalonador.algoritmo} | Quantum: {self._escalonador.quantum} ===")
        try:
            while True:
                self._ciclo_atual += 1

                if self._processo_atual is None:
                    self._processo_atual = self._escalonador.obter_proximo_processo()
                    self._quantum_gasto = 0

                if self._processo_atual is not None:
                    self._processo_atual.executar_passo()
                    self._quantum_gasto += 1

                    info_quantum = (
                        f"| Quantum: {self._quantum_gasto}/{self._escalonador.quantum}"
                        if self._escalonador.algoritmo == "ROUND_ROBIN"
                        else ""
                    )

                    print(
                        f"[Clock {self._ciclo_atual:04d}] Executando PID {self._processo_atual.id:02d} "
                        f"| Restante: {self._processo_atual.quantidade_instrucoes:02d} {info_quantum}"
                    )

                    if self._processo_atual.esta_finalizado():
                        print(f"  >>> [Fim] PID {self._processo_atual.id:02d} finalizou no clock {self._ciclo_atual}!")
                        self._processo_atual = None

                    elif (
                        self._escalonador.algoritmo == "ROUND_ROBIN"
                        and self._quantum_gasto >= self._escalonador.quantum
                    ):
                        print(f"  --- [Preempção] Quantum esgotado para PID {self._processo_atual.id:02d}. Retornando à fila.")
                        self._escalonador.devolver_processo(self._processo_atual)
                        self._processo_atual = None

                else:
                    print(f"[Clock {self._ciclo_atual:04d}] CPU Ociosa (nenhum processo na fila)...")
                    self._processo_atual = self._escalonador.obter_proximo_processo()

                time.sleep(self._clock_delay)

        except KeyboardInterrupt:
            print("\n[CPU] Simulação interrompida pelo usuário.")