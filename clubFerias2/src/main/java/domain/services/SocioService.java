package domain.services;

import application.dto.socioDto.SocioDto;
import domain.mappers.SocioMapper;
import domain.services.util.ExceptionsMessages.ExceptionsSocioMessages;
import domain.services.util.GeradorCodigoCarteirinha;
import infrastructure.SocioDao;
import infrastructure.entities.socio.Socio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SocioService {

    private static SocioService instance;

    private SocioService() {
    }

    public static SocioService getInstance() {
        if (instance == null) {
            instance = new SocioService();
        }
        return instance;
    }

    public Optional<SocioDto> buscar(String nomeOudocumento) {
        try {
            verificarNomeOuDocumento(nomeOudocumento);

            Optional<Socio> socio = SocioDao.getInstance().buscarPorNomeOuDocumento(nomeOudocumento);

            if (!socio.isPresent()) {
                throw new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NAO_ENCONTRADO.getMensagem());
            }

            return Optional.of(SocioMapper.mapToDto(socio.get()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<SocioDto> listarTodos() {
        try {
            List<Socio> socios = SocioDao.getInstance().buscarTodos();

            return SocioMapper.mapToDtoList(socios);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void remover(String nomeOudocumento) {
        try {
            verificarNomeOuDocumento(nomeOudocumento);

            Socio socio = SocioDao.getInstance().buscarPorNomeOuDocumento(nomeOudocumento).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NAO_ENCONTRADO.getMensagem()));

            SocioDao.getInstance().deletar(socio);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void cadastrarSocio(SocioDto socioDto) {
        try {
            Socio socio = SocioMapper.mapToEntity(socioDto);
            socio.setCarteirinha(GeradorCodigoCarteirinha.getCardCode());
            socio.setDataAssociacao(LocalDate.now());

            verificarSocio(socio);
            verificarDocumentoDuplicado(socio);
            verificarSocioJaCadastrado(socio);

            SocioDao.getInstance().salvar(socio);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void atualizar(SocioDto socioDto, String nomeOudocumento) {
        try {
            verificarNomeOuDocumento(nomeOudocumento);

            Socio socio = SocioDao.getInstance().buscarPorNomeOuDocumento(nomeOudocumento).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NAO_ENCONTRADO.getMensagem()));

            Socio socioAtualizado = SocioMapper.mapToEntity(socioDto);
            socioAtualizado.setCarteirinha(socio.getCarteirinha());
            socioAtualizado.setDataAssociacao(socio.getDataAssociacao());

            verificarSocio(socioAtualizado);
            verificarDocumentoDuplicado(socioAtualizado);
            verificarSocioJaCadastrado(socioAtualizado);

            SocioDao.getInstance().atualizar(socioAtualizado);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void verificarSocioJaCadastrado(Socio socioAtualizado) throws IOException {
        List<Socio> socios = SocioDao.getInstance().buscarTodos();

        for (Socio socio : socios) {
            if (socio.getNome().equalsIgnoreCase(socioAtualizado.getNome())) {
                throw new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_JA_CADASTRADO.getMensagem());
            }
        }
    }

    private void verificarDocumentoDuplicado(Socio socioAtualizado) throws IOException {
        List<Socio> socios = SocioDao.getInstance().buscarTodos();

        for (Socio socio : socios) {
            if (socio.getDocumento().getNumero().equalsIgnoreCase(socioAtualizado.getDocumento().getNumero())
                    && !socio.getCarteirinha().equalsIgnoreCase(socioAtualizado.getCarteirinha())) {
                throw new IllegalArgumentException(ExceptionsSocioMessages.DOCUMENTO_DUPLICADO.getMensagem());
            }
        }
    }

    private void verificarSocio(Socio socio) {
        if (socio == null)
            throw new IllegalArgumentException(ExceptionsSocioMessages.SOCIO_NULO.getMensagem());
        if (socio.getCarteirinha() == null)
            throw new IllegalArgumentException(ExceptionsSocioMessages.CARTEIRINHA_NULA.getMensagem());
        if (socio.getDataAssociacao() == null)
            throw new IllegalArgumentException(ExceptionsSocioMessages.DATA_ASSOCIACAO_NULA.getMensagem());
        if (socio.getNome() == null || socio.getNome().trim().isEmpty())
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_NULO_OU_VAZIO.getMensagem());
        if (socio.getNome().length() < 3)
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_MENOR_3_CARACTERES.getMensagem());
        if (socio.getNome().length() > 50)
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_MAIOR_50_CARACTERES.getMensagem());
        if (!socio.getNome().matches("[a-zA-Z\\s]+"))
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_DEVE_CONTER_APENAS_LETRAS.getMensagem());
        if (socio.getDocumento() == null)
            throw new IllegalArgumentException(ExceptionsSocioMessages.DOCUMENTO_NULO.getMensagem());
        if (!socio.getDocumento().validarDocumento())
            throw new IllegalArgumentException(ExceptionsSocioMessages.DOCUMENTO_INVALIDO.getMensagem());
    }


    private void verificarNomeOuDocumento(String nomeOudocumento) {
        if (nomeOudocumento == null || nomeOudocumento.isEmpty()) {
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_OU_DOCUMENTO_NULO_OU_VAZIO.getMensagem());
        }
        if (nomeOudocumento.length() < 3) {
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_OU_DOCUMENTO_MENOR_3_CARACTERES.getMensagem());
        }
        if (nomeOudocumento.length() > 50) {
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_OU_DOCUMENTO_MAIOR_50_CARACTERES.getMensagem());
        }
        if (!nomeOudocumento.matches("[a-zA-Z0-9\\s]*")) {
            throw new IllegalArgumentException(ExceptionsSocioMessages.NOME_OU_DOCUMENTO_DEVE_CONTER_APENAS_LETRAS_E_NUMEROS.getMensagem());
        }
    }
}
