package serializers.implementations.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import serializers.interfaces.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JacksonSerializer implements JsonSerializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GsonSerializer.class);

    static {
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public <E> String serialize(E entity) throws IOException {
        String result = null;
        try (Writer writer = new BufferedWriter(new StringWriter())) {
            OBJECT_MAPPER.writeValue(writer, entity);
            result = writer.toString();
        }
        System.out.println(result);
        return result;
    }

    @Override
    public <E> E deserialize(Class<E> classToken, String fileBody) throws IOException {
       return OBJECT_MAPPER.readValue(fileBody, classToken);
    }
}
