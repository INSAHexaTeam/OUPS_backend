package fr.insa.hexanome.OUPS.servicetests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@SpringBootTest
class FabriquePaterneTest {

    @Test
    void telechargerXml() throws Exception {
        //XML valide
        String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
        File xmlFile = new File(filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList noeuds = doc.getElementsByTagName("noeud");
        assertEquals(308, noeuds.getLength(), "all noeuds telecharges");

        // Verifier noeud
        Element firstNoeud = (Element) noeuds.item(0);
        assertEquals("25175791", firstNoeud.getAttribute("id"), "id correct");
        assertEquals("45.75406", firstNoeud.getAttribute("latitude"), "latitude correct");
        assertEquals("4.857418", firstNoeud.getAttribute("longitude"), "longitude correct");
    }

    @Test
    void jf() {
        int result = 8+3;
        assertEquals(11, result, "8 + 3 egale 11");
    }@Test
    void test2() {
        int result = 8+3;
        assertEquals(11, result, "8 + 3 egale 11");
    }
    @Test
    void test3() {
        int result = 8*3;
        assertEquals(24, result, "8 * 3 egale 24");
    }
}


