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

    public void addCategoria(String categoria) {
        this.categorias.add(categoria);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLotacaoMaxima() {
        return lotacaoMaxima;
    }

    public void setLotacaoMaxima(int lotacaoMaxima) {
        this.lotacaoMaxima = lotacaoMaxima;
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<String> categorias) {
        this.categorias = categorias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
