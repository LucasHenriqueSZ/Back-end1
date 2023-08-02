package com.newGo.clubFerias.model;

import com.newGo.clubFerias.util.LetterCodeGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbr_id")
    private long id;

    @Column(name = "mbr_name")
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    private String name;

    @Column(name = "mbr_cpf")
    @NotBlank(message = "CPF is required")
    @CPF(message = "invalid CPF")
    private String cpf;

    @Column(name = "mbr_dt_association")
    @PastOrPresent(message = "invalid date")
    private LocalDate dateAssociation;

    @Column(name = "mbr_card_code")
    private String cardCode;

    public Member() {
    }

    @PostPersist
    public void generateCardCode() {
        if (this.cardCode != null) return;
        this.cardCode = this.id + "-" + LetterCodeGenerator.generate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDate getDateAssociation() {
        return dateAssociation;
    }

    public void setDateAssociation(LocalDate dateAssociation) {
        this.dateAssociation = dateAssociation;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
}
