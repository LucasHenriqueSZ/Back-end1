package domain.mappers;

import application.dto.RegistroUtilizacaoDto;
import domain.services.EspacoClubService;
import domain.services.SocioService;
import domain.services.util.ExceptionsMessages.ExceptionsEspacoClubMessages;
import domain.services.util.ExceptionsMessages.ExceptionsSocioMessages;
import infrastructure.entities.RegistroUtilizacao;

import java.io.IOException;
import java.util.List;

public class RegistroUtilizacaoMapper {
    public static RegistroUtilizacao mapToEntity(RegistroUtilizacaoDto registroUtilizacaoDto) throws IOException {
        RegistroUtilizacao registroUtilizacao = new RegistroUtilizacao();
        registroUtilizacao.setDataUtilizacao(registroUtilizacaoDto.getDataUtilizacao());
        registroUtilizacao.setHoraSaida(registroUtilizacaoDto.getHoraSaida());
        registroUtilizacao.setHoraEntrada(registroUtilizacaoDto.getHoraEntrada());

        if (registroUtilizacaoDto.getEspacoClub().getCodigo() == null && registroUtilizacaoDto.getEspacoClub().getNome() != null) {
            registroUtilizacao.setCodigoEspaco(
                    EspacoClubMapper.mapToEntity(
                            EspacoClubService.getInstance().buscarEspaco(registroUtilizacaoDto.getEspacoClub().getNome()).orElseThrow(
                                    () -> new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem())
                            )
                    ).getCodigo()
            );
        } else {
            registroUtilizacao.setCodigoEspaco(registroUtilizacaoDto.getEspacoClub().getCodigo());
        }

        if (registroUtilizacaoDto.getSocio().getCarteirinha() == null && registroUtilizacaoDto.getSocio().getNome() != null) {
            registroUtilizacao.setCarteirinhaSocio(
                    SocioMapper.mapToEntity(
                            SocioService.getInstance().buscar(registroUtilizacaoDto.getSocio().getNome()).orElseThrow(
                                    () -> new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NAO_ENCONTRADO.getMensagem())
                            )
                    ).getCarteirinha()
            );
        } else {
            registroUtilizacao.setCarteirinhaSocio(registroUtilizacaoDto.getSocio().getCarteirinha());
        }

        return registroUtilizacao;
    }

    public static RegistroUtilizacaoDto mapToDto(RegistroUtilizacao categoriaEspaco) {
        RegistroUtilizacaoDto registroUtilizacaoDto = new RegistroUtilizacaoDto();
        registroUtilizacaoDto.setCodigoRegistro(categoriaEspaco.getCodigoRegistro());
        registroUtilizacaoDto.setDataUtilizacao(categoriaEspaco.getDataUtilizacao());
        registroUtilizacaoDto.setHoraSaida(categoriaEspaco.getHoraSaida());
        registroUtilizacaoDto.setHoraEntrada(categoriaEspaco.getHoraEntrada());

        registroUtilizacaoDto.setEspacoClub(EspacoClubService.getInstance().buscarEspacoPorCodigo(categoriaEspaco.getCodigoEspaco())
                .orElseThrow(
                        () -> new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem())
                ));

        registroUtilizacaoDto.setSocio(SocioService.getInstance().buscarPorCarteirinha(categoriaEspaco.getCarteirinhaSocio())
                .orElseThrow(
                        () -> new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NAO_ENCONTRADO.getMensagem())
                ));

        return registroUtilizacaoDto;
    }

    public static List<RegistroUtilizacaoDto> mapToDtoList(List<RegistroUtilizacao> categorias) {
        List<RegistroUtilizacaoDto> registroUtilizacaoDtos = new java.util.ArrayList<>();
        for (RegistroUtilizacao categoria : categorias) {
            registroUtilizacaoDtos.add(mapToDto(categoria));
        }
        return registroUtilizacaoDtos;
    }
}
