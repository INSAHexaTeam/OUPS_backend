package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.EntrepotDTO;
import lombok.*;

import java.util.Date;
@Data
@Builder
public class Entrepot  {
    private String heureDepart;
    private Intersection intersection;

    public EntrepotDTO toDTO() {
        return EntrepotDTO.builder()
                .heureDepart(heureDepart)
                .intersection(intersection != null ? intersection.toDTO() : null)
                .build();
    }

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
