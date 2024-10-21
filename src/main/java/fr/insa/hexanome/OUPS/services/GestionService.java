package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GestionService {

    private final String CHEMIN_DEFAULT = "src/main/resources/fichiersXMLPickupDelivery/";
    public GestionService() {}

    public Carte chargerCarteDepuisXML(EtatChargement etat, MultipartFile fichier, String cheminVersFichier){
        //TODO: il faut trouver un meilleur moyen de passer le fichier et le cheminVersFichier en paramètre.
        while(true){
            switch (etat){
                case ENREGISTREMENT -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                    this.chargerCarteDepuisXML(EtatChargement.CHARGEMENT, null, fichier.getOriginalFilename());
                }
                case CHARGEMENT -> {
                    if (cheminVersFichier == null){
                        return null;
                    }
                    FabriquePaterne fabriquePaterne = new FabriquePaterne();
                    try{
                        return fabriquePaterne.chargePlan(CHEMIN_DEFAULT + cheminVersFichier);
                    }
                    catch (Exception e){
                        return null;
                    }
                }
                case ENREGISTREMENT_SIMPLE -> {
                    if (fichier==null){
                        return null;
                    }
                    this.enregistrement(fichier);
                }
            }
        }
    }
    private void enregistrement(MultipartFile fichier){
        //logique d'enregistrement du fichier
    }

}
