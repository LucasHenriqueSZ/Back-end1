package infrastructure.entities;

import domain.services.util.GeradorCodigo;

public class CategoriaEspaco {

    private String codigo;

    private String nome;

    public CategoriaEspaco(String nome) {
        setNome(nome);
        setCodigo(GeradorCodigo.getCodigo());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio!");
        if (nome.length() < 3)
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
        if (nome.length() > 50)
            throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");

        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
