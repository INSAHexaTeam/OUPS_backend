package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * (Objet métier) Représentation d'une intersection
 */
@Data
@Builder
public class Intersection {
    private Long id;
    private Double latitude;
    private Double longitude;
    @JsonManagedReference
    private List<Voisin> voisins;

    /**
     * (Constructeur) les voisins sont construits avec une liste vide
     * @param id Id de l'intersecton
     * @param latitude latitude du point
     * @param longitude longitude du point
     */
    public Intersection(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.voisins = new ArrayList<>();
    }

    /**
     * (Constructeur) les voisins sont construits avec une liste copiée
     * @param id Id de l'intersecton
     * @param latitude latitude du point
     * @param longitude longitude du point
     * @param voisins Liste des voisins à copier
     */
    public Intersection(Long id, Double latitude, Double longitude, List<Voisin> voisins) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.voisins = voisins;
    }

    /**
     * Permer d'ajouter un voisin a la liste des voisins
     * @param voisin élément a ajouter à la liste des voisins
     */
    public void ajouterVoisin(Voisin voisin) {
        this.voisins.add(voisin);
    }

}
