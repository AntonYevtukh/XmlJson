package serializers.implementations.xml;

import serializers.interfaces.XmlSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class JaxbSerializer<E> implements XmlSerializer<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbSerializer.class);

    @Override
    public String serialize(E entity) throws IOException {
        String result = null;
        try (Writer writer = new BufferedWriter(new StringWriter())){
            JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public E deserialize(Class<E> classToken, String fileBody) throws IOException {
        try (Reader reader = new BufferedReader(new StringReader(fileBody))){
            JAXBContext jaxbContext = JAXBContext.newInstance(classToken);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (E)unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
