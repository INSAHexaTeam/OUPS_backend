package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.exception.FileExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

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

@Service
public class GestionService {

    private final String CHEMIN_DEFAULT = "src/main/resources/fichiersXMLPickupDelivery/";
    private final FabriquePaterne fabriquePaterne;
    public GestionService() {
        this.fabriquePaterne = new FabriquePaterne();

    }

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
    private void enregistrement(MultipartFile fichier) throws IOException {
        //Extension fichier doit être .xml
        if (!Objects.requireNonNull(fichier.getOriginalFilename()).endsWith(".xml")){
            throw new FileExtension("Le fichier n'est pas un fichier XML");
        }
        //TODO @Wassila Reinforce tests by checking the content

        Path path = Paths.get(this.CHEMIN_DEFAULT);
        try{
            Files.copy(fichier.getInputStream(), path.resolve(Objects.requireNonNull(fichier.getOriginalFilename())));
        }catch (FileAlreadyExistsException e){
            Files.delete(path.resolve(Objects.requireNonNull(fichier.getOriginalFilename())));
            Files.copy(fichier.getInputStream(), path.resolve(Objects.requireNonNull(fichier.getOriginalFilename())));

        }
    }
}
