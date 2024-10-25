package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.exception.FileExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * (Service) (Singleton) permet de gérer les différentes requête venant du frontend
 */
@Service
public class GestionService {

    private final String CHEMIN_DEFAULT = "src/main/resources/fichiersXMLPickupDelivery/";
    private final FabriquePaterne fabriquePaterne;
    public GestionService() {
        this.fabriquePaterne = new FabriquePaterne();

    }

    /**
     * Responsable du code métier permettant de charger un fichier XML. Il fait appel a la fabrique patterne pour instancier les objets
     * @param etat Etat du chargement
     * @param fichier (Nullable) fichier a enregistrer
     * @param cheminVersFichier (Nullable) chemin vers le fichier
     * @return une Carte
     * @throws ParserConfigurationException Erreur dans la formation du XML
     * @throws IOException Fichier introuvable
     * @throws SAXException Erreur dans le parse du XML
     */
    public Carte chargerCarteDepuisXML(EtatType etat, MultipartFile fichier, String cheminVersFichier) throws ParserConfigurationException, IOException, SAXException {
        while(true){
            switch (etat){
                case ENREGISTREMENT -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    return this.chargerCarteDepuisXML(EtatType.CHARGEMENT, null, fichier.getOriginalFilename());
                }
                case CHARGEMENT -> {
                    if (cheminVersFichier == null){
                        return null;
                    }
                    return  this.fabriquePaterne.chargePlan(CHEMIN_DEFAULT + cheminVersFichier); // Fin
                }
                case ENREGISTREMENT_SIMPLE -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    return null; // Fin
                }
            }
        }
    }

    /**
     * Responsable du code métier permettant de charger un fichier XML. Il fait appel a la fabrique patterne pour instancier les objets
     * @param etat Etat du chargement
     * @param fichier (Nullable) fichier a enregistrer
     * @param cheminVersFichier (Nullable) chemin vers le fichier
     * @return une Carte
     * @throws ParserConfigurationException Erreur dans la formation du XML
     * @throws IOException Fichier introuvable
     * @throws SAXException Erreur dans le parse du XML
     */
    public Livraisons chargerLivraisonsDepuisXML(EtatType etat, MultipartFile fichier, String cheminVersFichier) throws ParserConfigurationException, IOException, SAXException {
        while(true){
            switch (etat){
                case ENREGISTREMENT -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    return this.chargerLivraisonsDepuisXML(EtatType.CHARGEMENT, null, fichier.getOriginalFilename());
                }
                case CHARGEMENT -> {
                    if (cheminVersFichier == null){
                        return null;
                    }
                    return this.fabriquePaterne.chargerDemande(CHEMIN_DEFAULT + cheminVersFichier); // Fin
                }
                case ENREGISTREMENT_SIMPLE -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    return null; // Fin
                }
            }
        }
    }

    /**
     * Enregistre de manière sécurisé des fichiers XML en local
     * @param fichier fichier à enregistrer
     * @throws IOException emplacement introuvable ou trop volumineux
     */
    private void enregistrement(MultipartFile fichier) throws IOException {
    // Vérification de l'extension du fichier doit être .xml
        if (!Objects.requireNonNull(fichier.getOriginalFilename()).toLowerCase().endsWith(".xml")) {
            throw new FileExtension("Le fichier n'est pas un fichier XML");
        }

        // Limiter la taille du fichier pour éviter des attaques DoS
        if (fichier.getSize() > 20 * 1024 * 1024) { //20MB
            throw new IOException("Le fichier est trop volumineux");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
            // Désactiver les fonctionnalités potentiellement dangereuses pour prévenir les attaques XXE
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // Vérification du contenu et structure XML
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(fichier.getInputStream());
        } catch (ParserConfigurationException | SAXException e) {
            throw new FileExtension("Le contenu du fichier n'est pas un XML valide");
        }

        // Chemin sécurisé pour enregistrer le fichier
        Path path = Paths.get(this.CHEMIN_DEFAULT);
        Path destinationFile = path.resolve(Objects.requireNonNull(fichier.getOriginalFilename())).normalize();

        // Vérifier si le chemin est dans le répertoire souhaité pour éviter les attaques path traversal
        if (!destinationFile.startsWith(path)) {
            throw new SecurityException("tentative d'attaque detectée");
        }

        try {
            Files.copy(fichier.getInputStream(), destinationFile);
        } catch (FileAlreadyExistsException e) {
            Files.delete(destinationFile);
            Files.copy(fichier.getInputStream(), destinationFile);
        }
    }
}
