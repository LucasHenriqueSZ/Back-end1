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

    private RecuperadorRegistros<RegistroUtilizacao> recuperadorRegistros;

    private GravadorRegistros<RegistroUtilizacao> gravadorRegistros;

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
    public void salvar(RegistroUtilizacao entity) throws IOException {
        List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

        registros.add(entity);

        gravadorRegistros.salvarLista(registros);
    }

    @Override
    public void atualizar(RegistroUtilizacao entity) throws IOException {
        List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getCodigoRegistro().equals(entity.getCodigoRegistro())) {
                registros.set(i, entity);
                break;
            }
        }

        gravadorRegistros.salvarLista(registros);
    }

    @Override
    public void deletar(RegistroUtilizacao entity) throws IOException {
        List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

        registros.removeIf(registro -> registro.getCodigoRegistro().equals(entity.getCodigoRegistro()));

        gravadorRegistros.salvarLista(registros);
    }

    @Override
    public Optional<RegistroUtilizacao> buscarPorCodigo(String codigo) throws IOException {
        List<RegistroUtilizacao> registros = recuperadorRegistros.lerArquivo();

        return registros.stream()
                .filter(registro -> registro.getCodigoRegistro().equals(codigo))
                .findFirst();
    }

    @Override
    public List<RegistroUtilizacao> buscarTodos() throws IOException {
        return recuperadorRegistros.lerArquivo();
    }


