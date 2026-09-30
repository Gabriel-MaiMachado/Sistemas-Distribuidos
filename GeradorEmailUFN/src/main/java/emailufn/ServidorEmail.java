package emailufn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ServidorEmail implements Runnable {

    public static final int PORTA = 6000;

    private final TreeSet<Usuario> cadastrados = new TreeSet<>();
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final Consumer<List<Usuario>> aoAtualizar;
    private final Consumer<String> aoRegistrar;

    public ServidorEmail(Consumer<List<Usuario>> aoAtualizar, Consumer<String> aoRegistrar) {
        this.aoAtualizar = aoAtualizar;
        this.aoRegistrar = aoRegistrar;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            aoRegistrar.accept("Servidor escutando na porta " + PORTA);
            while (true) {
                Socket cliente = serverSocket.accept();
                pool.execute(() -> atender(cliente));
            }
        } catch (IOException e) {
            aoRegistrar.accept("Erro: " + e.getMessage());
        }
    }

    private void atender(Socket cliente) {
        try (Socket s = cliente) {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush(); // manda o cabeçalho do stream antes de abrir a entrada
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            String nomeRecebido = (String) in.readObject();
            Usuario usuario = new Usuario(
                    GeradorDeEmail.formatarNome(nomeRecebido),
                    GeradorDeEmail.gerar(nomeRecebido));

            boolean novo = cadastrar(usuario);
            aoRegistrar.accept((novo ? "Cadastrado: " : "Repetido: ") + usuario.getEmail());

            out.writeObject(new Resposta(novo, usuario));
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            aoRegistrar.accept("Falha ao atender cliente: " + e.getMessage());
        }
    }

    // synchronized porque várias threads do pool podem cadastrar ao mesmo tempo
    private synchronized boolean cadastrar(Usuario usuario) {
        boolean jaExiste = cadastrados.stream().anyMatch(u -> u.equals(usuario));
        if (jaExiste) return false;

        cadastrados.add(usuario);
        aoAtualizar.accept(new ArrayList<>(cadastrados));
        return true;
    }
}
