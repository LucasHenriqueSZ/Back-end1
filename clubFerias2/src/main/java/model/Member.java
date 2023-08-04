package model;

import util.GenerateCardCode;

import java.time.LocalDate;

public class Member {

    private String cardNumber;

    private String name;

    private LocalDate dateAssociation;

    private Document document;

    public Member(String name, Document document) {
        setName(name);
        setDocument(document);
        setDateAssociation(LocalDate.now());
        setCardNumber(GenerateCardCode.getCardCode());
    }

    public Member() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido!");
        if (name.length() < 3)
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
        if (name.length() > 50)
            throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");
        if (!name.matches("[a-zA-Z\\s]+"))
            throw new IllegalArgumentException("Nome deve conter apenas letras!");
        this.name = name;
    }

    public LocalDate getDateAssociation() {
        return dateAssociation;
    }

    public void setDateAssociation(LocalDate dateAssociation) {
        this.dateAssociation = dateAssociation;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        if (document == null)
            throw new IllegalArgumentException("Documento inválido!");
        this.document = document;
    }
}
