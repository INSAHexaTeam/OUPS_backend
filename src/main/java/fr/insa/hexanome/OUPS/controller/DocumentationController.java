package fr.insa.hexanome.OUPS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DocumentationController {
    @GetMapping("/documentation/javadoc")
    public String redirectToJavadoc() {
        return "redirect:/apidocs/index.html"; // Redirige vers la page index.html de la Javadoc
    }
    @GetMapping
    public String redirectToIndex() {
        return "redirect:/apidocs/index.html";
    }
}
