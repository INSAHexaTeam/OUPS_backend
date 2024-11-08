package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Carte;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.tsp.Graph;
import lombok.Getter;
import org.hibernate.Cache;

import java.util.List;
import java.util.Map;

@Getter
public class TSPGraph implements Graph {
    private final List<Livraison> livraisons;
    private final Carte carte;
    private final int PRECISION = 1000;

    public TSPGraph(List<Livraison> livraisons, Carte carte) {
        this.livraisons = livraisons;
        this.carte = carte;
        // Faire un dijkstra de tous les points de livraisons

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
        //Utiliser le dijsktra

        return (int) (depart.getVoisins().get(positionDansListVoisin).getLongueur()*this.PRECISION);

    }

    @Override
    public boolean isArc(int i, int j) {
        Intersection depart = this.livraisons.get(i).getIntersection();
        Intersection arrive = this.livraisons.get(j).getIntersection();
        int positionDansListVoisin = depart.aPourVoisin(arrive);
        return positionDansListVoisin != -1;

    }

    public List<Intersection> getSolution() {
        for(int i = 0;i<livraisons.size();i++){
            //calcul de solution

        }
        return null;
    }

}

