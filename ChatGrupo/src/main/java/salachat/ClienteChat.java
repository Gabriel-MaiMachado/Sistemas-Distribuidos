package salachat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {

    private final String host;
    private final int porta;

    public ClienteChat(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executar() {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Escolha seu apelido: ");
        String apelido = teclado.nextLine();

        try (Socket socket = new Socket(host, porta)) {
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            saida.writeUTF(apelido);

            OuvinteMensagens ouvinteMensagens = new OuvinteMensagens(socket);
            Thread ouvinte = new Thread(ouvinteMensagens);
            ouvinte.setDaemon(true);
            ouvinte.start();

            while (teclado.hasNextLine()) {
                String linha = teclado.nextLine();
                if (linha.isBlank()) continue;
                if (linha.equalsIgnoreCase("/sair")) ouvinteMensagens.parar();

                saida.writeUTF(linha);
                saida.flush();

                if (linha.equalsIgnoreCase("/sair")) break;
            }
        } catch (IOException e) {
            System.out.println("Não foi possível falar com o servidor em " + host + ":" + porta);
        }
        System.out.println("Voê saiu do chat.");
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        new ClienteChat(host, ServidorChat.PORTA).executar();
    }
}
