package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Intersection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ElemMatrice {
    private double cout;
    private List<Intersection> intersections;

    public ElemMatrice(double cout, List<Intersection> intersections) {
        this.cout = cout;
        this.intersections = intersections;
    }

}
