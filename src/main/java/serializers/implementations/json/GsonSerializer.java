package serializers.implementations.json;

import adapters.LocalDateJsonDeserializer;
import adapters.LocalDateJsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import serializers.interfaces.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class GsonSerializer<E> implements JsonSerializer<E> {

    private static final Gson GSON;
    private static final Logger LOGGER = LoggerFactory.getLogger(GsonSerializer.class);

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .setPrettyPrinting()
                .setDateFormat("dd.MM.yyyy")
                .registerTypeAdapter(LocalDate.class, new LocalDateJsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer());
        GSON = gsonBuilder.create();
    }

    @Override
    public String serialize(E entity) {
        return GSON.toJson(entity);
    }

    @Override
    public E deserialize(Class<E> classToken, String fileBody) {
        return GSON.fromJson(fileBody, classToken);
    }
}
