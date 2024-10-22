package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import fr.insa.hexanome.OUPS.services.EtatType;
import fr.insa.hexanome.OUPS.services.GestionService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/carte")
public class GestionController {
    GestionService service;
    public GestionController(GestionService service) {
        this.service = service;
    }

    @PostMapping("/charger")
    public ResponseEntity<Carte> charger(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        Carte carte = this.service.chargerCarteDepuisXML(etatType, fichier, cheminVersFichier);
        return ResponseEntity.ok(carte);
    }

    @GetMapping("/livraisons")
    public ResponseEntity<LivraisonsDTO> livraisons(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        Livraisons livraisons = this.service.chargerLivraisonsDepuisXML(etatType, fichier, cheminVersFichier);
        return ResponseEntity.ok(livraisons.toDTO());
    }


}
