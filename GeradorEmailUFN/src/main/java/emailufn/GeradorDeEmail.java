package emailufn;

import java.text.Normalizer;

public final class GeradorDeEmail {

    public static final String DOMINIO = "@ufn.edu.br";

    private GeradorDeEmail() {
    }

    public static boolean nomeValido(String nome) {
        return nome != null && nome.trim().split("\\s+").length >= 2;
    }

    public static String gerar(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().split("\\s+");
        String primeiro = limpar(partes[0]);
        String ultimo = limpar(partes[partes.length - 1]);
        return primeiro + "." + ultimo + DOMINIO;
    }

    private static String limpar(String palavra) {
        String semAcento = Normalizer.normalize(palavra, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase().replaceAll("[^a-z]", "");
    }

    public static String formatarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ").toUpperCase();
    }
}
