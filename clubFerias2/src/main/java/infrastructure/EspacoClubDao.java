package infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import infrastructure.entities.EspacoClub;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class EspacoClubDao {

    private static EspacoClubDao instance;

    private final String FILE_PATH = "dados/espacos.json";

    private final Type ESPACO_LIST_TYPE = new TypeToken<ArrayList<EspacoClub>>() {
    }.getType();

    private Gson gson;

    private EspacoClubDao() {
        try {
            gson = new Gson();

            File file = new File(FILE_PATH);
            if (!file.exists())
                file.createNewFile();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao criar arquivo: " + e.getMessage());
        }
    }

    public static EspacoClubDao getInstance() {
        if (instance == null) {
            instance = new EspacoClubDao();
        }
        return instance;
    }

    public void salvar(EspacoClub espacoClub) {
        try {
            if (espacoClub == null)
                throw new IllegalArgumentException("Espaco não pode ser nulo!");
            if (!validarCategoriaEspaco(espacoClub))
                throw new IllegalArgumentException("Categoria do espaco não existe!");

            List<EspacoClub> espacoClubs = carregarEspacos();
            if (verificaExistenciaEspaco(espacoClub, espacoClubs))
                throw new IllegalArgumentException("Espaco já existe!");
            espacoClubs.add(espacoClub);

            salvarListaEspacos(espacoClubs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao salvar espaco: " + e.getMessage());
        }
    }

    public Optional<EspacoClub> buscarEspaco(String nome) {
        try {
            if (nome == null || nome.isEmpty())
                throw new IllegalArgumentException("Nome não pode ser nulo ou vazio!");

            List<EspacoClub> espacoClubs = carregarEspacos();
            for (EspacoClub espacoClub : espacoClubs) {
                if (espacoClub.getNome().equalsIgnoreCase(nome)) {
                    return Optional.of(espacoClub);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao buscar espaco: " + e.getMessage());
        }
    }

    public Optional<EspacoClub> buscarEspacoCodigo(String codigo) {
        try {
            if (codigo == null || codigo.isEmpty())
                throw new IllegalArgumentException("Nome não pode ser nulo ou vazio!");

            List<EspacoClub> espacoClubs = carregarEspacos();
            for (EspacoClub espacoClub : espacoClubs) {
                if (espacoClub.getCodigo().equalsIgnoreCase(codigo)) {
                    return Optional.of(espacoClub);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao buscar espaco: " + e.getMessage());
        }
    }

    public void atualizar(EspacoClub espacoClub, String nome) {
        try {
            if (espacoClub == null)
                throw new IllegalArgumentException("Espaco não pode ser nulo!");
            if (!validarCategoriaEspaco(espacoClub))
                throw new IllegalArgumentException("Categoria do espaco não existe!");

            List<EspacoClub> espacoClubs = carregarEspacos();
            for (EspacoClub s : espacoClubs) {
                if (s.getNome().equalsIgnoreCase(nome)) {
                    espacoClub.setCodigo(s.getCodigo());
                    espacoClubs.remove(s);
                    espacoClubs.add(espacoClub);
                    salvarListaEspacos(espacoClubs);
                    return;
                }
            }
            throw new IllegalArgumentException("Espaco não encontrado!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao atualizar espaco: " + e.getMessage());
        }
    }

    public void deletar(String nome) {
        try {
            if (nome == null || nome.isEmpty())
                throw new IllegalArgumentException("Nome não pode ser nulo ou vazio!");

            List<EspacoClub> espacoClubs = carregarEspacos();
            for (EspacoClub espacoClub : espacoClubs) {
                if (espacoClub.getNome().equalsIgnoreCase(nome)) {
                    espacoClubs.remove(espacoClub);
                    salvarListaEspacos(espacoClubs);
                    return;
                }
            }
            throw new IllegalArgumentException("Espaco não encontrado!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao deletar espaco: " + e.getMessage());
        }
    }

    public List<EspacoClub> listarTodos() {
        return carregarEspacos();
    }

    private void salvarListaEspacos(List<EspacoClub> espacoClubs) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        writer.write(gson.toJson(espacoClubs, ESPACO_LIST_TYPE));
        writer.close();
    }

    private boolean verificaExistenciaEspaco(EspacoClub espacoClub, List<EspacoClub> espacoClubs) {
        for (EspacoClub s : espacoClubs) {
            if (s.getNome().equalsIgnoreCase(espacoClub.getNome()))
                return true;
        }
        return false;
    }

    private List<EspacoClub> carregarEspacos() {
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

    private boolean validarCategoriaEspaco(EspacoClub espacoClub) {
        CategoriaEspacoDao categoriaEspacoDao = CategoriaEspacoDao.getInstance();
        Set<String> categorias = espacoClub.getCategorias();

        for (String categoria : categorias) {
            if (!categoriaEspacoDao.buscarCategoriaCodigo(categoria).isPresent())
                return false;
        }
        return true;
    }
}
