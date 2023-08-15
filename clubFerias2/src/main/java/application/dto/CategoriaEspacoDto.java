package application.dto;

public class CategoriaEspacoDto {

    private String codigo;

    private String nome;

    public CategoriaEspacoDto(String nome, String codigo) {
        setNome(nome);
        setCodigo(codigo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
