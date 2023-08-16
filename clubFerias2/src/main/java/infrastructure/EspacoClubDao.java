package infrastructure;

import com.google.gson.Gson;
import infrastructure.entities.EspacoClub;
import infrastructure.util.GravadorRegistros;
import infrastructure.util.RecuperadorRegistros;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EspacoClubDao implements DaoGenerico<EspacoClub> {

    private static EspacoClubDao instance;

    private RecuperadorRegistros<EspacoClub> recuperadorRegistros;

    private GravadorRegistros<EspacoClub> gravadorRegistros;

    private EspacoClubDao() {
        Gson gson = new Gson();
        String filePath = "dados/espacos.json";
        recuperadorRegistros = new RecuperadorRegistros<>(filePath, gson, EspacoClub.class);
        gravadorRegistros = new GravadorRegistros<>(filePath, gson, EspacoClub.class);
    }

    public static EspacoClubDao getInstance() {
        if (instance == null) {
            instance = new EspacoClubDao();
        }
        return instance;
    }

    @Override
    public void salvar(EspacoClub entity) throws IOException {
        List<EspacoClub> espacos = recuperadorRegistros.lerArquivo();

        espacos.add(entity);

        gravadorRegistros.salvarLista(espacos);
    }

    @Override
    public void atualizar(EspacoClub entity) throws IOException {
        List<EspacoClub> espacos = recuperadorRegistros.lerArquivo();

        for (int i = 0; i < espacos.size(); i++) {
            if (espacos.get(i).getCodigo().equalsIgnoreCase(entity.getCodigo())) {
                espacos.set(i, entity);
                break;
            }
        }

        gravadorRegistros.salvarLista(espacos);
    }

    @Override
    public void deletar(EspacoClub entity) throws IOException {
        List<EspacoClub> espacos = recuperadorRegistros.lerArquivo();

        espacos.removeIf(espaco -> espaco.getCodigo().equalsIgnoreCase(entity.getCodigo()));

        gravadorRegistros.salvarLista(espacos);
    }

    @Override
    public Optional<EspacoClub> buscarPorCodigo(String codigo) throws IOException {
        List<EspacoClub> espacos = recuperadorRegistros.lerArquivo();

        return espacos.stream()
                .filter(espaco -> espaco.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<EspacoClub> buscarTodos() throws IOException {
        return recuperadorRegistros.lerArquivo();
    }

    public Optional<EspacoClub> buscarPorNome(String nome) throws IOException {
        List<EspacoClub> espacos = recuperadorRegistros.lerArquivo();

        return espacos.stream()
                .filter(espaco -> espaco.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }
}
