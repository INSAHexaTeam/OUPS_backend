package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Intersection {
    private Long id;
    private Double latitude;
    private Double longitude;
    @JsonManagedReference
    private List<Voisin> voisins;


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

}
