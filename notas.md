*AULA 1*
Pode escolher entre 3 linguagens, .py - .java ou c# 
vai precisar ter controller - model - service - comunication

-Comunicação
    broadcast, multicast, unicast
    é bloqueante: escrever (writer ou send) e ler (reader ou receiver)
    respeita ou segue o modelo TCP/IP (aplicação, transporte, interface, rede)
        - endereço IP: Servidor, cliente, grupo
        - máscara ou classe de rede e dominio
        - socket
        - porta lógica
    
    Arquitetura: 
    - cliente-servidor
    
    -ponto-a-ponto
    - comunicação bloqueante
    - escrever
    - ler

- programação multitarefa (thread)
    - thread é um mini processo dentro de um processo
    - thread pode ser com memória compartilhada
        - sincronismo: monitor, semáforo
    - thread sem memória compartilhada
    - importância: execução de processos concomitantemente. E em SD, para liberar comunicação bloqueante

THREAD:

- um subprocesso ou um miniprocesso pertencente a um processo (identificador, nome, endereço, tamanho, tempo, instruções) criado em tempo de programação/execução
- finalidade de threads: garantir processamento concomitante/paralelo
- estados de uma thread: execução, finalizado/pronto, espera/aguardando, parado, dormindo, cancelado, ...
- há comandos que garantem sincronismo de processamento.

- Thread com compartilhamento de memória/recurso (e o processamento é bloqueante). Fica de responsabilidade do PROGRAMADOR garantir SINCRONISMO da memória/recurso. ( o processamento é bloqueante). Fica de responsabilidade do PROGRAMADOR garantir sincronismo

- Thread sem compartilhamento de memória/recurso

- Thread em Java -> é processamento concomitante (JVM)
Com compartilhamento de memória -> Interface Runnable
Sem compartilhamento de memória -> Classe Thread


SISTEMAS PARALELOS
- Homogêneos: arquiteturas de hardware, sistema operacional e linguagens de programação idênticas
- Fortemente acoplados (fixos em um mesmo lugar via protocolos do modelo TCP/IP: endereço de rede, porta lógica, máscara de rede, protocolos de transporte)
- CLUSTER computacional
- Arquiteturas: Ponto-a-Ponto
  - Tolerância a falhas
  - Escalabilidade
  - Segurança
  - Manutenção/atualização

- Objetivo: compartilhar recursos (processador e memória)

