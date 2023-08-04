package model;

public class Rg implements Document {

    private String number;

    public Rg(String number) {
        if (!isValidRg(number)) {
            throw new IllegalArgumentException("O RG é inválido!");
        }
        this.number = number.trim();
    }

    private boolean isValidRg(String number) {
        if (number == null || number.trim().isEmpty() || !number.matches("[0-9X]+") || number.length() != 9) {
            return false;
        }

        String verifiedNumber = number.trim();

        String lastDigit = verifiedNumber.substring(verifiedNumber.length() - 1);

        if (lastDigit.equals("X")) {
            lastDigit = "10";
        } else if (lastDigit.equals("0")) {
            lastDigit = "11";
        }

        verifiedNumber = verifiedNumber.substring(0, verifiedNumber.length() - 1);

        String digito = discoverDigit(verifiedNumber);
        return digito.equals(lastDigit);
    }

    private String discoverDigit(String rg) {
        char[] digits = rg.toCharArray();
        int[] totals = new int[digits.length];
        int total = 0;

        for (int i = 0; i < digits.length; i++) {
            totals[i] = Character.getNumericValue(digits[i]) * (2 + i);
        }

        for (int numero : totals) {
            total += numero;
        }

        int resto = total % 11;

        return String.valueOf(11 - resto);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getDocumentSting() {
        return "RG: " + number;
    }

}
