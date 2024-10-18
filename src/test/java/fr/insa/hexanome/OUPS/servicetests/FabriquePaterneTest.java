package fr.insa.hexanome.OUPS.servicetests;

import fr.insa.hexanome.OUPS.model.Intersection;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootTest
class FabriquePaterneTest {

    private static FabriquePaterne fabriquePaterne;

    @BeforeAll
    public static void telechargerXml() throws ParserConfigurationException, IOException, SAXException {
        fabriquePaterne = new FabriquePaterne();
        // Code excecuté avant de tout
        String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
        fabriquePaterne.chargePlan(filePath);
        System.out.println("XML a eté telechargé...");
    }


    @Test
    void verifierTousNoeuds() {
        assertEquals(308, fabriquePaterne.getCarte().getIntersections().size(), "Tous les 308 noeuds ont été ajoutés à la carte");
    }


    @Test
    void verifierPremierNoeud() {
        Intersection premierNoeud = fabriquePaterne.getCarte().getIntersections().getFirst();
        assertEquals(25175791L, premierNoeud.getId(), "id correct");
        assertEquals(45.75406, premierNoeud.getLatitude(), "latitude correct");
        assertEquals(4.857418, premierNoeud.getLongitude(), "longitude correct");
    }

    @Test
    void verifierIntersection() {
        Intersection premierNoeud = fabriquePaterne.getCarte().getIntersections().getFirst();
        assertEquals(25175778L, premierNoeud.getVoisins().getFirst(), "id correct");
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

}


