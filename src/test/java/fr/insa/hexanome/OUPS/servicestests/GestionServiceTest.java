package fr.insa.hexanome.OUPS.servicestests;

import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import fr.insa.hexanome.OUPS.services.GestionService;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static fr.insa.hexanome.OUPS.services.EtatType.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestionServiceTest {

    private final String CHEMIN_DEFAULT = "src/main/resources/fichiersXMLPickupDelivery/";
    private GestionService gestionService;
    private MultipartFile mockMultipartFilePlan;
    private MultipartFile mockMultipartFileLivraisons;

    @BeforeEach
    public void creerGestion() {
        gestionService = new GestionService();
        Path path = Paths.get("src/test/java/resourcesTest/planPourTester.xml");
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        mockMultipartFilePlan = new MockMultipartFile("planPourTester.xml",
                "planPourTester.xml", "text/xml", content);
        path = Paths.get("src/test/java/resourcesTest/livraisonsPourTester.xml");
        content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        mockMultipartFileLivraisons = new MockMultipartFile("livraisonsPourTester.xml",
                "livraisonsPourTester.xml", "text/xml", content);
    }

    @Test
    @Order(1)
    void chargerCarteEnregistrementSimple() throws ParserConfigurationException, IOException, SAXException {
        gestionService.chargerCarteDepuisXML(ENREGISTREMENT_SIMPLE,mockMultipartFilePlan,null);
        // Verifier que le fichier a eté enregistré
        Path fichier = Paths.get(CHEMIN_DEFAULT + "planPourTester.xml");
        assertTrue(Files.exists(fichier), "Le fichier a eté enregistré");
        // Suprimmer le fichier
        Files.deleteIfExists(fichier);
    }

    @Test
    @Order(2)
    void chargerCarteEnregistrement() throws ParserConfigurationException, IOException, SAXException {
        gestionService.chargerCarteDepuisXML(ENREGISTREMENT,mockMultipartFilePlan,null);
        // Verifier que le fichier a eté enregistré
        Path fichier = Paths.get(CHEMIN_DEFAULT + "planPourTester.xml");
        assertTrue(Files.exists(fichier), "Le fichier a eté enregistré");
        // Suprimmer le fichier
        Files.deleteIfExists(fichier);
    }

    @Test
    @Order(3)
    void chargerLivraisonEnregistrement() throws ParserConfigurationException, IOException, SAXException {
        gestionService.chargerCarteDepuisXML(ENREGISTREMENT,mockMultipartFileLivraisons,null);
        // Verifier que le fichier a eté enregistré
        Path fichier = Paths.get(CHEMIN_DEFAULT + "livraisonsPourTester.xml");
        assertTrue(Files.exists(fichier), "Le fichier a eté enregistré");
        // Suprimmer le fichier
        Files.deleteIfExists(fichier);
    }

    @Test
    @Order(4)
    void chargerLivraisonEnregistrementSimple() throws ParserConfigurationException, IOException, SAXException {
        gestionService.chargerCarteDepuisXML(ENREGISTREMENT_SIMPLE,mockMultipartFileLivraisons,null);
        // Verifier que le fichier a eté enregistré
        Path fichier = Paths.get(CHEMIN_DEFAULT + "livraisonsPourTester.xml");
        assertTrue(Files.exists(fichier), "Le fichier a eté enregistré");
        // Suprimmer le fichier
        Files.deleteIfExists(fichier);
    }
}
