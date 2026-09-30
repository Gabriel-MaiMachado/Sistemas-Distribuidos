package emailufn;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class TelaServidor extends JFrame {

    private final DefaultListModel<Usuario> modeloLista = new DefaultListModel<>();
    private final JTextArea areaLog = new JTextArea(6, 30);
    private final JLabel lblTotal = new JLabel("Total: 0");

    public TelaServidor() {
        super("Servidor - Gerador de E-mails UFN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JList<Usuario> lista = new JList<>(modeloLista);
        lista.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane painelLista = new JScrollPane(lista);
        painelLista.setBorder(new TitledBorder("Usuários cadastrados"));

        areaLog.setEditable(false);
        JScrollPane painelLog = new JScrollPane(areaLog);
        painelLog.setBorder(new TitledBorder("Log"));

        JPanel conteudo = new JPanel(new BorderLayout(8, 8));
        conteudo.setBorder(new EmptyBorder(10, 10, 10, 10));
        conteudo.add(lblTotal, BorderLayout.NORTH);
        conteudo.add(painelLista, BorderLayout.CENTER);
        conteudo.add(painelLog, BorderLayout.SOUTH);
        setContentPane(conteudo);

        setSize(480, 440);
        setLocationRelativeTo(null);
    }

    public void mostrarUsuarios(List<Usuario> usuarios) {
        SwingUtilities.invokeLater(() -> {
            modeloLista.clear();
            usuarios.forEach(modeloLista::addElement);
            lblTotal.setText("Total: " + usuarios.size());
        });
    }

    public void registrar(String texto) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SwingUtilities.invokeLater(() -> areaLog.append("[" + hora + "] " + texto + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaServidor tela = new TelaServidor();
            tela.setVisible(true);
            new Thread(new ServidorEmail(tela::mostrarUsuarios, tela::registrar)).start();
        });
    }
}
