# Cliente/Servidor Gerador de E-mail

O cliente (interface Swing) envia um nome completo ao servidor por socket.
O servidor gera o e-mail no formato `primeironome.ultimosobrenome@ufn.edu.br`,
guarda o usuário numa lista ordenada sem repetição e devolve um objeto
`Resposta` serializado dizendo se o cadastro é novo ou se já existia.

## Classes
- `Usuario` – objeto serializável (nome + e-mail), ordenado pelo nome.
- `Resposta` – objeto de retorno do servidor.
- `GeradorDeEmail` – regra de geração (remove acentos, deixa minúsculo).
- `ServidorEmail` – lógica de rede; atende cada pedido em uma thread do pool.
- `TelaServidor` – janela com a lista de cadastrados e o log.
- `ClienteEmail` – envia o nome e recebe a resposta.
- `TelaCliente` – janela do cliente.

## Como executar
```
javac -d bin src/main/java/emailufn/*.java
java -cp bin emailufn.TelaServidor
java -cp bin emailufn.TelaCliente
```
