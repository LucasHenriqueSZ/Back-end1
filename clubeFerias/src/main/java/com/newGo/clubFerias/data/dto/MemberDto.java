package com.newGo.clubFerias.data.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class MemberDto extends RepresentationModel<MemberDto> {

    private String name;

    private String cpf;

    private String cardCode;

    private LocalDate dateAssociation;

    public MemberDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public LocalDate getDateAssociation() {
        return dateAssociation;
    }

    public void setDateAssociation(LocalDate dateAssociation) {
        this.dateAssociation = dateAssociation;
    }
}
