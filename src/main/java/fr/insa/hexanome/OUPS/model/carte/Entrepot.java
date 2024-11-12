package fr.insa.hexanome.OUPS.model.carte;

import fr.insa.hexanome.OUPS.model.dto.EntrepotDTO;
import lombok.*;

/**
 *  (Objet métier) permet la manipulation de données de type entrepôt
 */

@Data
@Builder
public class Entrepot  {
    private String heureDepart;
    private Intersection intersection;

    /**
     * Transforme l'objet entrepot en DTO
     * @return l'entrepot transformée en EntrepotDTO
     */

    public EntrepotDTO toDTO() {
        return EntrepotDTO.builder()
                .heureDepart(heureDepart)
                .intersection(intersection != null ? intersection.toDTO() : null)
                .build();
    }

    /**
     * @param dto  Transforme l'objet EntrepotDTO en entrepot
     * @return l'entrepotDTO transformée en Entrepot
     */

    public static Entrepot fromDTO(EntrepotDTO dto) {
        if (dto == null) return null;
        return Entrepot.builder()
                .heureDepart(dto.getHeureDepart())
                .intersection(Intersection.fromDTO(dto.getIntersection()))
                .build();
    }
    @Override
    public String toString() {
        return "Entrepot{" +
                "heureDepart='" + heureDepart + '\'' +
                ", intersection=" + (intersection != null ? intersection.getId() : "null") +
                '}';
    }


}
