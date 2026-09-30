package salachat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServidorChat {

    public static final int PORTA = 5000;
    private static final int MAX_CLIENTES = 10;

    private final Set<ConexaoCliente> participantes = ConcurrentHashMap.newKeySet();
    private final ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            log("Servidor no ar na porta " + PORTA + " (limite de " + MAX_CLIENTES + " clientes simultâneos)");

            while (true) {
                Socket socket = serverSocket.accept();
                log("Nova conexão de " + socket.getRemoteSocketAddress());

                ConexaoCliente conexao = new ConexaoCliente(socket, this);
                pool.submit(conexao);
            }
        } catch (IOException e) {
            log("Falha no servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    public void entrar(ConexaoCliente conexao) {
        participantes.add(conexao);
        transmitir(conexao, ">> " + conexao.getApelido() + " entrou na sala");
        log(conexao.getApelido() + " entrou. Online: " + participantes.size());
    }

    public void sair(ConexaoCliente conexao) {
        if (participantes.remove(conexao)) {
            transmitir(null, "<< " + conexao.getApelido() + " saiu da sala");
            log(conexao.getApelido() + " saiu. Online: " + participantes.size());
        }
    }

    public void transmitir(ConexaoCliente origem, String texto) {
        for (ConexaoCliente destino : participantes) {
            if (destino != origem) {
                destino.enviar(texto);
            }
        }
    }

    public int totalOnline() {
        return participantes.size();
    }

    private void log(String texto) {
        System.out.println("[SERVIDOR] " + texto);
    }

    public static void main(String[] args) {
        new ServidorChat().iniciar();
    }
}
