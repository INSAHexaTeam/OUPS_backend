package fr.insa.hexanome.OUPS.model;

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

    public List<Intersection> trouverIntersectionPourSegment(Long idOrigine, Long idDestination){
        Intersection origine = null;
        Intersection destination = null;
        int founded = 0;
        int i = 0;
        while(founded<2) {
            Intersection intersection = this.intersections.get(i);
            if (Objects.equals(intersection.getId(), idOrigine)) {
                origine = intersection;
                founded++;
            }
            if (Objects.equals(intersection.getId(), idDestination)) {
                destination = intersection;
                founded++;
            }
            i++;
        }
        if (origine == null || destination == null) {
            throw new IllegalArgumentException("Intersection non trouvée");
        }

        List<Intersection> intersections = new ArrayList<>();
        intersections.add(origine);
        intersections.add(destination);
        return intersections;

    }
}
