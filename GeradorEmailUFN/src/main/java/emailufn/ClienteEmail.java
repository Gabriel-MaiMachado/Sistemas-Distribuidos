package emailufn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteEmail {

    private final String host;
    private final int porta;

    public ClienteEmail(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public Resposta pedirEmail(String nomeCompleto) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(host, porta)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(nomeCompleto);
            out.flush();
            return (Resposta) in.readObject();
        }
    }
}
