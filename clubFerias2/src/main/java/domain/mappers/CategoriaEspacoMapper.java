package domain.mappers;

import application.dto.CategoriaEspacoDto;
import infrastructure.entities.CategoriaEspaco;

import java.util.ArrayList;
import java.util.List;

public class CategoriaEspacoMapper {
    public static CategoriaEspaco mapToEntity(CategoriaEspacoDto categoriaEspaco) {
        return new CategoriaEspaco(categoriaEspaco.getNome(), categoriaEspaco.getCodigo());
    }

    public static List<CategoriaEspacoDto> mapToDtoList(List<CategoriaEspaco> categorias) {
        List<CategoriaEspacoDto> categoriaEspacoDtos = new ArrayList<>();

        for (CategoriaEspaco categoria : categorias) {
            CategoriaEspacoDto categoriaDto = mapToDto(categoria);
            categoriaEspacoDtos.add(categoriaDto);
        }

        return categoriaEspacoDtos;
    }

    public static CategoriaEspacoDto mapToDto(CategoriaEspaco categoriaEspaco) {
        return new CategoriaEspacoDto(categoriaEspaco.getNome(), categoriaEspaco.getCodigo());
    }
//
//    public static List<CategoriaEspacoDto> mapToDtoList(List<CategoriaEspaco> categoriasEspaco) {
//        return null;
//    }
}
