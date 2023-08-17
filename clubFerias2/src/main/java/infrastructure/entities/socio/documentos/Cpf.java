package infrastructure.entities.socio.documentos;

import com.google.gson.annotations.SerializedName;

public class Cpf implements Documento {

    @SerializedName("cpf")
    private String numero;

    public Cpf(String numero) {
        setNumero(numero);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
