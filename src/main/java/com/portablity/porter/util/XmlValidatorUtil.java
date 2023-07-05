package com.portablity.porter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.portablity.porter.model.LoginRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;


@Component
public class XmlValidatorUtil {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(XmlValidatorUtil.class);

    public boolean isValidXmlFile(String xsdPath, String xmlPath) throws IOException, SAXException {
        Validator validator = initValidator(xsdPath);

        try {
            validator.validate(new StreamSource(getFile(xmlPath)));
            return true;
        } catch (SAXException e) {
            logger.error("Error while validating XML: {}", e.getMessage());
            return false;
        }

    }

    private Validator initValidator(String xsdPath) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(getFile(xsdPath));
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }

    private File getFile(String location) {
        return new File(location).getAbsoluteFile();
    }

    public String convertXmlToJson(final String xmlPath) throws JAXBException, IOException {
        // Generate code to convert POJO to XML
        JAXBContext jaxbContext = JAXBContext.newInstance(LoginRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        File file = new File(xmlPath);
        LoginRequest userRequest = (LoginRequest) unmarshaller.unmarshal(file);

        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(userRequest, stringWriter);

        XmlMapper xmlMapper = new XmlMapper();
        LoginRequest individual = xmlMapper.readValue(stringWriter.toString(), LoginRequest.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(individual);

        logger.info("Actual XML: {}", stringWriter);
        logger.info("Converted JSON : {}", json);

        StringWriter jsonStringWriter = new StringWriter(json.length());
        jsonStringWriter.write(json);

        writeFile("sampleJsonOutput.json", jsonStringWriter);

        return json;
    }

    public void writeFile(String desiredFilePath, StringWriter stringWriter) throws IOException {
        File file = new File(desiredFilePath);
        try (stringWriter; FileOutputStream fos = new FileOutputStream(file)) {

            fos.write(stringWriter.toString().getBytes());
        } catch (Exception e) {
            logger.error("Error while writing to file: {}", e.getMessage());
        }
    }

    public String convertJsonToXml(String jsonPath) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        LoginRequest loginRequest = objectMapper.readValue(new File(jsonPath), LoginRequest.class);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDefaultUseWrapper(true);
        System.out.println(xmlMapper.writeValueAsString(loginRequest));
        logger.info("Converted XML : {}", xmlMapper.writeValueAsString(loginRequest));

        return xmlMapper.writeValueAsString(loginRequest);
    }


    public Marshaller getMarshalledWithFormattedOutput(Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return marshaller;
    }
}
