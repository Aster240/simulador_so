import random
from processo import Processo


class GeradorDeProcessos:

    def __init__(self, min_instrucoes: int = 10, max_instrucoes: int = 50):
        self._contador_id = 0
        self._min_instrucoes = min_instrucoes
        self._max_instrucoes = max_instrucoes

    def criar_processo(self) -> Processo:
        self._contador_id += 1
        instrucoes = random.randint(self._min_instrucoes, self._max_instrucoes)
        return Processo(pid=self._contador_id, quantidade_instrucoes=instrucoes)