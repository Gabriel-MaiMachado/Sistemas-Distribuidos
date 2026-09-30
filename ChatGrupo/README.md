# Chat em Grupo com Pool de Threads

Chat via sockets TCP em que vários clientes conversam ao mesmo tempo.
O servidor usa um `ExecutorService` (pool fixo de threads): cada cliente
conectado vira uma tarefa `ConexaoCliente` executada por uma thread do pool.
Toda mensagem recebida é repassada para os demais participantes (broadcast).

## Classes
- `ServidorChat` – aceita conexões, guarda os participantes e faz o broadcast.
- `ConexaoCliente` – tarefa do pool que lê as mensagens de um cliente.
- `ClienteChat` – lê o teclado e envia para o servidor.
- `OuvinteMensagens` – thread do cliente que recebe e imprime as mensagens.

## Como executar
```
javac -d bin src/main/java/salachat/*.java
java -cp bin salachat.ServidorChat
java -cp bin salachat.ClienteChat        (abrir em vários terminais)
```
Digite `/sair` para sair da sala.
