package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.tsp.Graph;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class TSPGraph implements Graph {
    private final List<Livraison> livraisons;
    private final Map<Long, Intersection> carte;
    private final int PRECISION = 1000;

    public TSPGraph(List<Livraison> livraisons, Map<Long,Intersection> carte) {
        this.livraisons = livraisons;
        this.carte = carte;

    }

    @Override
    public int getNbVertices() {
        return this.livraisons.size();
    }

    @Override
    public int getCost(int i, int j) {
        Intersection depart = this.livraisons.get(i).getIntersection();
        Intersection arrive = this.livraisons.get(j).getIntersection();


        int positionDansListVoisin = depart.aPourVoisin(arrive);
        if (positionDansListVoisin == -1){
            return -1;
        }

        return (int) (depart.getVoisins().get(positionDansListVoisin).getLongueur()*this.PRECISION);

    }

    @Override
    public boolean isArc(int i, int j) {
        Intersection depart = this.livraisons.get(i).getIntersection();
        Intersection arrive = this.livraisons.get(j).getIntersection();
        int positionDansListVoisin = depart.aPourVoisin(arrive);
        return positionDansListVoisin != -1;

    }

}

