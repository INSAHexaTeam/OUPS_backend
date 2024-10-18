package fr.insa.hexanome.OUPS.model;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voisin {
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
