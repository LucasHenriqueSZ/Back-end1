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

    private RecuperadorRegistros<CategoriaEspaco> recuperadorRegistros;

    private GravadorRegistros<CategoriaEspaco> gravadorRegistros;

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
    public void salvar(CategoriaEspaco entity) throws IOException {
        List<CategoriaEspaco> categorias = recuperadorRegistros.lerArquivo();

        categorias.add(entity);

        gravadorRegistros.salvarLista(categorias);
    }

    @Override
    public void atualizar(CategoriaEspaco entity) throws IOException {
        List<CategoriaEspaco> categorias = recuperadorRegistros.lerArquivo();

        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getCodigo().equals(entity.getCodigo())) {
                categorias.set(i, entity);
                break;
            }
        }

        gravadorRegistros.salvarLista(categorias);
    }

    @Override
    public void deletar(CategoriaEspaco entity) throws IOException {
        List<CategoriaEspaco> categorias = recuperadorRegistros.lerArquivo();

        categorias.removeIf(categoria -> categoria.getCodigo().equals(entity.getCodigo()));

        gravadorRegistros.salvarLista(categorias);
    }

    @Override
    public Optional<CategoriaEspaco> buscarPorCodigo(String codigo) throws IOException {
        List<CategoriaEspaco> categorias = recuperadorRegistros.lerArquivo();

        return categorias.stream()
                .filter(categoria -> categoria.getCodigo().equals(codigo))
                .findFirst();
    }

    public Optional<CategoriaEspaco> buscarPorNome(String nomeCategoria) throws IOException {
        List<CategoriaEspaco> categorias = recuperadorRegistros.lerArquivo();

        return categorias.stream()
                .filter(categoria -> categoria.getNome().equalsIgnoreCase(nomeCategoria))
                .findFirst();
    }

    @Override
    public List<CategoriaEspaco> buscarTodos() throws IOException {
        return recuperadorRegistros.lerArquivo();
    }



//
//    public void salvar(CategoriaEspaco categoriaEspaco) {
//        try {
//            if (categoriaEspaco == null)
//                throw new IllegalArgumentException("Categoria não pode ser nula!");
//
//            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
//            if (verificaExistenciaCategoria(categoriaEspaco, categoriaEspacos))
//                throw new IllegalArgumentException("Categoria já existe!");
//            categoriaEspacos.add(categoriaEspaco);
//
//            salvarListaCategorias(categoriaEspacos);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao salvar categoria: " + e.getMessage());
//        }
//    }
//
//    public Optional<CategoriaEspaco> buscarCategoria(String nomeCategoria) {
//        try {
//            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
//            if (nomeCategoria == null || nomeCategoria.isEmpty())
//                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
//
//            for (CategoriaEspaco s : categoriaEspacos) {
//                if (s.getNome().equalsIgnoreCase(nomeCategoria))
//                    return Optional.of(s);
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
//        }
//        return Optional.empty();
//    }
//
//    public Optional<CategoriaEspaco> buscarCategoriaCodigo(String codigo) {
//        try {
//            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
//            if (codigo == null || codigo.isEmpty())
//                throw new IllegalArgumentException("Código da categoria não pode ser nulo ou vazio!");
//
//            for (CategoriaEspaco s : categoriaEspacos) {
//                if (s.getCodigo().equalsIgnoreCase(codigo))
//                    return Optional.of(s);
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
//        }
//        return Optional.empty();
//    }
//
//    public void atualizar(CategoriaEspaco categoriaEspacoAtualizada, String nomeCategoria) {
//        try {
//            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
//            if (nomeCategoria == null || nomeCategoria.isEmpty())
//                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
//            if (categoriaEspacoAtualizada == null)
//                throw new IllegalArgumentException("Categoria não pode ser nula!");
//            if (categoriaEspacoAtualizada.getNome().isEmpty())
//                throw new IllegalArgumentException("Nome da categoria a ser atualizada não pode ser vazio!");
//
//            for (CategoriaEspaco s : categoriaEspacos) {
//                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
//                    if (verificaExistenciaCategoria(categoriaEspacoAtualizada, categoriaEspacos))
//                        throw new IllegalArgumentException("Categoria já existe!");
//
//                    categoriaEspacos.remove(s);
//                    categoriaEspacos.add(categoriaEspacoAtualizada);
//
//                    categoriaEspacoAtualizada.setCodigo(s.getCodigo());
//
//                    salvarListaCategorias(categoriaEspacos);
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha durante o update: " + e.getMessage());
//        }
//        throw new IllegalArgumentException("Categoria não encontrada!");
//    }
//
//    public void deletar(String nomeCategoria) {
//        try {
//            List<CategoriaEspaco> categoriaEspacos = carregarCategorias();
//            if (nomeCategoria == null || nomeCategoria.isEmpty())
//                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
//
//            for (CategoriaEspaco s : categoriaEspacos) {
//                if (s.getNome().equalsIgnoreCase(nomeCategoria)) {
//                    categoriaEspacos.remove(s);
//
//                    salvarListaCategorias(categoriaEspacos);
//                    return;
//                }
//            }
//            throw new IllegalArgumentException("Carteirinha não encontrada!");
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
//        }
//    }
//
//    public List<CategoriaEspaco> listarTodos() {
//        return carregarCategorias();
//    }
//
//    private void salvarListaCategorias(List<CategoriaEspaco> categoriaEspacos) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
//
//        writer.write(gson.toJson(categoriaEspacos, CATEGORIAS_LIST_TYPE));
//        writer.close();
//    }
//
//    private boolean verificaExistenciaCategoria(CategoriaEspaco categoriaEspaco, List<CategoriaEspaco> categoriaEspacos) {
//        for (CategoriaEspaco s : categoriaEspacos) {
//            if (s.getNome().equalsIgnoreCase(categoriaEspaco.getNome()))
//                return true;
//        }
//        return false;
//    }
//
//    private List<CategoriaEspaco> carregarCategorias() {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
//            StringBuilder arquivoJson = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                arquivoJson.append(line);
//            }
//            reader.close();
//            if (arquivoJson.toString().isEmpty())
//                return new ArrayList<>();
//
//            return gson.fromJson(arquivoJson.toString(), CATEGORIAS_LIST_TYPE);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao carregar os dados: " + e.getMessage());
//        }
//    }
}
