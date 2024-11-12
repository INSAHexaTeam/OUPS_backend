package fr.insa.hexanome.OUPS.servicestests;

import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Voisin;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Tests unitaires pour la fabriquePatterne
 */
@SpringBootTest
class FabriquePaterneTest {

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
    void verifierTousSegments() {
        int i = 0;
        for (Intersection intersection : fabriquePaterne.getCarte().getIntersections()) {
            i += intersection.getVoisins().size();
        }
        assertEquals(616, i, "Tous les 616 segments ont été ajoutés à la carte");
    }

    @Test
    void verifierVoisins() {
        //Étant donné une intersection, vérifier que ses voisins ont eté ajoutés
        Intersection origineNoeud = fabriquePaterne.getCarte().trouverIntersectionParId(25152444L);
        int i = 0;
        for (Voisin voisin : origineNoeud.getVoisins()) {
            Long destinationId = voisin.getDestination().getId();
            if (destinationId == 26576954L || destinationId == 26576955L || destinationId == 2477791932L) {
                i ++;
            };
        }
        assertEquals(3, i, "voisins ajoutés correctement");
        assertEquals(3, origineNoeud.getVoisins().size(), "tous les voisins ajoutés correctement");
    }

    @Test
    void verifierRouteFichier() {
        //TODO Exception devrait avoir un message
        IOException exception = assertThrows(IOException.class, () -> {
            fabriquePaterne = new FabriquePaterne();
            String filePath = "mauveseroute";
            fabriquePaterne.chargePlan(filePath);
        });
        assertInstanceOf(FileNotFoundException.class, exception);
    }

    @Test
    void verifierFormatXML() {
        //TODO Exception devrait avoir un message
        assertThrows(SAXException.class, () -> {
            fabriquePaterne = new FabriquePaterne();
            String filePath = "src/main/resources/fichiersXMLPickupDelivery/mauveseFichier.xml";
            fabriquePaterne.chargePlan(filePath);
        });
    }

}


