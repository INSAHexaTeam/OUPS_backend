package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.model.*;
import fr.insa.hexanome.OUPS.model.dto.CarteDTO;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import fr.insa.hexanome.OUPS.services.CalculItineraire;
import fr.insa.hexanome.OUPS.services.EtatType;
import fr.insa.hexanome.OUPS.services.GestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carte")
public class GestionController {
    GestionService service;
    public GestionController(GestionService service) {
        this.service = service;
    }


    @PostMapping("/charger")
    public ResponseEntity<CarteDTO> charger(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        Carte carte = this.service.chargerCarteDepuisXML(etatType, fichier, cheminVersFichier);
        return ResponseEntity.ok(carte.toDTO());
    }

    @PostMapping("/livraisons")
    public ResponseEntity<?> livraisons(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        try {
            Livraisons livraisons = this.service.chargerLivraisonsDepuisXML(etatType, fichier, cheminVersFichier);
            return ResponseEntity.ok(livraisons.toDTO());
        } catch (Exception e) {
            // Créer un objet de réponse d'erreur personnalisé contenant le nom de l'exception et le message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getClass().getSimpleName()); // Nom de l'exception
            errorResponse.put("message", e.getMessage()); // Message de l'exception

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    //todo : peut être retourné un objet Tournée (pas encore ajouté au modèle)
    @PostMapping("/calculerItineraire")
    public  ResponseEntity<LivraisonsDTO>
    calculerItineraire(
            @RequestBody LivraisonsDTO request
    ) throws ParserConfigurationException, IOException, SAXException {
        Entrepot entrepot = Entrepot.builder()
                .heureDepart(request.getEntrepot().getHeureDepart())
                .intersection(request.getEntrepot().getIntersection())
                .build();
        Livraisons livraisons = request.toLivraison();
        CalculItineraire calculItineraireAleatoire = CalculItineraire.builder()
                .entrepots(entrepot)
                .pointDeLivraisons(livraisons)
                .build();
        Livraisons livraisonsAleatoires = calculItineraireAleatoire.getPointsDeLivraisonAleatoire();
        return ResponseEntity.ok(livraisonsAleatoires.toDTO());
    }


}
