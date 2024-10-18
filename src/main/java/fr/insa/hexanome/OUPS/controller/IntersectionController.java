package fr.insa.hexanome.OUPS.controller;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/plan")
public class IntersectionController {
    private final FabriquePaterne fabriquePaterne;

    public IntersectionController(FabriquePaterne fabriquePaterne) {
        this.fabriquePaterne = fabriquePaterne;
    }

    @GetMapping("/charger")
    public ResponseEntity<Carte> chargerPlan(@RequestParam("cheminFichier") String cheminFichier) {
        try {
            Carte carte = this.fabriquePaterne.chargePlan(cheminFichier);
//            System.out.printf("Carte chargée : %s\n", carte);
            return ResponseEntity.ok(carte);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
