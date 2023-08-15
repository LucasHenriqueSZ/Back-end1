package infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class GravadorRegistros<T> {

    private String filePath;

    private Gson gson;

    private final Type LIST_TYPE;

    public GravadorRegistros(String filePath, Gson gson, Class clazz) {
        this.filePath = filePath;
        this.gson = gson;
        LIST_TYPE = TypeToken.getParameterized(List.class, clazz).getType();
    }

    public void salvarLista(List<T> list) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write(gson.toJson(list, LIST_TYPE));
        writer.close();
    }
}
