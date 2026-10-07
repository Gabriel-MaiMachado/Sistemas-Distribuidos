package controller;

import view.TelaCliente;
import javax.swing.Timer;

public class ClienteController {

    public static final int INTERVALO_PEDIDO = 15_000;

    private final TelaCliente tela;
    private ClienteUDP cliente;
    private String emailCadastrado;
    private String tokenAtual;
    private int segundosRestantes;

    private final Timer timerPedido;
    private final Timer timerContagem;

    public ClienteController(TelaCliente tela) {
        this.tela = tela;

        try {
            cliente = new ClienteUDP("localhost", ServidorController.PORTA);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao criar o socket: " + e.getMessage());
            System.exit(1);
        }

        timerPedido = new Timer(INTERVALO_PEDIDO, e -> solicitarToken());
        timerContagem = new Timer(1000, e -> contarSegundo());
    }

    public void cadastrar(String nome, String email) {
        nome = nome.trim();
        email = email.trim();

        if (nome.isEmpty() || email.isEmpty()) {
            tela.mostrarAviso("Preencha o nome e o e-mail.");
            return;
        }
        if (!email.contains("@")) {
            tela.mostrarAviso("Digite um e-mail válido.");
            return;
        }
        if (nome.contains("|") || email.contains("|")) {
            tela.mostrarAviso("O caractere | não é permitido.");
            return;
        }

        tela.adicionarLog("Enviando cadastro de " + nome);
        String resposta = cliente.cadastrar(nome, email);
        String[] partes = resposta.split("\\|", 2);
        String texto = partes.length > 1 ? partes[1] : resposta;

        if (partes[0].equals("OK")) {
            tela.adicionarLog("Servidor: " + texto);
            emailCadastrado = email;
            tela.liberarAreaToken();
            solicitarToken();
            timerPedido.start();
            timerContagem.start();
        } else {
            tela.adicionarLog("Servidor recusou: " + texto);
            tela.mostrarAviso(texto);
        }
    }

    public void solicitarToken() {
        if (emailCadastrado == null) {
            return;
        }
        String resposta = cliente.pedirToken(emailCadastrado);
        String[] partes = resposta.split("\\|");

        if (partes[0].equals("TOKEN") && partes.length >= 3) {
            String tokenNovo = partes[1];
            segundosRestantes = Integer.parseInt(partes[2]);

            if (tokenNovo.equals(tokenAtual)) {
                tela.adicionarLog("Token mantido: " + tokenNovo + " (expira em " + segundosRestantes + "s)");
            } else {
                tela.adicionarLog("NOVO token recebido: " + tokenNovo);
            }
            tokenAtual = tokenNovo;
            tela.mostrarToken(tokenAtual);
            tela.mostrarValidade(segundosRestantes);
        } else {
            String texto = partes.length > 1 ? partes[1] : resposta;
            tela.adicionarLog("Erro: " + texto);
        }
    }

    public void definirPedidoAutomatico(boolean ligado) {
        if (ligado) {
            timerPedido.start();
        } else {
            timerPedido.stop();
        }
    }

    private void contarSegundo() {
        if (segundosRestantes > 0) {
            segundosRestantes--;
        }
        tela.mostrarValidade(segundosRestantes);
    }

    public void encerrar() {
        timerPedido.stop();
        timerContagem.stop();
        cliente.fechar();
    }
}
