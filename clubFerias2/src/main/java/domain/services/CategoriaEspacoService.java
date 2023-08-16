package domain.services;

import application.dto.CategoriaEspacoDto;
import domain.mappers.CategoriaEspacoMapper;
import domain.services.util.ExceptionsMessages.ExceptionsCategoriaEspacoMessages;
import domain.services.util.GeradorCodigo;
import infrastructure.CategoriaEspacoDao;
import infrastructure.EspacoClubDao;
import infrastructure.entities.CategoriaEspaco;
import infrastructure.entities.EspacoClub;

import java.io.IOException;
import java.util.List;

public class CategoriaEspacoService {

    private static CategoriaEspacoService instance;

    private CategoriaEspacoService() {
    }

    public static CategoriaEspacoService getInstance() {
        if (instance == null) {
            instance = new CategoriaEspacoService();
        }
        return instance;
    }

    public void cadastrarCategoria(CategoriaEspacoDto categoriaEspaco) {
        try {
            categoriaEspaco.setNome(tratarEspacamentoNome(categoriaEspaco.getNome()));
            CategoriaEspaco categoria = CategoriaEspacoMapper.mapToEntity(categoriaEspaco);
            categoria.setCodigo(GeradorCodigo.getCodigo());

            verificarCategoria(categoria);
            verificarCategoriaJaCadastrada(categoria);

            CategoriaEspacoDao.getInstance().salvar(categoria);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<CategoriaEspacoDto> listarCategorias() {
        try {
            List<CategoriaEspaco> categorias = CategoriaEspacoDao.getInstance().buscarTodos();

            if (categorias.isEmpty())
                throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.NENHUMA_CATEGORIA_CADASTRADA.getMensagem());

            return CategoriaEspacoMapper.mapToDtoList(categorias);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void removerCategoria(String nomeCategoria) {
        try {
            nomeCategoria = tratarEspacamentoNome(nomeCategoria);
            verificaNomeCategoria(nomeCategoria);

            CategoriaEspaco categoria = CategoriaEspacoDao.getInstance().buscarPorNome(nomeCategoria).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NAO_ENCONTRADA.getMensagem()));

            verificaEspacoUtilizandoCategoria(categoria.getCodigo());

            CategoriaEspacoDao.getInstance().deletar(categoria);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public CategoriaEspacoDto buscarCategoria(String nomeCategoria) {
        try {
            nomeCategoria = tratarEspacamentoNome(nomeCategoria);
            verificaNomeCategoria(nomeCategoria);
            CategoriaEspaco categoria = CategoriaEspacoDao.getInstance().buscarPorNome(nomeCategoria).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NAO_ENCONTRADA.getMensagem()));

            return CategoriaEspacoMapper.mapToDto(categoria);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void editarCategoria(String nomeCategoria, CategoriaEspacoDto categoriaAtualizada) {
        try {
            categoriaAtualizada.setNome(tratarEspacamentoNome(categoriaAtualizada.getNome()));
            verificaNomeCategoria(nomeCategoria);

            CategoriaEspaco categoria = CategoriaEspacoDao.getInstance().buscarPorNome(nomeCategoria).orElseThrow(
                    () -> new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NAO_ENCONTRADA.getMensagem()));

            CategoriaEspaco categoriaEspacoAtualizada = CategoriaEspacoMapper.mapToEntity(categoriaAtualizada);
            categoriaEspacoAtualizada.setCodigo(categoria.getCodigo());

            verificarCategoria(categoriaEspacoAtualizada);
            verificarCategoriaJaCadastrada(categoriaEspacoAtualizada);

            CategoriaEspacoDao.getInstance().atualizar(categoriaEspacoAtualizada);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void verificarCategoriaJaCadastrada(CategoriaEspaco categoria) throws IOException {
        List<CategoriaEspaco> categorias = CategoriaEspacoDao.getInstance().buscarTodos();

        for (CategoriaEspaco categoriaEspaco : categorias) {
            if (categoriaEspaco.getNome().equalsIgnoreCase(categoria.getNome()))
                throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_JA_CADASTRADA.getMensagem());
        }
    }

    private void verificarCategoria(CategoriaEspaco categoria) {
        if (categoria == null)
            throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_NULA.getMensagem());
        verificaNomeCategoria(categoria.getNome());
    }

    private void verificaNomeCategoria(String nomeCategoria) {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty())
            throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.NOME_CATEGORIA_NULO.getMensagem());
        if (nomeCategoria.length() < 3)
            throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.NOME_CATEGORIA_MENOR_3.getMensagem());
        if (nomeCategoria.length() > 50)
            throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.NOME_CATEGORIA_MAIOR_50.getMensagem());
        if (!nomeCategoria.matches("[a-zA-Z0-9\\s]*"))
            throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.NOME_DEVE_CONTER_APENAS_LETRAS_OU_NUMEROS.getMensagem());
    }

    private void verificaEspacoUtilizandoCategoria(String codigoCategoria) throws IOException {
        List<EspacoClub> espacos = EspacoClubDao.getInstance().buscarTodos();

        for (EspacoClub espaco : espacos) {
            if (espaco.getCategorias().contains(codigoCategoria))
                throw new IllegalArgumentException(ExceptionsCategoriaEspacoMessages.CATEGORIA_EM_USO.getMensagem());
        }
    }

    private String tratarEspacamentoNome(String nome) {
        nome = nome.replaceAll("\\s+", " ");
        if (nome.endsWith(" ")) {
            nome = nome.substring(0, nome.length() - 1);
        }
        return nome;
    }
}
