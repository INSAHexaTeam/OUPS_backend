package fr.insa.hexanome.OUPS.servicetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@SpringBootTest
class FabriquePaterneTest {

    private static Document doc;

    @BeforeAll
    public static void telechargerXml() throws ParserConfigurationException, IOException, SAXException {
        // Code excecuté avant de tout
        String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
        File xmlFile = new File(filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(xmlFile);

        System.out.println("XML a eté telechargé...");
    }


    @Test
    void verifierTousNoeuds() {
        NodeList noeuds = doc.getElementsByTagName("noeud");
        assertEquals(308, noeuds.getLength(), "all 308 noeuds telecharges");
    }

    @Test
    void verifierPremierNoeud() {
        NodeList noeuds = doc.getElementsByTagName("noeud");
        Element firstNoeud = (Element) noeuds.item(0);
        assertEquals("25175791", firstNoeud.getAttribute("id"), "id correct");
        assertEquals("45.75406", firstNoeud.getAttribute("latitude"), "latitude correct");
        assertEquals("4.857418", firstNoeud.getAttribute("longitude"), "longitude correct");
    }

    @Test
    void recupererNoeud() {
        //Etant donnes l'Id d'un noeud, recuérer
        assertEquals(1,1);
    }

    @Test
    void verifierDesArches() {
        //Etant donnes 2 noeuds, verifier s'il y a une connection
        assertEquals(1,1);
    }

    @Test
    void test3() {
        int result = 8*3;
        assertEquals(24, result, "8 * 3 egale 24");
    }
}


