package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Intersection;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.Voisin;
import fr.insa.hexanome.OUPS.model.dto.CarteDTO;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import fr.insa.hexanome.OUPS.model.exception.FileExtension;
import fr.insa.hexanome.OUPS.services.EtatType;
import fr.insa.hexanome.OUPS.services.GestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe représente le point de liaison entre le frontend et le backend
 */
@RestController
@RequestMapping("/carte")
public class GestionController {

    private final GestionService service;
    /**
     * @param service service pour la gestion des livraisons et des map
     */
    public GestionController(GestionService service) {
        this.service = service;
    }

    /**
     * Chargement des plans
     *
     * @param stringEtat etat du chargement (CHARGEMENT,ENREGISTREMENT,ENREGISTREMENT_SIMPLE)
     * @param fichier Fichier XML a charger
     * @param cheminVersFichier CheminVersFichier à charger
     */
    @Operation(
            summary = "Charger un fichier",
            description = "Charger un plan XML",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fichier chargé avec succès",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarteDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erreur de validation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileExtension.class)
                            )
                    )
            }
    )
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

    /**
     * Chargement des demandes
     *
     * @param stringEtat etat du chargement (CHARGEMENT,ENREGISTREMENT,ENREGISTREMENT_SIMPLE)
     * @param fichier Fichier XML a charger
     * @param cheminVersFichier CheminVersFichier à charger
     */
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


}
