package domain.mappers;

import application.dto.socioDto.documentosDto.CpfDto;
import application.dto.socioDto.documentosDto.DocumentoDto;
import application.dto.socioDto.documentosDto.RgDto;
import infrastructure.entities.socio.documentos.Cpf;
import infrastructure.entities.socio.documentos.Documento;
import infrastructure.entities.socio.documentos.Rg;

public class DocumentoMapper {

    public static DocumentoDto mapToDto(Documento documento) {
        if (documento.getClass().getSimpleName().equals("Rg")) {
            return new RgDto(documento.getNumero());
        } else if (documento.getClass().getSimpleName().equals("Cpf")) {
            return new CpfDto(documento.getNumero());
        }
        return null;
    }

    public static Documento mapToEntity(DocumentoDto documento) {
        if (documento.getClass().getSimpleName().equals("RgDto")) {
            return new Rg(documento.getNumero());
        } else if (documento.getClass().getSimpleName().equals("CpfDto")) {
            return new Cpf(documento.getNumero());
        }
        return null;
    }
}
