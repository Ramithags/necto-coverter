package com.portablity.porter;

import com.portablity.porter.model.LoginRequest;
import com.portablity.porter.util.XmlValidatorUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

@RestController
public class PorterController {

    public static final String DOCUMENTS_PORTER_SAMPLEOUTPUT_XML = "sampleoutput.xml";
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(PorterController.class);

    private final XmlValidatorUtil xmlValidatorUtil;

    @Autowired
    public PorterController(XmlValidatorUtil xmlValidatorUtil) {
        this.xmlValidatorUtil = xmlValidatorUtil;
    }

    @PostMapping(value = "/xsdtoxml", produces = "application/xml", consumes = "application/json")
    public ResponseEntity<String> xmlConverter(@RequestBody LoginRequest loginRequest) throws JAXBException, IOException {

        StringWriter stringWriter = new StringWriter();


        // Generate code to convert POJO to XML
        JAXBContext jaxbContext = JAXBContext.newInstance(LoginRequest.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

        // Write to System.out for debugging
        xmlValidatorUtil.getMarshalledWithFormattedOutput(LoginRequest.class).marshal(loginRequest, stringWriter);

        logger.info("XML: {}", stringWriter);

        xmlValidatorUtil.writeFile(DOCUMENTS_PORTER_SAMPLEOUTPUT_XML, stringWriter);

        return ResponseEntity.ok(stringWriter.toString());
    }


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateXmlWithXsd(@RequestParam("file") MultipartFile xsdFile, @RequestParam("xmlFile") MultipartFile xmlFile) throws SAXException, IOException {
        File schemaFile = xmlValidatorUtil.geFileFromMultiPart(xsdFile);
        File requestXMLFile = xmlValidatorUtil.geFileFromMultiPart(xmlFile);
        return ResponseEntity.ok(xmlValidatorUtil.isValidXmlFile(schemaFile, requestXMLFile));
    }

    @GetMapping("/jsonParser")
    public ResponseEntity<String> convertXMLToJson() throws JAXBException, IOException {
        return ResponseEntity.ok(xmlValidatorUtil.convertXmlToJson("sampleoutput.xml"));
    }

    @GetMapping(value = "/jsonToXml", produces = "application/xml")
    public ResponseEntity<String> convertJsonToXML() throws IOException {
        return ResponseEntity.ok(xmlValidatorUtil.convertJsonToXml("sampleJsonOutput.json"));
    }
}