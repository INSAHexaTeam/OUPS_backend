package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.carte.Voisin;
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
    List<Livraison> pointDeLivraisons;
    Entrepot entrepots;

private void verifierCarte(Map<Long, Intersection> carte) {
    System.out.println("Vérification de la carte :");
    System.out.println("Nombre d'intersections : " + carte.size());

    for (Map.Entry<Long, Intersection> entry : carte.entrySet()) {
        Intersection intersection = entry.getValue();
        List<Voisin> voisins = intersection.getVoisins();
        System.out.println("Intersection " + intersection.getId() +
                " a " + (voisins != null ? voisins.size() : "null") + " voisins");

        if (voisins != null) {
            for (Voisin voisin : voisins) {
                if (voisin.getDestination() == null) {
                    System.out.println("  - Voisin sans destination !");
                    continue;
                }
                if (!carte.containsKey(voisin.getDestination().getId())) {
                    System.out.println("  - Voisin " + voisin.getDestination().getId() +
                            " non trouvé dans la carte !");
                }
            }
        }
    }
}


    public TourneeLivraison getPointsDeLivraisonClusterOptimiseOrdreDefini(int nbCoursier, Map<Long, Intersection> carte) {
        if (nbCoursier == 0) {
            nbCoursier = 2;
        }

//        verifierCarte(carte);

        // Convertir les points de livraison en objets Clusterable
        List<ClusterableLivraison> clusterableLivraisons = new ArrayList<>();
        for (Livraison livraison : pointDeLivraisons) {
            clusterableLivraisons.add(new ClusterableLivraison(livraison));
        }

        // Appliquer l'algorithme K-means pour regrouper les livraisons
        KMeansPlusPlusClusterer<ClusterableLivraison> clusterer = new KMeansPlusPlusClusterer<>(nbCoursier);
        List<CentroidCluster<ClusterableLivraison>> clusters = clusterer.cluster(clusterableLivraisons);

        // Créer des parcours pour chaque coursier
        List<ParcoursDeLivraison> parcours = new ArrayList<>();
        for (Cluster<ClusterableLivraison> cluster : clusters) {
            List<Livraison> cheminComplet = new ArrayList<>();

            // Ajouter l'entrepôt comme point de départ
            cheminComplet.add(Livraison.builder()
                    .intersection(entrepots.getIntersection())
                    .estUneLivraison(false)
                    .build());

            List<Livraison> livraisonsCluster = new ArrayList<>();
            for (ClusterableLivraison clusterableLivraison : cluster.getPoints()) {
                livraisonsCluster.add(clusterableLivraison.getLivraison());
            }

            // Trouver le chemin optimal pour les livraisons dans ce cluster
            Intersection dernierPoint = entrepots.getIntersection();

            // Pour chaque livraison, trouver le chemin depuis le dernier point
            for (Livraison livraison : livraisonsCluster) {
                List<Intersection> cheminPartiel = trouverCheminEntreDeux(dernierPoint, livraison.getIntersection(), carte);

                // Convertir les intersections en Livraisons et les ajouter au chemin
                for (int i = 1; i < cheminPartiel.size(); i++) { // Commencer à 1 pour éviter le doublon
                    Intersection intersection = cheminPartiel.get(i);
                    boolean estLivraison = intersection.getId().equals(livraison.getIntersection().getId());
                    cheminComplet.add(Livraison.builder()
                            .intersection(intersection)
                            .estUneLivraison(estLivraison)
                            .build());
                }
                dernierPoint = livraison.getIntersection();
            }

            // Ajouter le chemin de retour à l'entrepôt
            List<Intersection> retourEntrepot = trouverCheminEntreDeux(dernierPoint, entrepots.getIntersection(), carte);
            for (int i = 1; i < retourEntrepot.size(); i++) {
                Intersection intersection = retourEntrepot.get(i);
                cheminComplet.add(Livraison.builder()
                        .intersection(intersection)
                        .estUneLivraison(false)
                        .build());
            }

            // Créer le ParcoursDeLivraison avec le chemin complet
            ParcoursDeLivraison parcoursDeLivraison = ParcoursDeLivraison.builder()
                    .entrepot(entrepots)
                    .livraisons(cheminComplet)  // ICI : on assigne le cheminComplet à livraisons
                    .build();

            parcours.add(parcoursDeLivraison);
        }

        // Pour debug
//        for (ParcoursDeLivraison p : parcours) {
//            System.out.println("Nombre de points dans le parcours : " + p.getLivraisons().size());
//            System.out.println("Points du parcours :");
//            for (Livraison l : p.getLivraisons()) {
//                System.out.println("- Point : " + l.getIntersection().getId() +
//                        " (Livraison : " + l.isEstUneLivraison() + ")");
//            }
//        }

        return TourneeLivraison.builder()
                .parcoursDeLivraisons(parcours)
                .build();
    }

    public TourneeLivraison getPointsDeLivraisonClusterOptimise(int nbCoursier, Map<Long, Intersection> carte) {
        if (nbCoursier == 0) {
            nbCoursier = 2;
        }

        // Clustering des livraisons (code existant...)
        List<ClusterableLivraison> clusterableLivraisons = new ArrayList<>();
        for (Livraison livraison : this.pointDeLivraisons) {
            clusterableLivraisons.add(new ClusterableLivraison(livraison));
        }

        //fix une graine pour avoir des résultats reproductibles (voir la fonction du dessus pour voir comment faire sans graine)
        RandomGenerator random = new org.apache.commons.math3.random.JDKRandomGenerator(42); // Graine fixe
        KMeansPlusPlusClusterer<ClusterableLivraison> clusterer =
                new KMeansPlusPlusClusterer<>(nbCoursier, 1000, new EuclideanDistance(), random);
        List<CentroidCluster<ClusterableLivraison>> clusters = clusterer.cluster(clusterableLivraisons); //TODO Possiblement un model
        // Pour chaque groupe...
        List<ParcoursDeLivraison> parcours = new ArrayList<>();
        for (Cluster<ClusterableLivraison> cluster : clusters) {
            // Récupérer les livraisons du cluster
            List<Livraison> livraisonsCluster = cluster.getPoints().stream()
                    .map(ClusterableLivraison::getLivraison)
                    .toList();

            // Optimiser l'ordre des livraisons avec l'algorithme du plus proche voisin
            List<Livraison> livraisonsOrdonnees = new ArrayList<>();
            List<Livraison> nonVisitees = new ArrayList<>(livraisonsCluster);

            // On commence par l'entrepôt
            Intersection pointCourant = entrepots.getIntersection();

            // Tant qu'il reste des livraisons à faire

            //public void trouverLeMeilleurItineraireDunCluster
            while (!nonVisitees.isEmpty()) {
                // Trouver la livraison la plus proche du point courant
                double distanceMin = Double.MAX_VALUE;
                Livraison plusProche = null;

                //public void trouverPlusCourtCheminDepuisPointCourant
                for (Livraison livraison : nonVisitees) {
                    List<Intersection> chemin = trouverCheminEntreDeux(
                            pointCourant,
                            livraison.getIntersection(),
                            carte
                    );

                    // Calculer la distance totale du chemin
                    double distance = 0;
                    for (int i = 0; i < chemin.size() - 1; i++) {
                        Intersection current = chemin.get(i);
                        Intersection next = chemin.get(i + 1);

                        // Trouver le voisin correspondant
                        for (Voisin voisin : current.getVoisins()) {
                            if (voisin.getDestination().getId().equals(next.getId())) {
                                distance += voisin.getLongueur();
                                break;
                            }
                        }
                    }

                    if (distance < distanceMin) {
                        distanceMin = distance;
                        plusProche = livraison;
                    }
                }

                // Ajouter la livraison la plus proche à notre parcours
                livraisonsOrdonnees.add(plusProche);
                nonVisitees.remove(plusProche);
                pointCourant = plusProche.getIntersection();
            }

            // Construire le chemin complet avec les intersections intermédiaires
            List<Livraison> cheminComplet = new ArrayList<>();
            cheminComplet.add(Livraison.builder()
                    .intersection(entrepots.getIntersection())
                    .estUneLivraison(false)
                    .build());

            // Pour chaque livraison dans l'ordre optimisé
            Intersection dernierPoint = entrepots.getIntersection();
            for (Livraison livraison : livraisonsOrdonnees) {
                List<Intersection> cheminPartiel = trouverCheminEntreDeux(dernierPoint, livraison.getIntersection(), carte);

                // Ajouter les points intermédiaires
                for (int i = 1; i < cheminPartiel.size(); i++) {
                    Intersection intersection = cheminPartiel.get(i);
                    boolean estLivraison = intersection.getId().equals(livraison.getIntersection().getId());
                    cheminComplet.add(Livraison.builder()
                            .intersection(intersection)
                            .estUneLivraison(estLivraison)
                            .build());
                }
                dernierPoint = livraison.getIntersection();
            }

            // Ajouter le retour à l'entrepôt
            List<Intersection> retourEntrepot = trouverCheminEntreDeux(
                    dernierPoint,
                    entrepots.getIntersection(),
                    carte
            );
            for (int i = 1; i < retourEntrepot.size(); i++) {
                cheminComplet.add(Livraison.builder()
                        .intersection(retourEntrepot.get(i))
                        .estUneLivraison(false)
                        .build());
            }

            ParcoursDeLivraison parcoursDeLivraison = ParcoursDeLivraison.builder()
                    .entrepot(entrepots)
                    .livraisons(cheminComplet)
                    .build();

            parcours.add(parcoursDeLivraison);
        }

        return TourneeLivraison.builder()
                .parcoursDeLivraisons(parcours)
                .build();
    }

    private List<Intersection> trouverCheminEntreDeux(Intersection depart, Intersection arrivee, Map<Long, Intersection> carte) {
        // Debug
        System.out.println("Recherche de chemin de " + depart.getId() + " vers " + arrivee.getId());
        System.out.println("Voisins du point de départ : " + (depart.getVoisins() != null ? depart.getVoisins().size() : "null"));

        Map<Long, Double> distances = new HashMap<>();
        Map<Long, Long> previous = new HashMap<>();
        PriorityQueue<Intersection> queue = new PriorityQueue<>(
                Comparator.comparingDouble(i -> distances.getOrDefault(i.getId(), Double.MAX_VALUE))
        );

        // Initialize distances
        for (Long intersectionId : carte.keySet()) {
            distances.put(intersectionId, Double.MAX_VALUE);
        }

        // Start from depart
        distances.put(depart.getId(), 0.0);
        queue.add(depart);

        boolean cheminTrouve = false;

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            Intersection current = queue.poll();
            System.out.println("Exploration du point : " + current.getId());

            if (current.getId().equals(arrivee.getId())) {
                cheminTrouve = true;
                break;
            }

            // Récupérer l'intersection depuis la carte pour avoir les voisins à jour
            current = carte.get(current.getId());
            List<Voisin> voisins = current.getVoisins();

            System.out.println("Voisins actuels : " + (voisins != null ? voisins.size() : "null"));

            if (voisins == null || voisins.isEmpty()) {
                System.out.println("Pas de voisins pour " + current.getId());
                continue;
            }

            for (Voisin voisin : voisins) {
                if (voisin == null || voisin.getDestination() == null) {
                    continue;
                }

                Long neighborId = voisin.getDestination().getId();
                System.out.println("Analyse du voisin : " + neighborId);

                if (!carte.containsKey(neighborId)) {
                    System.out.println("Voisin non trouvé dans la carte : " + neighborId);
                    continue;
                }

                double newDist = distances.get(current.getId()) + voisin.getLongueur();
                if (newDist < distances.getOrDefault(neighborId, Double.MAX_VALUE)) {
                    distances.put(neighborId, newDist);
                    previous.put(neighborId, current.getId());
                    queue.add(carte.get(neighborId));
                    System.out.println("Ajout du point " + neighborId + " au chemin");
                }
            }
        }

        if (!cheminTrouve) {
            System.out.println("Aucun chemin trouvé entre " + depart.getId() + " et " + arrivee.getId());
        }

        List<Intersection> chemin = new ArrayList<>();
        Long at = arrivee.getId();
        while (at != null) {
            Intersection intersection = carte.get(at);
            if (intersection != null) {
                chemin.add(0, intersection);
                at = previous.get(at);
            } else {
                System.out.println("Intersection non trouvée pour l'ID : " + at);
                break;
            }
        }

        System.out.println("Taille du chemin trouvé : " + chemin.size());
        System.out.println("Points du chemin : " + chemin.stream()
                .map(Intersection::getId)
                .collect(Collectors.toList()));

        if (chemin.isEmpty()) {
            System.out.println("Chemin vide, ajout des points de départ et d'arrivée");
            chemin.add(depart);
            if (!depart.equals(arrivee)) {
                chemin.add(arrivee);
            }
        }

        return chemin;
    }

}






