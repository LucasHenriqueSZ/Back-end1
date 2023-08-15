package infrastructure.entities.socio;

import infrastructure.entities.socio.documentos.Documento;

import java.time.LocalDate;

public class Socio {

    private String carteirinha;

    private String nome;

    private LocalDate dataAssociacao;

    private Documento documento;

    public Socio(String name, Documento document, LocalDate dataAssociacao, String carteirinha) {
        setNome(name);
        setDocumento(document);
        setDataAssociacao(dataAssociacao);
        setCarteirinha(carteirinha);
    }

    public Socio() {
    }

    public String getCarteirinha() {
        return carteirinha;
    }

    public void setCarteirinha(String carteirinha) {
        this.carteirinha = carteirinha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAssociacao() {
        return dataAssociacao;
    }

    public void setDataAssociacao(LocalDate dataAssociacao) {
        this.dataAssociacao = dataAssociacao;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
}
