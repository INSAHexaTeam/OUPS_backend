package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.CarteDTO;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class Carte {
    private List<Intersection> intersections;
    public Carte() {
        this.intersections = new ArrayList<>();
    }
    public void ajouterIntersection(Intersection intersection) {
        this.intersections.add(intersection);
    }
    public Intersection trouverIntersectionParId(Long id) {
        for (Intersection intersection : this.intersections) {
            if (Objects.equals(intersection.getId(), id)) {
                return intersection;
            }
        }
        throw new IllegalArgumentException("Intersection non trouvée");
    }
    public CarteDTO toDTO(){

        ArrayList<IntersectionDTO> intersectionDTOS = new ArrayList<>();

        for (Intersection i : this.intersections) {
            ArrayList<VoisinDTO> voisinsDTOS = new ArrayList<>();
            if(i.getVoisins() == null)
            {
                continue;
            }
            for(Voisin v: i.getVoisins()){
                IntersectionDTO voisinIntersection = IntersectionDTO.builder()
                        .id(v.getDestination().getId())
                        .latitude(v.getDestination().getLatitude())
                        .longitude(v.getDestination().getLongitude())
                        .voisins(null)
                        .build();

                VoisinDTO voisinDTO = VoisinDTO.builder()
                        .destination(voisinIntersection)
                        .longueur(v.getLongueur())
                        .nomRue(v.getNomRue())
                        .build();
                voisinsDTOS.add(voisinDTO);
            }
            IntersectionDTO intersectionDTO = IntersectionDTO.builder()
                    .voisins(voisinsDTOS)
                    .id(i.getId())
                    .longitude(i.getLongitude())
                    .latitude(i.getLatitude())
                    .build();
            intersectionDTOS.add(intersectionDTO);
        }
        return CarteDTO.builder()
                .intersections(intersectionDTOS)
                .build();
    }
}
