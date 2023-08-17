package application.dto.socioDto;

import application.dto.socioDto.documentosDto.DocumentoDto;

import java.time.LocalDate;

public class SocioDto {

    private String carteirinha;

    private String nome;

    private LocalDate dataAssociacao;

    private DocumentoDto documento;

    public SocioDto(String name, DocumentoDto document, LocalDate dataAssociacao, String carteirinha) {
        setNome(name);
        setDocumento(document);
        setDataAssociacao(dataAssociacao);
        setCarteirinha(carteirinha);
    }

    public SocioDto(String name) {
        setNome(name);
    }

    public SocioDto() {
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

    public DocumentoDto getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoDto documento) {
        this.documento = documento;
    }
}
