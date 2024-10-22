package fr.insa.hexanome.OUPS.model.dto;

import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import lombok.Data;

import java.util.List;
@Data
public class LivraisonsDTO {
    private Entrepot entrepot;
    private List<Livraison> livraisonList;

    public LivraisonsDTO(Entrepot entrepot, List<Livraison> livraisons) {
        this.entrepot = entrepot;
        this.livraisonList = livraisons;

    }


}
