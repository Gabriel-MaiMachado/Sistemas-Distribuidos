package salachat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class OuvinteMensagens implements Runnable {

    private final Socket socket;
    private volatile boolean ativo = true;

    public OuvinteMensagens(Socket socket) {
        this.socket = socket;
    }

    public void parar() {
        ativo = false;
    }

    @Override
    public void run() {
        try {
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            while (!socket.isClosed()) {
                System.out.println(entrada.readUTF());
            }
        } catch (IOException e) {
            if (ativo) {
                System.out.println("Conexão com o servidor perdida.");
            }
        }
    }
}
