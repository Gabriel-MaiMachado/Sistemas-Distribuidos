package controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClienteUDP {

    private final String hostServidor;
    private final int portaServidor;
    private final DatagramSocket socket;

    public ClienteUDP(String hostServidor, int portaServidor) throws SocketException {
        this.hostServidor = hostServidor;
        this.portaServidor = portaServidor;
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(3000);
    }

    public String cadastrar(String nome, String email) {
        return enviarEReceber("CADASTRAR|" + nome + "|" + email);
    }

    public String pedirToken(String email) {
        return enviarEReceber("TOKEN|" + email);
    }

    private String enviarEReceber(String mensagem) {
        DatagramPacket pacote = Comunicador.montaMensagem(mensagem, hostServidor, portaServidor);
        if (!Comunicador.enviaMensagem(socket, pacote)) {
            return "ERRO|Falha ao enviar a mensagem.";
        }

        DatagramPacket resposta = Comunicador.recebeMensagem(socket);
        if (resposta == null) {
            return "ERRO|Servidor não respondeu (verifique se ele está rodando).";
        }
        return Comunicador.lerTexto(resposta);
    }

    public void fechar() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
