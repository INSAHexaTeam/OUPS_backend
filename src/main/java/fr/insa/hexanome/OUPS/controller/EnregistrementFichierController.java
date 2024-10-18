package fr.insa.hexanome.OUPS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/fichier")
public class EnregistrementFichierController {
    private final String cheminFichier = "src/main/resources/fichiers/";

    @PostMapping("/enregistrer")
    public ResponseEntity<String> enregistrer(@RequestParam("fichier") MultipartFile fichier) {
        try {
            File dest = new File(cheminFichier + fichier.getOriginalFilename());
            fichier.transferTo(dest);
            return ResponseEntity.ok("Fichier enregistré avec succès : " + dest.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement du fichier");
        }
    }
}
