package salachat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConexaoCliente implements Runnable {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final Socket socket;
    private final ServidorChat servidor;
    private DataInputStream entrada;
    private DataOutputStream saida;
    private String apelido = "anonimo";

    public ConexaoCliente(Socket socket, ServidorChat servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());

            apelido = entrada.readUTF().trim();
            if (apelido.isEmpty()) {
                apelido = "user" + socket.getPort();
            }

            servidor.entrar(this);
            enviar("Bem-vindo, " + apelido + "! Pessoas onlne: " + servidor.totalOnline()
                    + ". Digite /sair para encerrar.");

            String texto;
            while ((texto = entrada.readUTF()) != null) {
                if (texto.equalsIgnoreCase("/sair")) {
                    break;
                }
                String formatada = "[" + LocalTime.now().format(HORA) + "] " + apelido + ": " + texto;
                System.out.println(formatada);
                servidor.transmitir(this, formatada);
            }
        } catch (IOException e) {
        } finally {
            servidor.sair(this);
            fechar();
        }
    }

    public synchronized void enviar(String texto) {
        if (saida == null) return;
        try {
            saida.writeUTF(texto);
            saida.flush();
        } catch (IOException e) {
            fechar();
        }
    }

    private void fechar() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public String getApelido() {
        return apelido;
    }
}