//
//    public void salvar(RegistroUtilizacao registroUtilizacao) {
//        try {
//            if (registroUtilizacao == null)
//                throw new IllegalArgumentException("Registro de utilização não pode ser nulo!");
//
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            if (verificaExistenciaRegistro(registroUtilizacao, registros))
//                throw new IllegalArgumentException("Registro de utilização já existe!");
//            if (verificaDisponibilidadeDataHoraEspaco(registroUtilizacao, registros))
//                throw new IllegalArgumentException("Data e hora já estão sendo utilizadas para este espaço!");
//
//            if (registroUtilizacao.getHoraEntrada() != null && registroUtilizacao.getHoraSaida() != null) {
//                if (registroUtilizacao.getHoraEntrada().isAfter(registroUtilizacao.getHoraSaida()))
//                    throw new IllegalArgumentException("Hora de entrada não pode ser posterior a hora de saída!");
//            }
//
//            if (verificarExistenciaSocio(registroUtilizacao))
//                throw new IllegalArgumentException("Sócio não existe!");
//
//            if (verificarExistenciaEspaco(registroUtilizacao))
//                throw new IllegalArgumentException("Espaço não existe!");
//
//            registros.add(registroUtilizacao);
//
//            salvarListaRegistros(registros);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao salvar categoria: " + e.getMessage());
//        }
//    }
//
//    public Optional<RegistroUtilizacao> buscarRegistroCodigo(String codigo) {
//        try {
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            if (codigo == null || codigo.isEmpty())
//                throw new IllegalArgumentException("Código do registro de utilização não pode ser nulo ou vazio!");
//
//            for (RegistroUtilizacao r : registros) {
//                if (r.getCodigoRegistro().equalsIgnoreCase(codigo))
//                    return Optional.of(r);
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
//        }
//        return Optional.empty();
//    }
//
//    public List<RegistroUtilizacao> buscarRegistrosCodigoEspaco(String codigoEspaco) {
//        try {
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            List<RegistroUtilizacao> registrosEspaco = new ArrayList<>();
//            if (codigoEspaco == null || codigoEspaco.isEmpty())
//                throw new IllegalArgumentException("Código do espaço não pode ser nulo ou vazio!");
//
//            for (RegistroUtilizacao r : registros) {
//                if (r.getCodigoEspaco().equalsIgnoreCase(codigoEspaco))
//                    registrosEspaco.add(r);
//            }
//            return registrosEspaco;
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
//        }
//    }
//
//    public List<RegistroUtilizacao> buscarRegistrosCarteirinhaSocio(String carteirinha) {
//        try {
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            List<RegistroUtilizacao> registrosSocio = new ArrayList<>();
//            if (carteirinha == null || carteirinha.isEmpty())
//                throw new IllegalArgumentException("Código da carteirinha não pode ser nulo ou vazio!");
//
//            for (RegistroUtilizacao r : registros) {
//                if (r.getCarteirinhaSocio().equalsIgnoreCase(carteirinha))
//                    registrosSocio.add(r);
//            }
//            return registrosSocio;
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
//        }
//    }
//
//    public void atualizar(RegistroUtilizacao registroUtilizacaoAtualizado, String codigoRegistro) {
//        try {
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            if (codigoRegistro == null || codigoRegistro.isEmpty())
//                throw new IllegalArgumentException("Código do registro de utilização que deseja atualizar não pode ser nulo ou vazio!");
//            if (registroUtilizacaoAtualizado == null)
//                throw new IllegalArgumentException("Registro de utilização não pode ser nulo!");
//
//            if (registroUtilizacaoAtualizado.getHoraEntrada() != null && registroUtilizacaoAtualizado.getHoraSaida() != null) {
//                if (registroUtilizacaoAtualizado.getHoraEntrada().isAfter(registroUtilizacaoAtualizado.getHoraSaida()))
//                    throw new IllegalArgumentException("Hora de entrada não pode ser posterior a hora de saída!");
//            }
//            if (verificarExistenciaSocio(registroUtilizacaoAtualizado))
//                throw new IllegalArgumentException("Sócio não existe!");
//
//            if (verificarExistenciaEspaco(registroUtilizacaoAtualizado))
//                throw new IllegalArgumentException("Espaço não existe!");
//
//            if (verificaExistenciaRegistro(registroUtilizacaoAtualizado, registros))
//                throw new IllegalArgumentException("Registro de utilização já existe!");
//
//            for (RegistroUtilizacao r : registros) {
//                if (r.getCodigoRegistro().equalsIgnoreCase(codigoRegistro)) {
//                    registroUtilizacaoAtualizado.setCodigoRegistro(r.getCodigoRegistro());
//
//                    registros.remove(r);
//                    registros.add(registroUtilizacaoAtualizado);
//
//                    salvarListaRegistros(registros);
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha durante o update: " + e.getMessage());
//        }
//        throw new IllegalArgumentException("Registro de utilização não encontrado!");
//    }
//
//    public void deletar(String codigo) {
//        try {
//            List<RegistroUtilizacao> registros = carregarRegistros();
//            if (codigo == null || codigo.isEmpty())
//                throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio!");
//
//            for (RegistroUtilizacao r : registros) {
//                if (r.getCodigoRegistro().equalsIgnoreCase(codigo)) {
//                    registros.remove(r);
//
//                    salvarListaRegistros(registros);
//                    return;
//                }
//            }
//            throw new IllegalArgumentException("Registro de utilização não encontrado!");
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
//        }
//    }
//
//    public List<RegistroUtilizacao> listarTodos() {
//        return carregarRegistros();
//    }
//
//    private void salvarListaRegistros(List<RegistroUtilizacao> registros) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
//
//        writer.write(gson.toJson(registros, REGISTRO_UTILIZACAO_LIST_TYPE));
//        writer.close();
//    }
//
//    private boolean verificaExistenciaRegistro(RegistroUtilizacao registro, List<RegistroUtilizacao> registros) {
//        for (RegistroUtilizacao r : registros) {
//            if (r.getCodigoRegistro().equalsIgnoreCase(registro.getCodigoRegistro()))
//                return true;
//        }
//        return false;
//    }
//
//    private boolean verificaDisponibilidadeDataHoraEspaco(RegistroUtilizacao registroUtilizacao, List<RegistroUtilizacao> registros) {
//        for (RegistroUtilizacao r : registros) {
//            if (r.getCodigoEspaco().equalsIgnoreCase(registroUtilizacao.getCodigoEspaco()) && r.getDataUtilizacao().equals(registroUtilizacao.getDataUtilizacao()))
//                if (r.getHoraEntrada().equals(registroUtilizacao.getHoraEntrada()) || r.getHoraSaida().equals(registroUtilizacao.getHoraSaida()))
//                    return true;
//        }
//        return false;
//    }
//
//    private List<RegistroUtilizacao> carregarRegistros() {
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
//            return gson.fromJson(arquivoJson.toString(), REGISTRO_UTILIZACAO_LIST_TYPE);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Falha ao carregar os dados: " + e.getMessage());
//        }
//    }
//
//    private boolean verificarExistenciaEspaco(RegistroUtilizacao registroUtilizacao) {
//        return EspacoClubDao.getInstance().buscarEspacoCodigo(registroUtilizacao.getCodigoEspaco()).isEmpty();
//    }
//
//    private boolean verificarExistenciaSocio(RegistroUtilizacao registroUtilizacao) {
//        return SocioDao.getInstance().buscaPorCarteirinha(registroUtilizacao.getCarteirinhaSocio()).isEmpty();
//    }
}
