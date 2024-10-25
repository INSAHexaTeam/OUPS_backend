package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.Livraisons;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class CalculItineraire {
    List<Livraison> pointDeLivraisons;
    Entrepot entrepots;

    public Livraisons getPointsDeLivraisonAleatoire() {
       Livraisons pointsAleatoires = Livraisons.builder()
               .entrepot(entrepots)
               .build();
        List<Livraison> livraisonsAleatoires = new ArrayList<>(pointDeLivraisons);
        Collections.shuffle(livraisonsAleatoires); // Mélange les points de livraison
        pointsAleatoires.addAll(livraisonsAleatoires); // Ajoute les points de livraison mélangés

        return pointsAleatoires;
    }



}
