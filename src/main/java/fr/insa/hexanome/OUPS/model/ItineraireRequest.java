package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ItineraireRequest {
    private Entrepot entrepot;
    private LivraisonsDTO livraisons;

}
