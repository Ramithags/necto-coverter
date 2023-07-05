package com.portablity.porter;

import com.portablity.porter.util.XmlValidatorUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlValidatorUtilTest {

    @Test
    void shouldThrowFileNotFoundExceptionWhenFileIsNotInValidPath() {
        XmlValidatorUtil xmlValidatorUtil = new XmlValidatorUtil();
        assertThrows(FileNotFoundException.class, () -> xmlValidatorUtil.isValidXmlFile("src/main/resources/user.xsd", "src/main/resources/sampleoutput.xml"));
    }

    @Test
    void shouldReturnTrueWhenXmlIsValid() throws Exception {
        XmlValidatorUtil xmlValidatorUtil = new XmlValidatorUtil();
        assertTrue(xmlValidatorUtil.isValidXmlFile("/Users/ramithags/Documents/porter/src/main/resources/user.xsd", "/Users/ramithags/Documents/porter/sampleoutput.xml"));
    }
}
