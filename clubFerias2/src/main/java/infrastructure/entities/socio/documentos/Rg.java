package infrastructure.entities.socio.documentos;

import com.google.gson.annotations.SerializedName;

public class Rg implements Documento {

    @SerializedName("rg")
    private String numero;

    public Rg(String numero) {
        setNumero(numero);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
