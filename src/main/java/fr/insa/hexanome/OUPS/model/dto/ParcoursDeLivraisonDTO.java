package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParcoursDeLivraisonDTO {
    private List<IntersectionDTO> cheminIntersections;
    private DemandeLivraisonsDTO livraisons;
}