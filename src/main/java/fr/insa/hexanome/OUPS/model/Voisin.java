package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
}
