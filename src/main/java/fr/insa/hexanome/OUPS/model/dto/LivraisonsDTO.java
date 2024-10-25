package fr.insa.hexanome.OUPS.model.dto;

import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.Livraisons;
import lombok.Data;

import java.util.List;
@Data
public class LivraisonsDTO {
    private Entrepot entrepot;
    private List<Livraison> livraisons;
    private int coursier;
    public LivraisonsDTO(Entrepot entrepot, List<Livraison> livraisons) {
        this.entrepot = entrepot;
        this.livraisons = livraisons;
    }

    public Livraisons toLivraison() {
        System.out.println("LivraisonsDTO.toLivraison avant: " + this);
        Livraisons livraisons = Livraisons.builder()
                .entrepot(entrepot)
                .build();
        livraisons.addAll(this.livraisons);
        return livraisons;
    }


}
