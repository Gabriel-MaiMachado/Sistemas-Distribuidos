package controller;

import model.Pessoa;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServidorController {

    public static final int PORTA = 5000;

    private DatagramSocket socket;
    private final List<Pessoa> pessoas = new ArrayList<>();

    public ServidorController() {
        try {
            socket = new DatagramSocket(PORTA);
        } catch (SocketException e) {
            System.out.println("Não foi possível abrir a porta " + PORTA + ": " + e.getMessage());
            System.exit(1);
        }
    }

    public void iniciar() {
        System.out.println("Servidor UDP rodando na porta " + PORTA + "...");

        while (true) {
            DatagramPacket recebido = Comunicador.recebeMensagem(socket);
            if (recebido == null) {
                continue;
            }

            String mensagem = Comunicador.lerTexto(recebido);
            System.out.println("[" + recebido.getAddress().getHostAddress() + ":" + recebido.getPort() + "] " + mensagem);

            String resposta = tratarMensagem(mensagem);
            System.out.println("  -> " + resposta);

            DatagramPacket envio = Comunicador.montaMensagem(resposta, recebido.getAddress(), recebido.getPort());
            Comunicador.enviaMensagem(socket, envio);
        }
    }

    private String tratarMensagem(String mensagem) {
        String[] campos = mensagem.split("\\|");
        String comando = campos[0].toUpperCase();

        switch (comando) {
            case "CADASTRAR":
                if (campos.length < 3) {
                    return "ERRO|Informe nome e e-mail.";
                }
                return cadastrar(campos[1].trim(), campos[2].trim());

            case "TOKEN":
                if (campos.length < 2) {
                    return "ERRO|Informe o e-mail.";
                }
                return gerarToken(campos[1].trim());

            default:
                return "ERRO|Comando desconhecido: " + comando;
        }
    }

    private synchronized String cadastrar(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            return "ERRO|Nome e e-mail não podem ficar vazios.";
        }

        if (buscarPorEmail(email) != null) {
            return "ERRO|Já existe um cadastro com o e-mail " + email;
        }

        Pessoa p = new Pessoa(nome, email);
        pessoas.add(p);
        System.out.println("  Novo cadastro: " + p + " (total: " + pessoas.size() + ")");
        return "OK|Cadastro realizado com sucesso, " + nome + "!";
    }

    private synchronized String gerarToken(String email) {
        Pessoa p = buscarPorEmail(email);
        if (p == null) {
            return "ERRO|E-mail não cadastrado.";
        }
        String chave = p.obterToken();
        return "TOKEN|" + chave + "|" + p.segundosRestantes();
    }

    private Pessoa buscarPorEmail(String email) {
        for (Pessoa p : pessoas) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new ServidorController().iniciar();
    }
}
