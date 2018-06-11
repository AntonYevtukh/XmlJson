package marshallers.implementations.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import marshallers.interfaces.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JacksonMarshaller implements JsonMarshaller {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GsonMarshaller.class);

    static {
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public <E> void marshall(E entity, String fileName) {
        try(PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            OBJECT_MAPPER.writeValue(printWriter, entity);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public <E> E unmarshall(Class<E> classToken, String fileName) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return OBJECT_MAPPER.readValue(reader, classToken);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
