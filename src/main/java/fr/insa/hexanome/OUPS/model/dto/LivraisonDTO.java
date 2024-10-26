package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDTO {
    private IntersectionDTO intersection;
    private boolean estUneLivraison;
}