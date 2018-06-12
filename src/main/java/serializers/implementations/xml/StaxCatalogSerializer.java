package serializers.implementations.xml;

import entities.Catalog;
import entities.Notebook;
import entities.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import serializers.interfaces.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StaxCatalogSerializer implements XmlSerializer<Catalog> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaxCatalogSerializer.class);
    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    static {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String serialize(Catalog catalog) throws IOException {
        Document document = documentBuilder.newDocument();
        Element catalogElement = document.createElement("catalog");
        document.appendChild(catalogElement);
        Element notebookElement = document.createElement("notebook");
        catalogElement.appendChild(notebookElement);
        for (Person person: catalog.getNotebook().getPersons()) {
            notebookElement.appendChild(serializePerson(person, document));
        }
        document.normalizeDocument();
        DOMSource domSource = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        try {
            transformer.transform(domSource, new StreamResult(stringWriter));
        } catch (TransformerException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return stringWriter.getBuffer().toString();
    }

    @Override
    public Catalog deserialize(Class<Catalog> classToken, String fileBody) throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(fileBody.getBytes()))) {
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();
            Element catalogElement = document.getDocumentElement();
            checkElementName(catalogElement, "catalog");
            Node notebookNode = catalogElement.getElementsByTagName("notebook").item(0);
            Element notebookElement = (Element) notebookNode;
            checkElementName(notebookElement, "notebook");
            NodeList persons = notebookElement.getElementsByTagName("person");
            List<Person> personList = new ArrayList<>();
            for (int i = 0; i < persons.getLength(); i++) {
                Node personNode = persons.item(i);
                Element personElement = (Element) personNode;
                checkElementName(personElement, "person");
                personList.add(parsePerson(personElement));
            }
            Notebook notebook = new Notebook(personList);
            Catalog catalog = new Catalog(notebook);
            return catalog;
        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Cannot parse document");
        }

    }

    private void checkElementName(Element element, String elementName) {
        if (!elementName.equals(element.getNodeName()))
            throw new RuntimeException("Can't read element \"" + elementName + "\"");
    }

    private Person parsePerson(Element personElement) {
        Person person = new Person();
        person.setId(Long.parseLong(personElement.getAttribute("id")));
        person.setName(personElement.getElementsByTagName("name").item(0).getTextContent());
        person.setAddress(personElement.getElementsByTagName("address").item(0).getTextContent());
        person.setCash(Long.parseLong(personElement.getElementsByTagName("cash").item(0).getTextContent()));
        person.setEducation(personElement.getElementsByTagName("education").item(0).getTextContent());
        return person;
    }

    private Element serializePerson(Person person, Document document) {
        Element personElement = document.createElement("person");
        Attr idAttr = document.createAttribute("id");
        idAttr.setValue(String.valueOf(person.getId()));
        personElement.setAttributeNode(idAttr);
        Element nameElement = document.createElement("name");
        personElement.appendChild(nameElement);
        nameElement.appendChild(document.createTextNode(person.getName()));
        Element addressElement = document.createElement("address");
        personElement.appendChild(addressElement);
        addressElement.appendChild(document.createTextNode(person.getAddress()));
        Element cashElement = document.createElement("cash");
        cashElement.appendChild(document.createTextNode(String.valueOf(person.getCash())));
        personElement.appendChild(cashElement);
        Element educationElement = document.createElement("education");
        educationElement.appendChild(document.createTextNode(person.getEducation()));
        personElement.appendChild(educationElement);
        return personElement;
    }
}
