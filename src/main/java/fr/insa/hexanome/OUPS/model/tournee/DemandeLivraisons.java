package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.dto.DemandeLivraisonsDTO;
import lombok.*;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class DemandeLivraisons extends ArrayList<Livraison> {
    private Entrepot entrepot;

    public DemandeLivraisonsDTO toDTO() {
        return DemandeLivraisonsDTO.builder()
                .entrepot(entrepot.toDTO())
                .livraisons(this.stream()
                        .map(Livraison::toDTO)
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }
}