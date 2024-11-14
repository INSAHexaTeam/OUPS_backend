package fr.insa.hexanome.OUPS.servicestests;

import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Voisin;
import fr.insa.hexanome.OUPS.model.tournee.DemandeLivraisons;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
class FabriquePaterneTest {

    private FabriquePaterne fabriquePaterne;
    private DemandeLivraisons livraisons;

    @BeforeEach
    public void telechargerXml() throws ParserConfigurationException, IOException, SAXException {
        fabriquePaterne = new FabriquePaterne();
        // Code excecuté avant de tout
        String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
        fabriquePaterne.chargePlan(filePath);
        System.out.println("XML plan a eté telechargé...");
        String filePathDemande = "src/main/resources/fichiersXMLPickupDelivery/myDeliverRequestTest.xml";
        livraisons = fabriquePaterne.chargerDemande(filePathDemande);
        System.out.println("XML demande a eté telechargé...");
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
        IOException exception = assertThrows(IOException.class, () -> {
            fabriquePaterne = new FabriquePaterne();
            String filePath = "mauvaiseroute";
            fabriquePaterne.chargePlan(filePath);
        });
        assertInstanceOf(FileNotFoundException.class, exception);
    }

    @Test
    void verifierFormatXML() {
        assertThrows(SAXException.class, () -> {
            fabriquePaterne = new FabriquePaterne();
            String filePath = "src/main/resources/fichiersXMLPickupDelivery/mauveseFichier.xml";
            fabriquePaterne.chargePlan(filePath);
        });
    }

    @Test
    void verifierChargerDemandeSansCarte() {
        FabriquePaterne nouvellefabriquePaterne = new FabriquePaterne();;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DemandeLivraisons l = nouvellefabriquePaterne.chargerDemande("src/main/resources/fichiersXMLPickupDelivery/myDeliverRequest.xml");
        });
        assertEquals("Carte non chargée", exception.getMessage());
    }

    @Test
    void verifierTousLivraisons() {
        assertEquals(9, livraisons.size(), "Tous les 9 livraisons ont été ajoutés");
    }

    @Test
    void verifierEntrepot() {
        Entrepot entrepot = livraisons.getEntrepot();
        assertEquals(26086307L, entrepot.getIntersection().getId(),"id intersection correct");
        assertEquals("08:00", entrepot.getHeureDepart(),"heure depart correct");
    }



}


