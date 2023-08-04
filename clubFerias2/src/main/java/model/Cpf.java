package model;

import br.com.caelum.stella.validation.CPFValidator;

public class Cpf implements Document {

    private String number;

    public Cpf(String number) {
        number = number.trim().replace("[^\\d]", "");
        CPFValidator cpfValidator = new CPFValidator();
        if (!cpfValidator.invalidMessagesFor(number).isEmpty()) {
            throw new IllegalArgumentException("O CPF é inválido!");
        }
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getDocumentSting() {
        return "CPF: " + number;
    }
}
