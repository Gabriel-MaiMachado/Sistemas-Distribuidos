package view;

import controller.ClienteController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TelaCliente extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JButton btnCadastrar;
    private JButton btnToken;
    private JCheckBox chkAutomatico;
    private JLabel lblToken;
    private JLabel lblValidade;
    private JTextArea txtLog;

    private final ClienteController controller;

    public TelaCliente() {
        super("Cliente UDP - Cadastro e Token");
        montarTela();
        controller = new ClienteController(this);
        configurarEventos();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastro"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        painelCadastro.add(new JLabel("Nome completo:"), c);
        c.gridx = 1; c.weightx = 1;
        txtNome = new JTextField(22);
        painelCadastro.add(txtNome, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        painelCadastro.add(new JLabel("E-mail:"), c);
        c.gridx = 1; c.weightx = 1;
        txtEmail = new JTextField(22);
        painelCadastro.add(txtEmail, c);

        c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.EAST;
        btnCadastrar = new JButton("Cadastrar");
        painelCadastro.add(btnCadastrar, c);

        JPanel painelToken = new JPanel(new BorderLayout(4, 4));
        painelToken.setBorder(BorderFactory.createTitledBorder("Token de acesso"));

        lblToken = new JLabel("--------", JLabel.CENTER);
        lblToken.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
        lblValidade = new JLabel("Faça o cadastro para receber um token.", JLabel.CENTER);

        JPanel botoesToken = new JPanel(new FlowLayout());
        btnToken = new JButton("Solicitar token agora");
        btnToken.setEnabled(false);
        chkAutomatico = new JCheckBox("Pedir automaticamente (a cada " + ClienteController.INTERVALO_PEDIDO / 1000 + "s)", true);
        chkAutomatico.setEnabled(false);
        botoesToken.add(btnToken);
        botoesToken.add(chkAutomatico);

        painelToken.add(lblToken, BorderLayout.NORTH);
        painelToken.add(lblValidade, BorderLayout.CENTER);
        painelToken.add(botoesToken, BorderLayout.SOUTH);

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(painelCadastro, BorderLayout.NORTH);
        topo.add(painelToken, BorderLayout.CENTER);

        txtLog = new JTextArea(9, 40);
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Mensagens trocadas"));

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pack();
        setLocationRelativeTo(null);
    }

    private void configurarEventos() {
        btnCadastrar.addActionListener(e -> controller.cadastrar(txtNome.getText(), txtEmail.getText()));
        btnToken.addActionListener(e -> controller.solicitarToken());
        chkAutomatico.addActionListener(e -> controller.definirPedidoAutomatico(chkAutomatico.isSelected()));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.encerrar();
            }
        });
    }

    public void liberarAreaToken() {
        txtNome.setEditable(false);
        txtEmail.setEditable(false);
        btnCadastrar.setEnabled(false);
        btnToken.setEnabled(true);
        chkAutomatico.setEnabled(true);
    }

    public void mostrarToken(String token) {
        lblToken.setText(token);
    }

    public void mostrarValidade(int segundos) {
        if (segundos > 0) {
            lblValidade.setText("Válido por mais " + segundos + " segundos");
            lblValidade.setForeground(segundos <= 10 ? Color.RED : new Color(0, 120, 0));
        } else {
            lblValidade.setText("Token expirado - solicite um novo");
            lblValidade.setForeground(Color.GRAY);
        }
    }

    public void adicionarLog(String texto) {
        String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
        txtLog.append("[" + hora + "] " + texto + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    public void mostrarAviso(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarErro(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new TelaCliente().setVisible(true));
    }
}
