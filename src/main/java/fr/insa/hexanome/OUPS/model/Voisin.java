package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Voisin {
    @JsonBackReference
    private Intersection destination;
    private String nomRue;
    private Double longueur;

    @Override
    public String toString() {
        return "Voisin{" +
                "destination=" + destination.getId() +
                ", nomRue='" + nomRue + '\'' +
                ", longueur=" + longueur +
                '}';
    }

    public VoisinDTO toDTO() {
        IntersectionDTO voisinDuVoisin = null;
        return VoisinDTO.builder()
                .nomRue(nomRue)
                .longueur(longueur)
                .destination(voisinDuVoisin)
                .build();
    }

    public static Voisin fromDTO(VoisinDTO dto) {
        if (dto == null) return null;
        return Voisin.builder()
                .nomRue(dto.getNomRue())
                .longueur(dto.getLongueur())
                .destination(Intersection.fromDTO(dto.getDestination()))
                .build();
    }
}
