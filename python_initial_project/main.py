from gerador import GeradorDeProcessos
from escalonador import Escalonador
from cpu import CPU


def main():
    # Algoritmos disponíveis: 
    # "FCFS"(Primeiro a chegar, primeiro a ser servido.), 
    # "SJF"(menor tempo de execução primeiro), 
    # "ROUND_ROBIN"(tempo de execução limitado)
    ALGORITMO_ESCOLHIDO = "SJF"
    VALOR_QUANTUM = 4
    CHANCE_CHEGADA_PROCESSO = 0.20 
    DELAY_CLOCK_SEGUNDOS = 0.1      # 100ms por ciclo de clock
    PROCESSOS_INICIAIS = 3


    gerador = GeradorDeProcessos()

    escalonador = Escalonador(
        gerador=gerador,
        algoritmo=ALGORITMO_ESCOLHIDO,
        quantum=VALOR_QUANTUM,
        chance_novo_processo=CHANCE_CHEGADA_PROCESSO
    )

    # Carga inicial para a CPU
    for _ in range(PROCESSOS_INICIAIS):
        escalonador.adicionar_processo(gerador.criar_processo())

    cpu = CPU(escalonador=escalonador, clock_delay=DELAY_CLOCK_SEGUNDOS)
    cpu.iniciar()


if __name__ == "__main__":
    main()