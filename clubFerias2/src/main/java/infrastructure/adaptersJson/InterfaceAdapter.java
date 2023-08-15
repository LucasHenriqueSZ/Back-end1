package infrastructure.adaptersJson;

import com.google.gson.*;

import java.lang.reflect.Type;

//https://www.finra.org/about/technology/blog/how-to-serialize-deserialize-interfaces-in-java-using-gson
public class InterfaceAdapter implements JsonDeserializer, JsonSerializer {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String DATA = "DATA";

    public Object deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        Class klass = getObjectClass(className);
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
    }

    public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
        return jsonObject;
    }

    /****** Helper method to get the className of the object to be deserialized *****/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }

//    @Override
//    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        return null;
//    }
//
//    @Override
//    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
//        return null;
//    }

//    @Override
//    public Documento deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//            throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        if (jsonObject.has("cpf")) {
//            return context.deserialize(json, Cpf.class);
//        } else if (jsonObject.has("rg")) {
//            return context.deserialize(json, Rg.class);
//        }
//        throw new JsonParseException("Não foi possível determinar o tipo de Documento.");
//    }
//
//    @Override
//    public JsonElement serialize(Documento documento, Type type, JsonSerializationContext jsonSerializationContext) {
//        return null;
//    }
}
