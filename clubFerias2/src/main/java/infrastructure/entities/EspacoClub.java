package infrastructure.entities;

import java.util.HashSet;
import java.util.Set;

public class EspacoClub {

    private String codigo;

    private String nome;

    private String descricao;

    private int lotacaoMaxima;

    private Set<String> categorias = new HashSet<String>();

    public EspacoClub(String nome, String descricao, int lotacaoMaxima, Set<String> categorias, String codigo) {
        setNome(nome);
        setDescricao(descricao);
        setLotacaoMaxima(lotacaoMaxima);
        setCategorias(categorias);
        setCodigo(codigo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido!");
        if (nome.length() < 3)
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
        if (nome.length() > 50)
            throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");

        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido!");
        if (descricao.length() < 3)
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
        if (descricao.length() > 50)
            throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");

        this.descricao = descricao;
    }

    public int getLotacaoMaxima() {
        return lotacaoMaxima;
    }

    public void setLotacaoMaxima(int lotacaoMaxima) {
        if( lotacaoMaxima <0)
            throw new IllegalArgumentException("A lotação maxima nao deve ser negativa");

        this.lotacaoMaxima = lotacaoMaxima;
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<String> categorias) {
        if(categorias.isEmpty() || categorias == null)
            throw new IllegalArgumentException("O espaço deve conter no mínimo 1 categoria");

        this.categorias = categorias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
