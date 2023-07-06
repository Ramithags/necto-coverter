package com.portablity.porter;

import com.portablity.porter.util.XmlValidatorUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlValidatorUtilTest {

    @Test
    void shouldThrowFileNotFoundExceptionWhenFileIsNotInValidPath() {
        XmlValidatorUtil xmlValidatorUtil = new XmlValidatorUtil();
        File xsdFile = new File("/Users/ramithags/Documents/porter/src/main/resources/user.xsd");
        File xmlFile = new File("src/main/resources/sampleoutput.xml");
        assertThrows(FileNotFoundException.class, () -> xmlValidatorUtil.isValidXmlFile(xsdFile, xmlFile));
    }

    @Test
    void shouldReturnTrueWhenXmlIsValid() throws Exception {
        XmlValidatorUtil xmlValidatorUtil = new XmlValidatorUtil();
        File xsdFile = new File("/Users/ramithags/Documents/porter/src/main/resources/user.xsd");
        File xmlFile = new File("/Users/ramithags/Documents/porter/sampleoutput.xml");
        assertTrue(xmlValidatorUtil.isValidXmlFile(xsdFile, xmlFile));
    }
}
