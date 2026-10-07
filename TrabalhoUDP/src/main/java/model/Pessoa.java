package model;

import java.util.Random;

public class Pessoa {

    public static final long VALIDADE_TOKEN = 60_000;

    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final Random random = new Random();

    private String nome;
    private String email;
    private String token;
    private long geradoEm;

    public Pessoa() {
    }

    public Pessoa(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public long getGeradoEm() {
        return geradoEm;
    }

    public boolean tokenValido() {
        return token != null && (System.currentTimeMillis() - geradoEm) < VALIDADE_TOKEN;
    }

    public long segundosRestantes() {
        if (!tokenValido()) {
            return 0;
        }
        long restante = VALIDADE_TOKEN - (System.currentTimeMillis() - geradoEm);
        return (restante + 999) / 1000;
    }

    public String obterToken() {
        if (!tokenValido()) {
            token = gerarTokenAleatorio(8);
            geradoEm = System.currentTimeMillis();
        }
        return token;
    }

    private String gerarTokenAleatorio(int tamanho) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            sb.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return nome + " <" + email + ">";
    }
}
