package fr.insa.hexanome.OUPS.model.dto;

import fr.insa.hexanome.OUPS.model.ParcoursDeLivraison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourneeLivraisonDTO {
    public List<ParcoursDeLivraisonDTO> livraisons;
}
