package infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infrastructure.adaptersJson.LocalDateAdapter;
import infrastructure.adaptersJson.LocalTimeAdapter;
import infrastructure.entities.RegistroUtilizacao;
import infrastructure.util.GravadorRegistros;
import infrastructure.util.RecuperadorRegistros;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class RegistroUtilizacaoDao implements DaoGenerico<RegistroUtilizacao> {

    private static RegistroUtilizacaoDao instance;

    private final RecuperadorRegistros<RegistroUtilizacao> recuperadorRegistros;

    private final GravadorRegistros<RegistroUtilizacao> gravadorRegistros;

    private RegistroUtilizacaoDao() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        String filePath = "dados/registrosUtilizacao.json";
        recuperadorRegistros = new RecuperadorRegistros<>(filePath, gson, RegistroUtilizacao.class);
        gravadorRegistros = new GravadorRegistros<>(filePath, gson, RegistroUtilizacao.class);
    }

    public static RegistroUtilizacaoDao getInstance() {
        if (instance == null) {
            instance = new RegistroUtilizacaoDao();
        }
        return instance;
    }

    @Override
    public void salvar(RegistroUtilizacao entity) {
        try {
            List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

            registros.add(entity);

            gravadorRegistros.salvarLista(registros);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(RegistroUtilizacao entity) {
        try {
            List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

            for (int i = 0; i < registros.size(); i++) {
                if (registros.get(i).getCodigoRegistro().equals(entity.getCodigoRegistro())) {
                    registros.set(i, entity);
                    break;
                }
            }

            gravadorRegistros.salvarLista(registros);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(RegistroUtilizacao entity) {
        try {
            List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

            registros.removeIf(registro -> registro.getCodigoRegistro().equals(entity.getCodigoRegistro()));

            gravadorRegistros.salvarLista(registros);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<RegistroUtilizacao> buscarPorCodigo(String codigo) {
        List<RegistroUtilizacao> registros = null;
        try {
            registros = recuperadorRegistros.lerArquivo();

            return registros.stream()
                    .filter(registro -> registro.getCodigoRegistro().equalsIgnoreCase(codigo))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RegistroUtilizacao> buscarTodos() {
        try {
            return recuperadorRegistros.lerArquivo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
