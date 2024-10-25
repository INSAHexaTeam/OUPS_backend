package fr.insa.hexanome.OUPS.model.dto;

import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import lombok.Data;

import java.util.List;

/**
 * (DATA TO OBJECT) Cette classe permet d'afficher des Livraisons au frontend
 */
@Data
public class LivraisonsDTO {
    private Entrepot entrepot;
    private List<Livraison> livraisonList;

    public LivraisonsDTO(Entrepot entrepot, List<Livraison> livraisons) {
        this.entrepot = entrepot;
        this.livraisonList = livraisons;

    }


}
