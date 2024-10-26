package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.DemandeLivraisonsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ItineraireRequest {
    private Entrepot entrepot;
    private DemandeLivraisonsDTO livraisons;

}
