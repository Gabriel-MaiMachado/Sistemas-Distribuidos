Trabalho de Sistemas Distribuídos - Cliente/Servidor UDP

Trabalho prático da disciplina de Sistemas Distribuídos. A ideia é um cliente com interface gráfica (Swing) que se cadastra num servidor e fica pedindo um token de tempos em tempos, tudo usando UDP pela classe Comunicador.

O que o programa faz
O cliente manda nome e e-mail pro servidor
O servidor salva numa lista de Pessoa e não deixa cadastrar o mesmo e-mail duas vezes
Depois de cadastrado, o cliente pede o token a cada 15 segundos (ou na hora, pelo botão)
O token dura 60 segundos. Se pedir antes disso o servidor manda o mesmo, se já passou ele gera outro
Organização

Separei em MVC:

model - classe Pessoa, que guarda os dados e cuida do token
view - a tela do cliente
controller - Comunicador, ClienteUDP, ClienteController e o ServidorController
Mensagens

Usei o | pra separar os campos:

CADASTRAR|nome|email - servidor responde OK|... ou ERRO|...
TOKEN|email - servidor responde TOKEN|chave|segundos que faltam ou ERRO|...
Como rodar

Abrir no NetBeans como projeto Maven, rodar primeiro o ServidorController e depois a TelaCliente. Dá pra abrir mais de um cliente ao mesmo tempo pra testar o cadastro duplicado.

O servidor usa a porta 5000.
