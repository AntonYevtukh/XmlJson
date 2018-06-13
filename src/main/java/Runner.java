import entities.Catalog;
import entities.CurrencyRate;
import entities.Person;
import serializers.implementations.json.GsonSerializer;
import serializers.implementations.json.JacksonSerializer;
import serializers.implementations.xml.DomCatalogSerializer;
import serializers.implementations.xml.JaxbSerializer;
import serializers.interfaces.JsonSerializer;
import serializers.interfaces.XmlSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import url.ResponseSaver;
import utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static final String URL_STRING = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private static final String XML_INPUT_FILE_NAME = new File("").getAbsolutePath() + "\\src\\main\\resources\\catalog_input.xml";
    private static final String XML_OUTPUT_FILE_NAME = new File("").getAbsolutePath() + "\\src\\main\\resources\\catalog_output.xml";
    private static final String JSON_FILE_NAME = new File("").getAbsolutePath() + "\\src\\main\\resources\\currencies.json";
    private static final Set<String> CURRENCY_CODES = Set.of("USD", "EUR", "RUB");

    public static void main(String[] args) {
        LOGGER.info("JSON demo using GSON:");
        jsonDemo(new GsonSerializer<>());
        LOGGER.info("JSON demo using Jackson:");
        jsonDemo(new JacksonSerializer<>());
        LOGGER.info("XML demo using DOM parser");
        xmlDemo(new DomCatalogSerializer());
        LOGGER.info("XML demo using JAXB parser");
        xmlDemo(new JaxbSerializer<>());
    }

    private static void jsonDemo(JsonSerializer<CurrencyRate[]> jsonSerializer) {
        ResponseSaver responseSaver = new ResponseSaver();
        FileUtil fileUtil = new FileUtil();
        try {
            responseSaver.saveResponse(URL_STRING, JSON_FILE_NAME);
            String tempString = fileUtil.readFromFile(JSON_FILE_NAME);
            List<CurrencyRate> currencyRates = Arrays.asList(
                    jsonSerializer.deserialize(CurrencyRate[].class, tempString));
            currencyRates = currencyRates.stream().filter(
                    (CurrencyRate currencyRate) -> CURRENCY_CODES.contains(currencyRate.getCc())).
                    collect(Collectors.toList());
            LOGGER.info(currencyRates.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void xmlDemo(XmlSerializer<Catalog> xmlSerializer) {
        FileUtil fileUtil = new FileUtil();
        String tempString = fileUtil.readFromFile(XML_INPUT_FILE_NAME);
        try {
            Catalog catalog = xmlSerializer.deserialize(Catalog.class, tempString);
            List<Person> personList = catalog.getNotebook().getPersons();
            Person person1 = new Person(4, "Ddd", "Kyiv", 15000, "ee");
            Person person2 = new Person(5, "Eee", "Lviv", 12000, "ee");
            personList.add(person1);
            personList.add(person2);
            catalog.getNotebook().setPersons(personList);
            fileUtil.writeToFile(xmlSerializer.serialize(catalog), XML_OUTPUT_FILE_NAME);
            List<Person> filteredPersonList = personList.stream().
                    filter((Person p) -> p.getCash() >= 10000).collect(Collectors.toList());
            LOGGER.info("List of Person, that has >= 10000 of cash");
            LOGGER.info(filteredPersonList.toString());

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
