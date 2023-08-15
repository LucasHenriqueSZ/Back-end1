package application.dto.socioDto.documentosDto;
public class CpfDto implements DocumentoDto {

    private String numero;

    public CpfDto(String numero) {
        setNumero(numero);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    @Override
    public String toString() {
        return "CPF: " +  this.numero;
    }
}
