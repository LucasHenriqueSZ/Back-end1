package infrastructure.entities;

public class CategoriaEspaco {

    private String codigo;

    private String nome;

    public CategoriaEspaco(String nome,String codigo) {
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
