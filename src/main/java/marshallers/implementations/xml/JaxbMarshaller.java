package marshallers.implementations.xml;

import marshallers.interfaces.XmlMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JaxbMarshaller implements XmlMarshaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbMarshaller.class);

    @Override
    public <E> void marshall(E entity, String fileName) {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, file);
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public <E> E unmarshall(Class<E> classToken, String fileName) {
        try {
            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(classToken);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (E)unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
