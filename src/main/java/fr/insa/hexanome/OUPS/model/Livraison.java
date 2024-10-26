package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.LivraisonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Livraison {
    private Intersection intersection;
    private boolean estUneLivraison;

    public LivraisonDTO toDTO() {
        return LivraisonDTO.builder()
                .intersection(intersection != null ? intersection.toDTO() : null)
                .estUneLivraison(estUneLivraison)
                .build();
    }

    public static Livraison fromDTO(LivraisonDTO dto) {
        if (dto == null) return null;
        return Livraison.builder()
                .intersection(Intersection.fromDTO(dto.getIntersection()))
                .estUneLivraison(dto.isEstUneLivraison())
                .build();
    }
    @Override
    public String toString() {
        return "Livraison{" +
                "intersection=" + (intersection != null ? intersection.getId() : "null") +
                ", estUneLivraison=" + estUneLivraison +
                '}';
    }

}