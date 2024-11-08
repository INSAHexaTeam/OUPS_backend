package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.carte.*;
import fr.insa.hexanome.OUPS.model.tournee.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.*;
import org.apache.commons.math3.random.RandomGenerator;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

@Data
@Builder
public class CalculItineraire {
    private Carte carte;
    private List<Livraison> livraisons;
    private ElemMatrice[][] matrice;

    public CalculItineraire(Carte carte, List<Livraison> livraisons) {
        this.carte = carte;
        this.livraisons = livraisons;
    }


    /**
     * Calcul de l'itinéraire le plus court entre deux points grâce aux intersections de la carte
     * @param depart
     * @param arrivee
     * @param carte contient la liste des points
     *
     * @return une ElemMatrice qui contient la longueur du chemin le plus court et la liste des intersections à parcourir
     */
    public static ElemMatrice dijkstraEntreDeuxPoints(Intersection depart, Intersection arrivee, Carte carte) {
        Map<Long, Double> distances = new HashMap<>();
        Map<Long, List<Intersection>> precedents = new HashMap<>();
        PriorityQueue<Intersection> queue = new PriorityQueue<>(
                Comparator.comparingDouble(i -> distances.getOrDefault(i.getId(), Double.MAX_VALUE))
        );

        // Initialize distances
        for (Long intersectionId : carte.getIntersections().stream().map(Intersection::getId).toList()) {
            distances.put(intersectionId, Double.MAX_VALUE);
        }

        // Start from depart
        distances.put(depart.getId(), 0.0);
        queue.add(depart);

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            Intersection pointCourant = queue.poll();

            if (pointCourant.getId().equals(arrivee.getId())) {
                return new ElemMatrice(distances.get(pointCourant.getId()), precedents.get(pointCourant.getId()));
            }

            // Récupérer l'intersection depuis la carte pour avoir les voisins à jour
            pointCourant = carte.trouverIntersectionParId(pointCourant.getId());

            List<Voisin> voisins = pointCourant.getVoisins();

            //SI c'est une impasse
            if (voisins == null || voisins.isEmpty()) {
                continue;
            }

            // Sinon pour chaque voisin
            for (Voisin voisin : voisins) {
                //(non null)
                if (voisin == null || voisin.getDestination() == null) {
                    continue;
                }

                Intersection voisinCourant = voisin.getDestination();
                double newDistance = distances.get(pointCourant.getId()) + voisin.getLongueur();

                if (newDistance < distances.get(voisinCourant.getId())) {
                    distances.put(voisinCourant.getId(), newDistance);
                    List<Intersection> path = new ArrayList<>(precedents.getOrDefault(pointCourant.getId(), new ArrayList<>()));
                    path.add(pointCourant);
                    precedents.put(voisinCourant.getId(), path);
                    queue.add(voisinCourant);
                }
            }
        }

        return new ElemMatrice(Double.MAX_VALUE, null);
    }


    public void calculDijkstra(){
        // Initialisation de la matrice
        this.matrice = new ElemMatrice[this.livraisons.size()][this.livraisons.size()];

        //boucle pour remplir la matrice avec les elemMatrice
        for(int i = 0; i < this.livraisons.size(); i++){
            for(int j = 0; j < this.livraisons.size(); j++){
                if(i == j){
                    this.matrice[i][j] = new ElemMatrice(0.0, new ArrayList<>());
                }else{
                    this.matrice[i][j] = dijkstraEntreDeuxPoints(this.livraisons.get(i).getIntersection(), this.livraisons.get(j).getIntersection(), this.carte);
                }
            }
        }
        System.out.println(Arrays.deepToString(this.matrice));
    }
}






