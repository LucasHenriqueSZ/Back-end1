package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.Espaco;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EspacoDao {

    private static EspacoDao instance;

    private final String FILE_PATH = "dados/espacos.json";

    private final Type ESPACO_LIST_TYPE = new TypeToken<ArrayList<Espaco>>() {
    }.getType();

    private Gson gson;

    private EspacoDao() {
        try {
            gson = new Gson();

            File file = new File(FILE_PATH);
            if (!file.exists())
                file.createNewFile();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao criar arquivo: " + e.getMessage());
        }
    }

    public static EspacoDao getInstance() {
        if (instance == null) {
            instance = new EspacoDao();
        }
        return instance;
    }

    public void salvar(Espaco espaco) {
        try {
            if (espaco == null)
                throw new IllegalArgumentException("Espaco não pode ser nulo!");
            if (!validarCategoriaEspaco(espaco))
                throw new IllegalArgumentException("Categoria do espaco não existe!");

            List<Espaco> espacos = carregarEspacos();
            if (verificaExistenciaEspaco(espaco, espacos))
                throw new IllegalArgumentException("Espaco já existe!");
            espacos.add(espaco);

            salvarListaEspacos(espacos);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao salvar espaco: " + e.getMessage());
        }
    }

    private void salvarListaEspacos(List<Espaco> espacos) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        writer.write(gson.toJson(espacos, ESPACO_LIST_TYPE));
        writer.close();
    }

    private boolean verificaExistenciaEspaco(Espaco espaco, List<Espaco> espacos) {
        for (Espaco s : espacos) {
            if (s.getNome().equalsIgnoreCase(espaco.getNome()))
                return true;
        }
        return false;
    }

    private List<Espaco> carregarEspacos() {
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

            return gson.fromJson(arquivoJson.toString(), ESPACO_LIST_TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao carregar os dados: " + e.getMessage());
        }
    }

    private boolean validarCategoriaEspaco(Espaco espaco) {
        CategoriaDao categoriaDao = CategoriaDao.getInstance();
        Set<String> categorias = espaco.getCategorias();

        for (String categoria : categorias) {
            if (!categoriaDao.buscarCategoriaCodigo(categoria).isPresent())
                return false;
        }
        return true;
    }
}
