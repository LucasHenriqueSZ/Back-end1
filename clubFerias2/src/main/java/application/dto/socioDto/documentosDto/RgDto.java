package application.dto.socioDto.documentosDto;

public class RgDto implements DocumentoDto {

    private String numero;

    public RgDto(String numero) {
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
        return "RG: " + this.numero;
    }
}
