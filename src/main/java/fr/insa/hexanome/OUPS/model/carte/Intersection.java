package fr.insa.hexanome.OUPS.model.carte;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
public class Intersection {
    private Long id;
    private Double latitude;
    private Double longitude;
    @JsonManagedReference
    private List<Voisin> voisins;
    private final int TRAVER_SPEED = 15;

    public Intersection(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.voisins = new ArrayList<>();
    }

    public Intersection(Long id, Double latitude, Double longitude, List<Voisin> voisins) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.voisins = voisins;
    }


    public void ajouterVoisin(Voisin voisin) {
        this.voisins.add(voisin);
    }

    public IntersectionDTO toDTO() {
        return IntersectionDTO.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .voisins(this.voisins.stream()
                        .map(Voisin::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }
    public static Intersection fromDTO(IntersectionDTO dto) {
        if (dto == null) return null;
        return Intersection.builder()
                .id(dto.getId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .voisins(dto.getVoisins() != null ? dto.getVoisins().stream()
                        .map(Voisin::fromDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }
    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", voisins=" + (voisins != null ? voisins.size() : "null") +
                '}';
    }

    public int aPourVoisin(Intersection intersection){
        boolean founded = false;
        int position = -1;
        int i = 0;
        while(!founded){
            Voisin voisin = intersection.getVoisins().get(i);
            if (Objects.equals(voisin.getDestination().getId(), intersection.getId())) {
                founded = true;
                position = i;
            }
            i++;
        }
        return position;
    }
    public double getCoutEntreVoisin(Intersection intersection){
        int position = this.aPourVoisin(intersection);
        if (position == -1){
            return -1;
        }
        return this.voisins.get(position).getLongueur();
    }
}
