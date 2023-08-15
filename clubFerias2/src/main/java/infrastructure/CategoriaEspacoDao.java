package infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import infrastructure.entities.CategoriaEspaco;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaEspacoDao {

    private static CategoriaEspacoDao instance;

    private final String FILE_PATH = "dados/categorias.json";

    private final Type CATEGORIAS_LIST_TYPE = new TypeToken<ArrayList<CategoriaEspaco>>() {
    }.getType();

    private Gson gson;

    private CategoriaEspacoDao() {
        try {
            gson = new Gson();

            File file = new File(FILE_PATH);
            if (!file.exists())
                file.createNewFile();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao criar arquivo: " + e.getMessage());
        }
    }

    public static CategoriaEspacoDao getInstance() {
        if (instance == null) {
            instance = new CategoriaEspacoDao();
        }
        return instance;
    }

    public void salvar(CategoriaEspaco categoriaEspaco) {
        try {
            if (categoriaEspaco == null)
                throw new IllegalArgumentException("Categoria não pode ser nula!");

            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
            if (verificaExistenciaCategoria(categoriaEspaco, categoriaEspacos))
                throw new IllegalArgumentException("Categoria já existe!");
            categoriaEspacos.add(categoriaEspaco);

            salvarListaCategorias(categoriaEspacos);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao salvar categoria: " + e.getMessage());
        }
    }

    public Optional<CategoriaEspaco> buscarCategoria(String nomeCategoria) {
        try {
            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");

            for (CategoriaEspaco s : categoriaEspacos) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria))
                    return Optional.of(s);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<CategoriaEspaco> buscarCategoriaCodigo(String codigo) {
        try {
            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
            if (codigo == null || codigo.isEmpty())
                throw new IllegalArgumentException("Código da categoria não pode ser nulo ou vazio!");

            for (CategoriaEspaco s : categoriaEspacos) {
                if (s.getCodigo().equalsIgnoreCase(codigo))
                    return Optional.of(s);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void atualizar(CategoriaEspaco categoriaEspacoAtualizada, String nomeCategoria) {
        try {
            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
            if (categoriaEspacoAtualizada == null)
                throw new IllegalArgumentException("Categoria não pode ser nula!");
            if (categoriaEspacoAtualizada.getNome().isEmpty())
                throw new IllegalArgumentException("Nome da categoria a ser atualizada não pode ser vazio!");

            for (CategoriaEspaco s : categoriaEspacos) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
                    if (verificaExistenciaCategoria(categoriaEspacoAtualizada, categoriaEspacos))
                        throw new IllegalArgumentException("Categoria já existe!");

                    categoriaEspacos.remove(s);
                    categoriaEspacos.add(categoriaEspacoAtualizada);

                    categoriaEspacoAtualizada.setCodigo(s.getCodigo());

                    salvarListaCategorias(categoriaEspacos);
                    return;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante o update: " + e.getMessage());
        }
        throw new IllegalArgumentException("Categoria não encontrada!");
    }

    public void deletar(String nomeCategoria) {
        try {
            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");

            for (CategoriaEspaco s : categoriaEspacos) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
                    categoriaEspacos.remove(s);

                    salvarListaCategorias(categoriaEspacos);
                    return;
                }
            }
            throw new IllegalArgumentException("Carteirinha não encontrada!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
        }
    }

    public List<CategoriaEspaco> listarTodos() {
        return carregarCategorias();
    }

    private void salvarListaCategorias(List<CategoriaEspaco> categoriaEspacos) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        writer.write(gson.toJson(categoriaEspacos, CATEGORIAS_LIST_TYPE));
        writer.close();
    }

    private boolean verificaExistenciaCategoria(CategoriaEspaco categoriaEspaco, List<CategoriaEspaco> categoriaEspacos) {
        for (CategoriaEspaco s : categoriaEspacos) {
            if (s.getNome().equalsIgnoreCase(categoriaEspaco.getNome()))
                return true;
        }
        return false;
    }

    private List<CategoriaEspaco> carregarCategorias() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            StringBuilder arquivoJson = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                arquivoJson.append(line);
            }
            reader.close();
            if (arquivoJson.toString().isEmpty())
                return new ArrayList<>();

            return gson.fromJson(arquivoJson.toString(), CATEGORIAS_LIST_TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao carregar os dados: " + e.getMessage());
        }
    }
}
