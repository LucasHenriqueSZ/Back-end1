package domain.mappers;

import application.dto.socioDto.SocioDto;
import infrastructure.entities.socio.Socio;

import java.util.ArrayList;
import java.util.List;

public class SocioMapper {
    public static SocioDto mapToDto(Socio socio) {
        SocioDto socioDto = new SocioDto();
        socioDto.setNome(socio.getNome());
        socioDto.setCarteirinha(socio.getCarteirinha());
        socioDto.setDataAssociacao(socio.getDataAssociacao());
        socioDto.setDocumento(DocumentoMapper.mapToDto(socio.getDocumento()));

        return socioDto;
    }

    public static Socio mapToEntity(SocioDto socioDto) {
        Socio socio = new Socio();
        socio.setNome(socioDto.getNome());
        socio.setCarteirinha(socioDto.getCarteirinha());
        socio.setDataAssociacao(socioDto.getDataAssociacao());
        socio.setDocumento(DocumentoMapper.mapToEntity(socioDto.getDocumento()));

        return socio;
    }

    public static List<SocioDto> mapToDtoList(List<Socio> socios) {
        List<SocioDto> socioDtoList = new ArrayList<>();

        for (Socio socio : socios) {
            SocioDto socioDto = mapToDto(socio);
            socioDtoList.add(socioDto);
        }

        return socioDtoList;
    }
}
