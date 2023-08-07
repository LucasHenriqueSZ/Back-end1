package domain.socio.documentos;

import com.google.gson.annotations.SerializedName;

public class Rg implements Documento {

    @SerializedName("rg")
    private String numero;

    public Rg(String numero) {
        setNumero(numero);
    }

    @Override
    public boolean validarDocumento() {
        if (numero == null || numero.trim().isEmpty() || !numero.matches("[0-9X]+") || numero.length() != 9) {
            return false;
        }

        String verifiedNumber = numero.trim();

        String lastDigit = verifiedNumber.substring(verifiedNumber.length() - 1);

        if (lastDigit.equals("X")) {
            lastDigit = "10";
        } else if (lastDigit.equals("0")) {
            lastDigit = "11";
        }

        verifiedNumber = verifiedNumber.substring(0, verifiedNumber.length() - 1);

        String digito = descobrirDigito(verifiedNumber);
        return digito.equals(lastDigit);
    }

    private String descobrirDigito(String rg) {
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "RG: " +  this.numero;
    }
}
