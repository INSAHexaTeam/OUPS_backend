package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.services.GestionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestion")
public class GestionController {
    GestionService service;
    public GestionController(GestionService service) {
        this.service = service;
    }



}
