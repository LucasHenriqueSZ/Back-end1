package infrastructure.entities.socio.documentos;

import br.com.caelum.stella.validation.CPFValidator;
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

    @Override
    public boolean validarDocumento() {
        this.numero = this.numero.trim().replace("[^\\d]", "");
        CPFValidator cpfValidator = new CPFValidator();
        if (!cpfValidator.invalidMessagesFor(this.numero).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CPF: " +  this.numero;
    }
}
