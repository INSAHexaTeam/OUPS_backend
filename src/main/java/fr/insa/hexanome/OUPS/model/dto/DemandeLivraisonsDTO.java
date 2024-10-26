package fr.insa.hexanome.OUPS.model.dto;

import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.DemandeLivraisons;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class DemandeLivraisonsDTO {
    private EntrepotDTO entrepot;
    private List<LivraisonDTO> livraisons;
    private Integer coursier;  // optionnel, selon vos besoins


    // Méthode utilitaire pour convertir en objet du modèle si nécessaire
    public DemandeLivraisons toDemandeLivraisons() {
        DemandeLivraisons result = DemandeLivraisons.builder().build();
        result.setEntrepot(Entrepot.fromDTO(this.entrepot));
        if (this.livraisons != null) {
            result.addAll(this.livraisons.stream()
                    .map(Livraison::fromDTO)
                    .collect(Collectors.toList()));
        }
        return result;
    }


}
