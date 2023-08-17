package domain.services;

import application.dto.EspacoClubDto;
import domain.mappers.EspacoClubMapper;
import domain.services.util.ExceptionsMessages.ExceptionsEspacoClubMessages;
import domain.services.util.GeradorCodigo;
import infrastructure.EspacoClubDao;
import infrastructure.RegistroUtilizacaoDao;
import infrastructure.entities.EspacoClub;

import java.util.List;
import java.util.Optional;

public class EspacoClubService {

    private static EspacoClubService instance;

    private EspacoClubService() {
    }

    public static EspacoClubService getInstance() {
        if (instance == null) {
            instance = new EspacoClubService();
        }
        return instance;
    }

    public void cadastrarEspaco(EspacoClubDto espacoClubDto) {
        try {
            espacoClubDto.setNome(tratarEspacamentoNome(espacoClubDto.getNome()));
            EspacoClub espaco = EspacoClubMapper.mapToEntity(espacoClubDto);
            espaco.setCodigo(GeradorCodigo.getCodigo());

            verificarEspaco(espaco);
            verificarEspacoJaCadastrado(espaco);

            EspacoClubDao.getInstance().salvar(espaco);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Optional<EspacoClubDto> buscarEspaco(String nome) {
        try {
            nome = tratarEspacamentoNome(nome);
            verificarNomeEspaco(nome);

            Optional<EspacoClub> espacoClub = EspacoClubDao.getInstance().buscarPorNome(nome);

            if (espacoClub.isEmpty()) {
                throw new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem());
            }

            return Optional.of(EspacoClubMapper.mapToDto(espacoClub.get()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void editarEspaco(String nome, EspacoClubDto espacoClubDto) {
        try {
            espacoClubDto.setNome(tratarEspacamentoNome(espacoClubDto.getNome()));
            verificarNomeEspaco(nome);

            EspacoClub espacoClub = EspacoClubDao.getInstance().buscarPorNome(nome).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem()));

            EspacoClub espacoAtualizado = EspacoClubMapper.mapToEntity(espacoClubDto);
            espacoAtualizado.setCodigo(espacoClub.getCodigo());

            verificarEspaco(espacoAtualizado);
            verificarEspacoJaCadastrado(espacoAtualizado);

            EspacoClubDao.getInstance().atualizar(espacoAtualizado);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void removerEspaco(String nome) {
        try {
            nome = tratarEspacamentoNome(nome);
            verificarNomeEspaco(nome);

            EspacoClub espaco = EspacoClubDao.getInstance().buscarPorNome(nome).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem()));

            verificarEspacoEmRegistroUtilizacao(espaco.getCodigo());

            EspacoClubDao.getInstance().deletar(espaco);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<EspacoClubDto> listarEspacos() {
        try {
            List<EspacoClub> espacos = EspacoClubDao.getInstance().buscarTodos();
            if (espacos.isEmpty())
                throw new IllegalArgumentException(ExceptionsEspacoClubMessages.NENHUM_ESPACO_CADASTRADO.getMensagem());

            return EspacoClubMapper.mapToDtoList(espacos);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Optional<EspacoClubDto> buscarEspacoPorCodigo(String codigoEspaco) {
        try {
            Optional<EspacoClub> espacoClub = EspacoClubDao.getInstance().buscarPorCodigo(codigoEspaco);

            if (espacoClub.isEmpty()) {
                throw new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NAO_ENCONTRADO.getMensagem());
            }

            return Optional.of(EspacoClubMapper.mapToDto(espacoClub.get()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void verificarEspacoJaCadastrado(EspacoClub espaco)  {
        List<EspacoClub> espacos = EspacoClubDao.getInstance().buscarTodos();
        for (EspacoClub espacoClub : espacos) {
            if (espacoClub.getNome().equalsIgnoreCase(espaco.getNome()) && espacoClub.getCodigo() != espaco.getCodigo())
                throw new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_JA_CADASTRADO.getMensagem());
        }
    }

    private void verificarEspaco(EspacoClub espaco) {
        if (espaco == null)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_NULO.getMensagem());
        verificarNomeEspaco(espaco.getNome());
        if (espaco.getDescricao() == null || espaco.getDescricao().trim().isEmpty())
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.DESCRICAO_INVALIDA.getMensagem());
        if (espaco.getDescricao().length() < 3)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.DESCRICAO_MINIMA.getMensagem());
        if (espaco.getDescricao().length() > 50)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.DESCRICAO_MAXIMA.getMensagem());
        if (espaco.getLotacaoMaxima() < 0)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.LOTACAO_INVALIDA.getMensagem());
        if (espaco.getCategorias().isEmpty() || espaco.getCategorias() == null)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.CATEGORIA_INVALIDA.getMensagem());
    }

    private static void verificarNomeEspaco(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.NOME_INVALIDO.getMensagem());
        if (nome.length() < 3)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.NOME_MINIMO.getMensagem());
        if (nome.length() > 50)
            throw new IllegalArgumentException(ExceptionsEspacoClubMessages.NOME_MAXIMO.getMensagem());
    }

    private void verificarEspacoEmRegistroUtilizacao(String codigo) {
        RegistroUtilizacaoDao.getInstance().buscarTodos().forEach(registro -> {
            if (registro.getCodigoEspaco().equalsIgnoreCase(codigo))
                throw new IllegalArgumentException(ExceptionsEspacoClubMessages.ESPACO_EM_REGISTRO_UTILIZACAO.getMensagem());
        });
    }

    private String tratarEspacamentoNome(String nome) {
        nome = nome.replaceAll("\\s+", " ");
        if (nome.endsWith(" ")) {
            nome = nome.substring(0, nome.length() - 1);
        }
        return nome;
    }
}
