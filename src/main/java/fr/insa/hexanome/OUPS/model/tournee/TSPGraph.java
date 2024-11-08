package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Carte;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.tsp.Graph;
import fr.insa.hexanome.OUPS.tsp.TSP;
import fr.insa.hexanome.OUPS.tsp.TSP1;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Cache;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@Builder
@Getter
public class TSPGraph implements Graph {
    private final List<Livraison> livraisons;
    private final Carte carte;
    private final int PRECISION = 1000;
    private ElemMatrice[][] matrice;
    private TSP1 tsp;



    @Override
    public int getNbVertices() {
        return this.livraisons.size();
    }

    @Override
    public int getCost(int i, int j) {

        if(matrice[i][j] == null){
            return -1;
        }
        return (int) (this.matrice[i][j].getCout() * this.PRECISION);
    }

    @Override
    public boolean isArc(int i, int j) {
        return !this.matrice[i][j].getIntersections().isEmpty();
    }

    public List<Intersection> getSolution() {
        this.tsp = new TSP1();
        this.tsp.searchSolution(10000, this);
        return null;
    }

}

