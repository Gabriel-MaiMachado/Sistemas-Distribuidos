package controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Comunicador {

    public static final int TAM_BUFFER = 1024;

    public static DatagramPacket montaMensagem(String mensagem, InetAddress destino, int porta) {
        byte[] dados = mensagem.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(dados, dados.length, destino, porta);
    }

    public static DatagramPacket montaMensagem(String mensagem, String host, int porta) {
        try {
            return montaMensagem(mensagem, InetAddress.getByName(host), porta);
        } catch (Exception e) {
            System.out.println("Host inválido: " + host);
            return null;
        }
    }

    public static boolean enviaMensagem(DatagramSocket socket, DatagramPacket pacote) {
        if (pacote == null) {
            return false;
        }
        try {
            socket.send(pacote);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DatagramPacket recebeMensagem(DatagramSocket socket) {
        try {
            DatagramPacket pacote = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
            socket.receive(pacote);
            return pacote;
        } catch (SocketTimeoutException e) {
            System.out.println("Tempo de espera esgotado, nenhuma resposta recebida.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String lerTexto(DatagramPacket pacote) {
        return new String(pacote.getData(), 0, pacote.getLength(), StandardCharsets.UTF_8).trim();
    }
}
