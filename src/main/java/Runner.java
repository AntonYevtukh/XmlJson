import entities.Catalog;
import entities.CurrencyRate;
import marshallers.implementations.json.GsonMarshaller;
import marshallers.implementations.json.JacksonMarshaller;
import marshallers.implementations.xml.JaxbMarshaller;
import marshallers.interfaces.JsonMarshaller;
import marshallers.interfaces.XmlMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import url.ResponseSaver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static final String URL_STRING = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private static final String XML_FILE_NAME = new File("").getAbsolutePath() + "\\src\\main\\resources\\catalog.xml";
    private static final String JSON_FILE_NAME = new File("").getAbsolutePath() + "\\src\\main\\resources\\currencies.json";
    private static final Set<String> CURRENCY_CODES = Set.of("USD", "EUR", "RUB");

    public static void main(String[] args) {
        /*LOGGER.info("JSON demo using GSON:");
        jsonDemo(new GsonMarshaller());
        LOGGER.info("JSON demo using Jackson:");
        jsonDemo(new JacksonMarshaller());*/
        xmlDemo(new JaxbMarshaller());
    }

    private static void jsonDemo(JsonMarshaller jsonMarshaller) {
        ResponseSaver responseSaver = new ResponseSaver();
        try {
            responseSaver.saveResponse(URL_STRING, JSON_FILE_NAME);
            List<CurrencyRate> currencyRates = Arrays.asList(
                    jsonMarshaller.unmarshall(CurrencyRate[].class, JSON_FILE_NAME));
            currencyRates = currencyRates.stream().filter(
                    (CurrencyRate currencyRate) -> CURRENCY_CODES.contains(currencyRate.getCc())).
                    collect(Collectors.toList());
            LOGGER.info(currencyRates.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void xmlDemo(XmlMarshaller xmlMarshaller) {
        Catalog catalog = xmlMarshaller.unmarshall(Catalog.class, XML_FILE_NAME);
        LOGGER.info(catalog.toString());
    }
}
