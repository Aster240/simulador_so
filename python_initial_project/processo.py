class Processo:


    def __init__(self, pid: int, quantidade_instrucoes: int):
        self._id = pid
        self._instrucoes_totais = quantidade_instrucoes
        self._quantidade_instrucoes = quantidade_instrucoes

    @property
    def id(self) -> int:
        return self._id

    @property
    def quantidade_instrucoes(self) -> int:
        return self._quantidade_instrucoes

    @property
    def instrucoes_totais(self) -> int:
        return self._instrucoes_totais

    # Lembra um semáforo
    def executar_passo(self) -> None:
        
        if self._quantidade_instrucoes > 0:
            self._quantidade_instrucoes -= 1

    def esta_finalizado(self) -> bool:

        return self._quantidade_instrucoes == 0

    def __repr__(self) -> str:
        return f"[PID {self._id:02d} | Restante: {self._quantidade_instrucoes:02d}/{self._instrucoes_totais:02d}]"