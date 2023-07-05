package com.portablity.porter;

import com.portablity.porter.model.LoginRequest;
import com.portablity.porter.util.XmlValidatorUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;

@RestController
public class PorterController {

    public static final String DOCUMENTS_PORTER_SAMPLEOUTPUT_XML = "/Users/ramithags/Documents/porter/sampleoutput.xml";
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(PorterController.class);

    private final XmlValidatorUtil xmlValidatorUtil;

    @Autowired
    public PorterController(XmlValidatorUtil xmlValidatorUtil) {
        this.xmlValidatorUtil = xmlValidatorUtil;
    }

    @GetMapping("/xsdtoxml")
    public String hello() throws JAXBException, IOException {

        StringWriter stringWriter = new StringWriter();


        LoginRequest userRequest = new LoginRequest();
        userRequest.setPassword("24545");
        userRequest.setUsername("ramith");

        // Generate code to convert POJO to XML
        JAXBContext jaxbContext = JAXBContext.newInstance(LoginRequest.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

        // Write to System.out for debugging
        xmlValidatorUtil.getMarshalledWithFormattedOutput(LoginRequest.class)
                .marshal(userRequest, stringWriter);

        logger.info("XML: {}", stringWriter);

        xmlValidatorUtil.writeFile(DOCUMENTS_PORTER_SAMPLEOUTPUT_XML, stringWriter);

        return "Hello World";
    }


    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateXmlWithXsd() throws SAXException, IOException {
        return ResponseEntity.ok(xmlValidatorUtil.isValidXmlFile("/Users/ramithags/Documents/porter/src/main/resources/user.xsd", "/Users/ramithags/Documents/porter/sampleoutput.xml"));
    }

    @GetMapping("/jsonParser")
    public ResponseEntity<String> convertXMLToJson() throws JAXBException, IOException {
        return ResponseEntity.ok(xmlValidatorUtil.convertXmlToJson("/Users/ramithags/Documents/porter/sampleoutput.xml"));
    }

    @GetMapping(value = "/jsonToXml", produces = "application/xml")
    public ResponseEntity<String> convertJsonToXML() throws IOException {
        return ResponseEntity.ok(xmlValidatorUtil.convertJsonToXml("/Users/ramithags/Documents/porter/sampleJsonOutput.json"));
    }
}