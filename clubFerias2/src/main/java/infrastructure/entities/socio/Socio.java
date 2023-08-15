package infrastructure.entities.socio;

import infrastructure.entities.socio.documentos.Documento;
import domain.services.util.GeradorCodigoCarteirinha;

import java.time.LocalDate;

public class Socio {

    private String carteirinha;

    private String nome;

    private LocalDate dataAssociacao;

    private Documento documento;

    public Socio(String name, Documento document) {
        setNome(name);
        setDocumento(document);
        setDataAssociacao(LocalDate.now());
        setCarteirinha(GeradorCodigoCarteirinha.getCardCode());
    }

    public Socio() {
    }

    public String getCarteirinha() {
        return carteirinha;
    }

    public void setCarteirinha(String carteirinha) {
        this.carteirinha = carteirinha;
    }

    public java.lang.String getNome() {
        return nome;
    }

    public void setNome(java.lang.String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido!");
        if (nome.length() < 3)
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
        if (nome.length() > 50)
            throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");
        if (!nome.matches("[a-zA-Z\\s]+"))
            throw new IllegalArgumentException("Nome deve conter apenas letras!");
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
        if (documento == null)
            throw new IllegalArgumentException("Documento inválido!");
        this.documento = documento;
    }
}
