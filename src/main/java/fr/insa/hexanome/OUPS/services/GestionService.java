package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
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

@Service
public class GestionService {

    private final String CHEMIN_DEFAULT = "src/main/resources/fichiersXMLPickupDelivery/";
    public GestionService() {}

    public Carte chargerCarteDepuisXML(EtatType etat, MultipartFile fichier, String cheminVersFichier) throws ParserConfigurationException, IOException, SAXException {
        while(true){
            switch (etat){
                case ENREGISTREMENT -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    this.chargerCarteDepuisXML(EtatType.CHARGEMENT, null, fichier.getOriginalFilename());
                }
                case CHARGEMENT -> {
                    if (cheminVersFichier == null){
                        return null;
                    }
                    FabriquePaterne fabriquePaterne = new FabriquePaterne();
                    return fabriquePaterne.chargePlan(CHEMIN_DEFAULT + cheminVersFichier); // Fin
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
    private void enregistrement(MultipartFile fichier) throws IOException, FileNotFoundException {
        String filePath = System.getProperty("user.dir") + this.CHEMIN_DEFAULT + File.separator + fichier.getOriginalFilename();
        FileOutputStream fout = new FileOutputStream(filePath);
        fout.write(fichier.getBytes());
        fout.close();
    }

}
