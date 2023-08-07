package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import domain.socio.Socio;
import domain.socio.documentos.Documento;
import gson.InterfaceAdapter;
import gson.LocalDateAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioDao {

    private static final String FILE_PATH = "socios.json";

    private static final Type SOCIOS_LIST_TYPE = new TypeToken<ArrayList<Socio>>() {
    }.getType();

    private Gson gson;

    public SocioDao() {
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

    public void salvar(Socio socio) {
        try {
            List<Socio> socios = carregarSocios();
            if (socio == null)
                throw new IllegalArgumentException("Socio não pode ser nulo!");
            if (!socio.getDocumento().validarDocumento())
                throw new IllegalArgumentException("Documento inválido!");
            if (verificaExistenciaSocio(socio, socios))
                throw new IllegalArgumentException("Socio ja cadastrado!");

            socios.add(socio);

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            writer.write(gson.toJson(socios, SOCIOS_LIST_TYPE));
            writer.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar o cadastro: " + e.getMessage());
        }
    }

    public List<Socio> carregarSocios() {
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

            return gson.fromJson(arquivoJson.toString(), SOCIOS_LIST_TYPE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                if (s.getDocumento().getNumero().equals(documentoOuNome) || s.getNome().equals(documentoOuNome))
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

                    BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

                    writer.write(gson.toJson(socios, SOCIOS_LIST_TYPE));
                    writer.close();
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

                    BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

                    writer.write(gson.toJson(socios, SOCIOS_LIST_TYPE));
                    writer.close();
                    break;
                }
            }
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

    private boolean verificaExistenciaDocumento(Socio socio, List<Socio> socios) {
        for (Socio s : socios) {
            if (s.getDocumento().getNumero().equals(socio.getDocumento().getNumero()))
                return true;
        }
        return false;
    }

    private boolean verificaExistenciaSocio(Socio socio, List<Socio> socios) {
        for (Socio s : socios) {
            if (s.getDocumento().getNumero().equals(socio.getDocumento().getNumero()) || s.getNome().equals(socio.getNome()))
                return true;
        }
        return false;
    }
}
