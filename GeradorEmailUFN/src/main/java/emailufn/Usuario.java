package emailufn;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable, Comparable<Usuario> {

    private static final long serialVersionUID = 1L;

    private final String nomeCompleto;
    private final String email;

    public Usuario(String nomeCompleto, String email) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int compareTo(Usuario outro) {
        return nomeCompleto.compareToIgnoreCase(outro.nomeCompleto);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario)) return false;
        return email.equalsIgnoreCase(((Usuario) o).email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email.toLowerCase());
    }

    @Override
    public String toString() {
        return nomeCompleto + "  ->  " + email;
    }
}
