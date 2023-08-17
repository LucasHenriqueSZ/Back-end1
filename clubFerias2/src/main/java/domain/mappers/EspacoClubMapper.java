package domain.mappers;

import application.dto.CategoriaEspacoDto;
import application.dto.EspacoClubDto;
import domain.services.util.ExceptionsMessages.ExceptionsCategoriaEspacoMessages;
import infrastructure.CategoriaEspacoDao;
import infrastructure.entities.CategoriaEspaco;
import infrastructure.entities.EspacoClub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EspacoClubMapper {

    public static EspacoClub mapToEntity(EspacoClubDto espacoClubDto) {
        EspacoClub espacoClub = new EspacoClub(espacoClubDto.getNome(), espacoClubDto.getDescricao(),
                espacoClubDto.getLotacaoMaxima(), new HashSet<>(), espacoClubDto.getCodigo());
        if (espacoClubDto.getCategorias() != null) {
            for (CategoriaEspacoDto categoria : espacoClubDto.getCategorias()) {
                if (categoria.getCodigo() == null) {
                    CategoriaEspaco categoriaOptional = CategoriaEspacoDao.getInstance().buscarPorNome(categoria.getNome()).orElseThrow(
                            () -> new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NAO_ENCONTRADA.getMensagem() + " :" + categoria.getNome())
                    );
                    espacoClub.addCategoria(categoriaOptional.getCodigo());
                } else {
                    espacoClub.addCategoria(CategoriaEspacoMapper.mapToEntity(categoria).getCodigo());
                }
            }
        }
        return espacoClub;
    }

    public static EspacoClubDto mapToDto(EspacoClub espacoClub) {
        EspacoClubDto espacoClubDto = new EspacoClubDto(espacoClub.getNome(), espacoClub.getDescricao(), espacoClub.getLotacaoMaxima(),
                new HashSet<>(), espacoClub.getCodigo());

        if (espacoClub.getCategorias() != null) {
            for (String categoria : espacoClub.getCategorias()) {
                espacoClubDto.addCategoria(CategoriaEspacoMapper.mapToDto(CategoriaEspacoDao.getInstance().buscarPorCodigo(categoria).orElseThrow(
                                () -> new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NAO_ENCONTRADA.getMensagem() + " :" + categoria)
                        )
                ));
            }
        }
        return espacoClubDto;
    }

    public static List<EspacoClubDto> mapToDtoList(List<EspacoClub> espacos) {
        List<EspacoClubDto> espacoClubDtos = new ArrayList<>();
        for (EspacoClub espaco : espacos) {
            espacoClubDtos.add(mapToDto(espaco));
        }
        return espacoClubDtos;
    }
}
