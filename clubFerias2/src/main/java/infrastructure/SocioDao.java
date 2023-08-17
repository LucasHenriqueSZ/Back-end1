package infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infrastructure.adaptersJson.InterfaceAdapter;
import infrastructure.adaptersJson.LocalDateAdapter;
import infrastructure.entities.socio.Socio;
import infrastructure.entities.socio.documentos.Documento;
import infrastructure.util.GravadorRegistros;
import infrastructure.util.RecuperadorRegistros;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SocioDao implements DaoGenerico<Socio> {

    private static SocioDao instance;

    private final RecuperadorRegistros<Socio> recuperadorRegistros;

    private final GravadorRegistros<Socio> gravadorRegistros;

    private SocioDao() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Documento.class, new InterfaceAdapter())
                .create();
        String FILE_PATH = "dados/socios.json";
        recuperadorRegistros = new RecuperadorRegistros<>(FILE_PATH, gson, Socio.class);
        gravadorRegistros = new GravadorRegistros<>(FILE_PATH, gson, Socio.class);
    }

    public static SocioDao getInstance() {
        if (instance == null) {
            instance = new SocioDao();
        }
        return instance;
    }

    @Override
    public void salvar(Socio entity) {
        try {
            List<Socio> socios = recuperadorRegistros.lerArquivo();

            socios.add(entity);

            gravadorRegistros.salvarLista(socios);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Socio entity) {
        try {
            List<Socio> socios = recuperadorRegistros.lerArquivo();

            for (int i = 0; i < socios.size(); i++) {
                if (socios.get(i).getCarteirinha().equals(entity.getCarteirinha())) {
                    socios.set(i, entity);
                    break;
                }
            }

            gravadorRegistros.salvarLista(socios);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(Socio entity) {
        try {
            List<Socio> socios = recuperadorRegistros.lerArquivo();

            socios.removeIf(socio -> socio.getCarteirinha().equals(entity.getCarteirinha()));

            gravadorRegistros.salvarLista(socios);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Socio> buscarPorCodigo(String codigo) {
        List<Socio> socios = null;
        try {
            socios = recuperadorRegistros.lerArquivo();

            return socios.stream()
                    .filter(socio -> socio.getCarteirinha().equals(codigo))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Socio> buscarTodos() {
        try {
            return recuperadorRegistros.lerArquivo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Socio> buscarPorNomeOuDocumento(String nomeOudocumento) {
        List<Socio> socios = null;
        try {
            socios = recuperadorRegistros.lerArquivo();

            return socios.stream()
                    .filter(socio -> socio.getNome().equalsIgnoreCase(nomeOudocumento) || socio.getDocumento().getNumero().equalsIgnoreCase(nomeOudocumento))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
