package infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecuperadorRegistros<T> {

    private String filePath;

    private Gson gson;

    private final Type LIST_TYPE;

    public RecuperadorRegistros(String filePath, Gson gson, Class clazz) {
        this.filePath = filePath;
        this.gson = gson;
        LIST_TYPE = TypeToken.getParameterized(List.class, clazz).getType();
    }

    public List<T> lerArquivo() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder arquivoJson = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            arquivoJson.append(line);
        }
        reader.close();
        if (arquivoJson.toString().isEmpty())
            return new ArrayList<>();

        return gson.fromJson(arquivoJson.toString(), LIST_TYPE);
    }
}
