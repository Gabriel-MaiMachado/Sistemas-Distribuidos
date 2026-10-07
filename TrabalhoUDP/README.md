# Trabalho Prático – Cliente/Servidor UDP (MVC)

Aplicação cliente-servidor em Java usando UDP (classe `Comunicador`) e interface gráfica em Swing, organizada no padrão MVC.

## Estrutura

```
src/main/java/
├── model/
│   └── Pessoa.java              -> dados do usuário e regra do token (60s)
├── view/
│   └── TelaCliente.java         -> interface gráfica (Swing), só exibe e repassa eventos
└── controller/
    ├── Comunicador.java         -> monta, envia e recebe os datagramas UDP
    ├── ClienteUDP.java          -> envia os pedidos do cliente ao servidor
    ├── ClienteController.java   -> trata os eventos da tela e interpreta as respostas
    └── ServidorController.java  -> servidor UDP (porta 5000) com a lista de pessoas
```

## Protocolo (campos separados por `|`)

| Cliente envia              | Servidor responde                                  |
|----------------------------|----------------------------------------------------|
| `CADASTRAR|nome|email`     | `OK|mensagem` ou `ERRO|mensagem` (e-mail repetido)  |
| `TOKEN|email`              | `TOKEN|chave|segundosRestantes` ou `ERRO|mensagem`  |

## Regras

- O servidor guarda as pessoas em um `ArrayList<Pessoa>` e recusa cadastro com e-mail já existente.
- O token é uma chave aleatória de 8 caracteres e vale 60 segundos a partir de quando foi gerado.
- Se o cliente pedir de novo dentro dos 60s, recebe o mesmo token; depois disso, um novo é gerado.
- O cliente pede o token automaticamente a cada 15 segundos (dá para desligar ou pedir manualmente).

## Como executar (NetBeans)

1. Abra a pasta `TrabalhoUDP` como projeto Maven.
2. Rode primeiro `controller/ServidorController.java` (botão direito → Run File).
3. Depois rode `view/TelaCliente.java`. Pode abrir mais de um cliente ao mesmo tempo.
