package emailufn;

import java.io.Serializable;

public class Resposta implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean cadastroNovo;
    private final Usuario usuario;

    public Resposta(boolean cadastroNovo, Usuario usuario) {
        this.cadastroNovo = cadastroNovo;
        this.usuario = usuario;
    }

    public boolean isCadastroNovo() {
        return cadastroNovo;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
