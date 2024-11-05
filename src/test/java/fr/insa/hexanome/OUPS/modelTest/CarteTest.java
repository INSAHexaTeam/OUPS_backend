package fr.insa.hexanome.OUPS.modelTest;

import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarteTest {

    private FabriquePaterne fabriquePaterne;

    @BeforeEach
    public void telechargerXml() throws ParserConfigurationException, IOException, SAXException {
        fabriquePaterne = new FabriquePaterne();
        // Code excecuté avant de tout
        String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
        fabriquePaterne.chargePlan(filePath);
        System.out.println("XML a eté telechargé...");
    }

    @Test
    void verifierIntersectionExistante() {
        Intersection intersection = fabriquePaterne.getCarte().trouverIntersectionParId(1703401805L);
        assertEquals(1703401805L, intersection.getId(), "id correct");
        assertEquals(45.749405, intersection.getLatitude(), "latitude correct");
        assertEquals(4.866139, intersection.getLongitude(), "longitude correct");
    }

    @Test
    void verifierIntersectionNonExistante() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fabriquePaterne.getCarte().trouverIntersectionParId(1000L);
        });
        assertEquals("Intersection non trouvée", exception.getMessage());
    }
/*
    @Test
    void trouverSegmentExistant() {
        List<Intersection> intersections = fabriquePaterne.getCarte().trouverIntersectionPourSegment(25175791L,25175778L);
        assertEquals(25175791L, intersections.get(0).getId(), "id correct");
        assertEquals(45.75406, intersections.get(0).getLatitude(), "latitude correct");
        assertEquals(4.857418, intersections.get(0).getLongitude(), "longitude correct");
        assertEquals(25175778L, intersections.get(1).getId(), "id correct");
        assertEquals(45.75343, intersections.get(1).getLatitude(), "latitude correct");
        assertEquals(4.8574653, intersections.get(1).getLongitude(), "longitude correct");
    }


    @Test
    void trouverSegmentNonExistant() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fabriquePaterne.getCarte().trouverIntersectionPourSegment(1000L,25175778L);
        });
        assertEquals("Intersection non trouvée", exception.getMessage());
    }

*/

}
