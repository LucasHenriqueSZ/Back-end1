package domain.services;

import application.dto.RegistroUtilizacaoDto;
import domain.mappers.RegistroUtilizacaoMapper;
import domain.services.util.ExceptionsMessages.ExceptionsRegistroUtilizacaoMessages;
import domain.services.util.GeradorCodigo;
import infrastructure.RegistroUtilizacaoDao;
import infrastructure.entities.RegistroUtilizacao;

import java.util.List;
import java.util.Optional;

public class RegistroUtilizacaoService {
    private static RegistroUtilizacaoService instance;

    private RegistroUtilizacaoService() {
    }

    public static RegistroUtilizacaoService getInstance() {
        if (instance == null) {
            instance = new RegistroUtilizacaoService();
        }
        return instance;
    }

    public Optional<RegistroUtilizacaoDto> consultarRegistroUtilizacao(String codigo) {
        try {
            codigo = tratarEspacamentoString(codigo);
            verificarCodigo(codigo);

            Optional<RegistroUtilizacao> socio = RegistroUtilizacaoDao.getInstance().buscarPorCodigo(codigo);

            if (socio.isEmpty()) {
                throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.REGISTRO_UTILIZACAO_NAO_ENCONTRADO.getMensagem());
            }

            return Optional.of(RegistroUtilizacaoMapper.mapToDto(socio.get()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<RegistroUtilizacaoDto> listarRegistrosUtilizacao() {
        try {
            List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();

            if (registros.isEmpty()) {
                throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.NENHUM_REGISTRO_UTILIZACAO_CADASTRADO.getMensagem());
            }

            return RegistroUtilizacaoMapper.mapToDtoList(registros);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void removerRegistroUtilizacao(String codigoRegistro) {
        try {
            codigoRegistro = tratarEspacamentoString(codigoRegistro);
            verificarCodigo(codigoRegistro);

            RegistroUtilizacao registro = RegistroUtilizacaoDao.getInstance().buscarPorCodigo(codigoRegistro)
                    .orElseThrow(() -> new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.REGISTRO_UTILIZACAO_NAO_ENCONTRADO.getMensagem()));

            RegistroUtilizacaoDao.getInstance().deletar(registro);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void adicionarRegistroUtilizacao(RegistroUtilizacaoDto registroUtilizacaoDto) {
        try {
            RegistroUtilizacao registroUtilizacao = RegistroUtilizacaoMapper.mapToEntity(registroUtilizacaoDto);
            registroUtilizacao.setCodigoRegistro(GeradorCodigo.getCodigo());

            verificarRegistroUtilizacao(registroUtilizacao);
            verificarRegistroJaCadastrado(registroUtilizacao);

            RegistroUtilizacaoDao.getInstance().salvar(registroUtilizacao);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void editarRegistroUtilizacao(String codigoRegistro, RegistroUtilizacaoDto registroUtilizacaoDto) {
        try {
            codigoRegistro = tratarEspacamentoString(codigoRegistro);
            verificarCodigo(codigoRegistro);

            RegistroUtilizacao registroUtilizacao = RegistroUtilizacaoDao.getInstance().buscarPorCodigo(codigoRegistro).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.REGISTRO_UTILIZACAO_NAO_ENCONTRADO.getMensagem()));

            RegistroUtilizacao registroUtilizacaoAtualizado = RegistroUtilizacaoMapper.mapToEntity(registroUtilizacaoDto);
            registroUtilizacaoAtualizado.setCodigoRegistro(registroUtilizacao.getCodigoRegistro());

            verificarRegistroUtilizacao(registroUtilizacaoAtualizado);
            verificarRegistroJaCadastrado(registroUtilizacaoAtualizado);

            RegistroUtilizacaoDao.getInstance().atualizar(registroUtilizacaoAtualizado);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void verificarCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CODIGO_REGISTRO_UTILIZACAO_INVALIDO.getMensagem());
        }
        if (codigo.length() != 5) {
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CODIGO_REGISTRO_UTILIZACAO_INVALIDO.getMensagem());
        }
        if (!codigo.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CODIGO_REGISTRO_UTILIZACAO_INVALIDO.getMensagem());
        }
    }

    private void verificarRegistroJaCadastrado(RegistroUtilizacao registroUtilizacao){
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();

        for (RegistroUtilizacao registro : registros) {
            if (registro.getCarteirinhaSocio().equalsIgnoreCase(registroUtilizacao.getCarteirinhaSocio()) && registro.getCodigoEspaco().equalsIgnoreCase(registroUtilizacao.getCodigoEspaco())
                    && registro.getDataUtilizacao().equals(registroUtilizacao.getDataUtilizacao()) && registro.getHoraEntrada().equals(registroUtilizacao.getHoraEntrada()) && registro.getHoraSaida().equals(registroUtilizacao.getHoraSaida())) {
                throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.REGISTRO_UTILIZACAO_JA_CADASTRADO.getMensagem());
            }
        }
    }

    private void verificarRegistroUtilizacao(RegistroUtilizacao registroUtilizacao) {
        if (registroUtilizacao.getDataUtilizacao() == null)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.DATA_UTILIZACAO_INVALIDA.getMensagem());
        if (registroUtilizacao.getHoraEntrada() == null)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.HORA_ENTRADA_INVALIDA.getMensagem());
        if (registroUtilizacao.getHoraSaida() == null)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.HORA_SAIDA_INVALIDA.getMensagem());
        if (registroUtilizacao.getCarteirinhaSocio() == null || registroUtilizacao.getCarteirinhaSocio().isEmpty())
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CARTEIRINHA_SOCIO_INVALIDA.getMensagem());
        if (registroUtilizacao.getCarteirinhaSocio().length() != 8)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CARTEIRINHA_SOCIO_INVALIDA.getMensagem());
        if (registroUtilizacao.getCodigoEspaco() == null || registroUtilizacao.getCodigoEspaco().isEmpty())
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CODIGO_ESPACO_INVALIDO.getMensagem());
        if (registroUtilizacao.getCodigoEspaco().length() != 5)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.CODIGO_ESPACO_INVALIDO.getMensagem());
        if (registroUtilizacao.getHoraEntrada().compareTo(registroUtilizacao.getHoraSaida()) > 0)
            throw new IllegalArgumentException(ExceptionsRegistroUtilizacaoMessages.HORA_ENTRADA_MAIOR_HORA_SAIDA.getMensagem());
    }

    private String tratarEspacamentoString(String string) {
        string = string.replaceAll("\\s+", " ");
        if (string.endsWith(" ")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }
}
