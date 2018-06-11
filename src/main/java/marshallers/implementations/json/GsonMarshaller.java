package marshallers.implementations.json;

import adapters.LocalDateJsonDeserializer;
import adapters.LocalDateJsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import marshallers.interfaces.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;

public class GsonMarshaller implements JsonMarshaller {

    private static final Gson GSON;
    private static final Logger LOGGER = LoggerFactory.getLogger(GsonMarshaller.class);

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
    public <E> void marshall(E entity, String fileName) {
        String jsonString = GSON.toJson(entity);
        try(PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            printWriter.write(jsonString);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public <E> E unmarshall(Class<E> classToken, String fileName) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return GSON.fromJson(reader, classToken);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
