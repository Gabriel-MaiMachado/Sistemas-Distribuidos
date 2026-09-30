package emailufn;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;

public class TelaCliente extends JFrame {

    private final JTextField campoNome = new JTextField(22);
    private final JTextField campoEmail = new JTextField(22);
    private final JButton botaoGerar = new JButton("Gerar e-mail");
    private final ClienteEmail cliente;

    public TelaCliente(ClienteEmail cliente) {
        super("Cliente - Solicitar E-mail");
        this.cliente = cliente;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        campoEmail.setEditable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; painel.add(new JLabel("Nome completo:"), c);
        c.gridx = 1; painel.add(campoNome, c);
        c.gridx = 0; c.gridy = 1; painel.add(new JLabel("E-mail:"), c);
        c.gridx = 1; painel.add(campoEmail, c);
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST; painel.add(botaoGerar, c);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(null);

        botaoGerar.addActionListener(e -> solicitar());
        campoNome.addActionListener(e -> solicitar()); // Enter também envia
    }

    private void solicitar() {
        String nome = campoNome.getText();
        if (!GeradorDeEmail.nomeValido(nome)) {
            JOptionPane.showMessageDialog(this, "Informe pelo menos nome e sobrenome.",
                    "Nome inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        botaoGerar.setEnabled(false);
        new SwingWorker<Resposta, Void>() {
            @Override
            protected Resposta doInBackground() throws Exception {
                return cliente.pedirEmail(nome);
            }

            @Override
            protected void done() {
                botaoGerar.setEnabled(true);
                try {
                    Resposta r = get();
                    campoEmail.setText(r.getUsuario().getEmail());
                    if (r.isCadastroNovo()) {
                        JOptionPane.showMessageDialog(TelaCliente.this,
                                "E-mail criado: " + r.getUsuario().getEmail());
                    } else {
                        JOptionPane.showMessageDialog(TelaCliente.this,
                                "Esse nome já tinha e-mail cadastrado.",
                                "Já existe", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(TelaCliente.this,
                            "Servidor indisponível. Ele está rodando?",
                            "Erro de conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        SwingUtilities.invokeLater(() ->
                new TelaCliente(new ClienteEmail(host, ServidorEmail.PORTA)).setVisible(true));
    }
}
