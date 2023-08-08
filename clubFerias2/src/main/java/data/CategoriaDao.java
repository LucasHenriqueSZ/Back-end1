package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.Categoria;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDao {

    private static CategoriaDao instance;

    private final String FILE_PATH = "dados/categorias.json";

    private final Type CATEGORIAS_LIST_TYPE = new TypeToken<ArrayList<Categoria>>() {
    }.getType();

    private Gson gson;

    private CategoriaDao() {
        try {
            gson = new Gson();

            File file = new File(FILE_PATH);
            if (!file.exists())
                file.createNewFile();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao criar arquivo: " + e.getMessage());
        }
    }

    public static CategoriaDao getInstance() {
        if (instance == null) {
            instance = new CategoriaDao();
        }
        return instance;
    }

    public void salvar(Categoria categoria) {
        try {
            if (categoria == null)
                throw new IllegalArgumentException("Categoria não pode ser nula!");

            List<Categoria> categorias = carregarCategorias();
            if (verificaExistenciaCategoria(categoria, categorias))
                throw new IllegalArgumentException("Categoria já existe!");
            categorias.add(categoria);

            salvarListaCategorias(categorias);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao salvar categoria: " + e.getMessage());
        }
    }

    public Optional<Categoria> buscarCategoria(String nomeCategoria) {
        try {
            List<Categoria> categorias = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");

            for (Categoria s : categorias) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria))
                    return Optional.of(s);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Categoria> buscarCategoriaCodigo(String codigo) {
        try {
            List<Categoria> categorias = carregarCategorias();
            if (codigo == null || codigo.isEmpty())
                throw new IllegalArgumentException("Código da categoria não pode ser nulo ou vazio!");

            for (Categoria s : categorias) {
                if (s.getCodigo().equalsIgnoreCase(codigo))
                    return Optional.of(s);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void atualizar(Categoria categoriaAtualizada, String nomeCategoria) {
        try {
            List<Categoria> categorias = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
            if (categoriaAtualizada == null)
                throw new IllegalArgumentException("Categoria não pode ser nula!");
            if (categoriaAtualizada.getNome().isEmpty())
                throw new IllegalArgumentException("Nome da categoria a ser atualizada não pode ser vazio!");

            for (Categoria s : categorias) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
                    if (verificaExistenciaCategoria(categoriaAtualizada, categorias))
                        throw new IllegalArgumentException("Categoria já existe!");

                    categorias.remove(s);
                    categorias.add(categoriaAtualizada);

                    categoriaAtualizada.setCodigo(s.getCodigo());

                    salvarListaCategorias(categorias);
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
            List<Categoria> categorias = carregarCategorias();
            if (nomeCategoria == null || nomeCategoria.isEmpty())
                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");

            for (Categoria s : categorias) {
                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
                    categorias.remove(s);

                    salvarListaCategorias(categorias);
                    return;
                }
            }
            throw new IllegalArgumentException("Carteirinha não encontrada!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
        }
    }

    public List<Categoria> listarTodos() {
        return carregarCategorias();
    }

    private void salvarListaCategorias(List<Categoria> categorias) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        writer.write(gson.toJson(categorias, CATEGORIAS_LIST_TYPE));
        writer.close();
    }

    private boolean verificaExistenciaCategoria(Categoria categoria, List<Categoria> categorias) {
        for (Categoria s : categorias) {
            if (s.getNome().equalsIgnoreCase(categoria.getNome()))
                return true;
        }
        return false;
    }

    private List<Categoria> carregarCategorias() {
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
