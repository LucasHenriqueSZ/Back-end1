package infrastructure;

import com.google.gson.Gson;
import infrastructure.entities.CategoriaEspaco;
import infrastructure.util.GravadorRegistros;
import infrastructure.util.RecuperadorRegistros;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CategoriaEspacoDao implements DaoGenerico<CategoriaEspaco> {

    private static CategoriaEspacoDao instance;

    private final RecuperadorRegistros<CategoriaEspaco> recuperadorRegistros;

    private final GravadorRegistros<CategoriaEspaco> gravadorRegistros;

    private CategoriaEspacoDao() {
        Gson gson = new Gson();
        String filePath = "dados/categorias.json";
        recuperadorRegistros = new RecuperadorRegistros<>(filePath, gson, CategoriaEspaco.class);
        gravadorRegistros = new GravadorRegistros<>(filePath, gson, CategoriaEspaco.class);
    }

    public static CategoriaEspacoDao getInstance() {
        if (instance == null) {
            instance = new CategoriaEspacoDao();
        }
        return instance;
    }

    @Override
    public void salvar(CategoriaEspaco entity) {
        List<CategoriaEspaco> categorias = null;
        try {
            categorias = recuperadorRegistros.lerArquivo();

            categorias.add(entity);

            gravadorRegistros.salvarLista(categorias);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(CategoriaEspaco entity) {
        List<CategoriaEspaco> categorias = null;
        try {
            categorias = recuperadorRegistros.lerArquivo();

            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getCodigo().equals(entity.getCodigo())) {
                    categorias.set(i, entity);
                    break;
                }
            }

            gravadorRegistros.salvarLista(categorias);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(CategoriaEspaco entity) {
        List<CategoriaEspaco> categorias = null;
        try {
            categorias = recuperadorRegistros.lerArquivo();

            categorias.removeIf(categoria -> categoria.getCodigo().equals(entity.getCodigo()));

            gravadorRegistros.salvarLista(categorias);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoriaEspaco> buscarPorCodigo(String codigo) {
        List<CategoriaEspaco> categorias = null;
        try {
            categorias = recuperadorRegistros.lerArquivo();

            return categorias.stream()
                    .filter(categoria -> categoria.getCodigo().equals(codigo))
                    .findFirst();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CategoriaEspaco> buscarPorNome(String nomeCategoria) {
        List<CategoriaEspaco> categorias = null;
        try {
            categorias = recuperadorRegistros.lerArquivo();

            return categorias.stream()
                    .filter(categoria -> categoria.getNome().equalsIgnoreCase(nomeCategoria))
                    .findFirst();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CategoriaEspaco> buscarTodos() {
        try {
            return recuperadorRegistros.lerArquivo();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
