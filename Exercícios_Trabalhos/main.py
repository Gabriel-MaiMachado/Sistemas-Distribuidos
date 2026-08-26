from multiprocessing import Process, Queue

class Trabalhador(Process):

    def __init__(self, numero, fila_tarefas, fila_resultados):
        super().__init__()
        self.numero = numero
        self.fila_tarefas = fila_tarefas
        self.fila_resultados = fila_resultados

    def run(self):

        while True:

            tarefa = self.fila_tarefas.get()
            if tarefa is None:
                break

            numero_pedaco, linhas = tarefa

           
            quantidade_erros = 0

            for linha in linhas:
                if "erro" in linha.lower():
                    quantidade_erros += 1

            self.fila_resultados.put(
                (numero_pedaco, self.numero, quantidade_erros)
            )


class Pool:

    def __init__(self, quantidade_trabalhadores):

        self.quantidade = quantidade_trabalhadores

        self.fila_tarefas = Queue()
        self.fila_resultados = Queue()

        self.trabalhadores = []

    def iniciar(self):

        for numero in range(1, self.quantidade + 1):

            trabalhador = Trabalhador(
                numero,
                self.fila_tarefas,
                self.fila_resultados
            )

            trabalhador.start()

            self.trabalhadores.append(trabalhador)

    def enviar(self, tarefa):
        self.fila_tarefas.put(tarefa)

    def receber(self):
        return self.fila_resultados.get()

    def finalizar(self):

        for _ in self.trabalhadores:
            self.fila_tarefas.put(None)

        for trabalhador in self.trabalhadores:
            trabalhador.join()


class Coordenador:

    def __init__(self, nome_arquivo):

        self.nome_arquivo = nome_arquivo

        self.pool = Pool(4)

        self.tamanho_pedaco = 1000

    def executar(self):

        print("Iniciando processamento...\n")

        self.pool.iniciar()

        quantidade_pedacos = self.dividir_arquivo()

        total_erros = 0

        for _ in range(quantidade_pedacos):

            pedaco, trabalhador, erros = self.pool.receber()

            print(
                f"Pedaço {pedaco} -> "
                f"Trabalhador {trabalhador} -> "
                f"{erros} erros"
            )

            total_erros += erros

        self.pool.finalizar()

        print("RESULTADO")
        print(f"Total de erros: {total_erros}")

    def dividir_arquivo(self):

        pedaco = []
        numero_pedaco = 1
        quantidade_pedacos = 0

        with open(
            self.nome_arquivo,
            "r",
            encoding="utf-8",
            errors="ignore"
        ) as arquivo:

            for linha in arquivo:

                pedaco.append(linha)

                if len(pedaco) >= self.tamanho_pedaco:

                    self.pool.enviar(
                        (numero_pedaco, pedaco)
                    )

                    quantidade_pedacos += 1
                    numero_pedaco += 1

                    pedaco = []

            if pedaco:

                self.pool.enviar(
                    (numero_pedaco, pedaco)
                )

                quantidade_pedacos += 1

        return quantidade_pedacos


if __name__ == "__main__":

    coordenador = Coordenador("erro.txt")

    coordenador.executar()