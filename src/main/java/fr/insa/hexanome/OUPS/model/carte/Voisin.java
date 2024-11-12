package fr.insa.hexanome.OUPS.model.carte;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import lombok.*;

import java.util.ArrayList;

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

    /**
     * Trandsforme l'objet Voisin en DTO
     * @return le DTO
     */
    public VoisinDTO toDTO() {
        Intersection tempDest = Intersection.builder()
                .voisins(
                        new ArrayList<>()
                )
                .id(destination.getId())
                .longitude(destination.getLongitude())
                .latitude(destination.getLatitude())
                .build();

        IntersectionDTO voisinDuVoisin = tempDest.toDTO();


        return VoisinDTO.builder()
                .nomRue(nomRue)
                .longueur(longueur)
                .destination(voisinDuVoisin)
                .build();
    }

    /**
     *
     * @param dto Transforme l'objet VoisinDTO en un voisin
     * @return le voisin
     */
    public static Voisin fromDTO(VoisinDTO dto) {
        if (dto == null) return null;
        return Voisin.builder()
                .nomRue(dto.getNomRue())
                .longueur(dto.getLongueur())
                .destination(Intersection.fromDTO(dto.getDestination()))
                .build();
    }
}
