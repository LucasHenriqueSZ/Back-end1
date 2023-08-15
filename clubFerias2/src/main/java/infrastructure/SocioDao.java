package infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import infrastructure.entities.socio.Socio;
import infrastructure.entities.socio.documentos.Documento;
import infrastructure.adaptersJson.InterfaceAdapter;
import infrastructure.adaptersJson.LocalDateAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioDao {

    private static SocioDao instance;

    private final java.lang.String FILE_PATH = "dados/socios.json";

    private final Type SOCIOS_LIST_TYPE = new TypeToken<ArrayList<Socio>>() {
    }.getType();

    private Gson gson;

    private SocioDao() {
        try {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(Documento.class, new InterfaceAdapter())
                    .create();

            File file = new File(FILE_PATH);
            if (!file.exists())
                file.createNewFile();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao criar arquivo: " + e.getMessage());
        }
    }

    public static SocioDao getInstance() {
        if (instance == null) {
            instance = new SocioDao();
        }
        return instance;
    }

    public void salvar(Socio socio) {
        try {
            if (socio == null)
                throw new IllegalArgumentException("Socio não pode ser nulo!");
            if (!socio.getDocumento().validarDocumento())
                throw new IllegalArgumentException("Documento inválido!");

            List<Socio> socios = carregarSocios();
            if (verificaExistenciaSocio(socio, socios))
                throw new IllegalArgumentException("Socio ja cadastrado!");

            socios.add(socio);

            salvarListaSocios(socios);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar o cadastro: " + e.getMessage());
        }
    }

    public Optional<Socio> buscarPorDocumentoOuNome(String documentoOuNome) {
        try {
            List<Socio> socios = carregarSocios();
            if (documentoOuNome == null || documentoOuNome.isEmpty())
                throw new IllegalArgumentException("Nome ou documento não pode ser nulo ou vazio!");
            if (documentoOuNome.length() < 3)
                throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
            if (documentoOuNome.length() > 50)
                throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");

            for (Socio s : socios) {
                if (s.getDocumento().getNumero().equals(documentoOuNome) || s.getNome().equalsIgnoreCase(documentoOuNome))
                    return Optional.of(s);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Socio> buscaPorCarteirinha(String carteirinha){
        try {
            List<Socio> socios = carregarSocios();
            if (carteirinha == null || carteirinha.isEmpty())
                throw new IllegalArgumentException("Numero da carteirinha não pode ser nulo ou vazio!");
            if (carteirinha.length() != 8)
                throw new IllegalArgumentException("Numero da carteirinha deve conter 8 caracteres!");

            for (Socio s : socios) {
                if (s.getCarteirinha().equals(carteirinha))
                    return Optional.of(s);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void atualizar(Socio socioNovo, String carteirinha) {
        try {
            List<Socio> socios = carregarSocios();
            if (socioNovo == null)
                throw new IllegalArgumentException("Membro não pode ser nulo!");
            if (carteirinha == null || carteirinha.isEmpty())
                throw new IllegalArgumentException("Numero do cartão não pode ser nulo ou vazio!");
            if (!socioNovo.getDocumento().validarDocumento())
                throw new IllegalArgumentException("Documento inválido!");

            socioNovo.setCarteirinha(carteirinha);

            for (Socio s : socios) {
                if (s.getCarteirinha().equals(carteirinha)) {
                    if (!socioNovo.getDocumento().getNumero().equals(s.getDocumento().getNumero()))
                        if (verificaExistenciaDocumento(socioNovo, socios))
                            throw new IllegalArgumentException("Socio ja cadastrado com estes dados!");

                    socios.remove(s);
                    socios.add(socioNovo);

                    salvarListaSocios(socios);
                    return;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante o update: " + e.getMessage());
        }
        throw new IllegalArgumentException("Carteirinha não encontrada!");
    }

    public void deletar(String carteirinha) {
        try {
            List<Socio> socios = carregarSocios();
            if (carteirinha == null || carteirinha.isEmpty())
                throw new IllegalArgumentException("Numero da carteirinha não pode ser nulo ou vazio!");
            if (carteirinha.length() != 8)
                throw new IllegalArgumentException("Numero da carteirinha deve conter 8 caracteres!");

            for (Socio s : socios) {
                if (s.getCarteirinha().equals(carteirinha)) {
                    socios.remove(s);

                    salvarListaSocios(socios);
                    return;
                }
            }
            throw new IllegalArgumentException("Carteirinha não encontrada!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
        }
    }

    public List<Socio> listarTodos() {
        return carregarSocios();
    }

    public List<List<Socio>> listaPaginada(int tamamhoPagina) {
        List<Socio> socios = carregarSocios();
        List<List<Socio>> sociosPaginados = new ArrayList<>();

        for (int i = 0; i < socios.size(); i += tamamhoPagina) {
            sociosPaginados.add(socios.subList(i, Math.min(i + tamamhoPagina, socios.size())));
        }

        return sociosPaginados;
    }

    public int totalsocios() {
        List<Socio> socios = carregarSocios();
        return socios.size();
    }

    private void salvarListaSocios(List<Socio> socios) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

        writer.write(gson.toJson(socios, SOCIOS_LIST_TYPE));
        writer.close();
    }

    private List<Socio> carregarSocios() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            StringBuilder arquivoJson = new StringBuilder();
            java.lang.String line;
            while ((line = reader.readLine()) != null) {
                arquivoJson.append(line);
            }
            reader.close();
            if (arquivoJson.toString().isEmpty())
                return new ArrayList<>();

            return gson.fromJson(arquivoJson.toString(), SOCIOS_LIST_TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao carregar os dados: " + e.getMessage());
        }
    }

    private boolean verificaExistenciaDocumento(Socio socio, List<Socio> socios) {
        for (Socio s : socios) {
            if (s.getDocumento().getNumero().equals(socio.getDocumento().getNumero()))
                return true;
        }
        return false;
    }

    private boolean verificaExistenciaSocio(Socio socio, List<Socio> socios) {
        for (Socio s : socios) {
            if (s.getDocumento().getNumero().equals(socio.getDocumento().getNumero()) || s.getNome().equalsIgnoreCase(socio.getNome()))
                return true;
        }
        return false;
    }
}