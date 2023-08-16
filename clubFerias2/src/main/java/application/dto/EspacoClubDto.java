package application.dto;

import java.util.HashSet;
import java.util.Set;

public class EspacoClubDto {

    private String codigo;

    private String nome;

    private String descricao;

    private int lotacaoMaxima;

    private Set<CategoriaEspacoDto> nomesCategorias = new HashSet<CategoriaEspacoDto>();

    public EspacoClubDto(String nome, String descricao, int lotacaoMaxima, Set<CategoriaEspacoDto> categorias, String codigo) {
        setNome(nome);
        setDescricao(descricao);
        setLotacaoMaxima(lotacaoMaxima);
        setCategorias(categorias);
        setCodigo(codigo);
    }

    public void addCategoria(CategoriaEspacoDto categoria) {
        this.nomesCategorias.add(categoria);
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

    public Set<CategoriaEspacoDto> getCategorias() {
        return nomesCategorias;
    }

    public void setCategorias(Set<CategoriaEspacoDto> nomesCategorias) {
        this.nomesCategorias = nomesCategorias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
