package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Voisin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Calcul d'itinéraire entre deux intersections
 */

public class ItineraireService {

    public static List<Intersection> trouverCheminEntreDeuxIntersections(Intersection depart, Intersection arrivee, Map<Long, Intersection> carte) {

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
//            System.out.println("Exploration du point : " + current.getId());

            if (current.getId().equals(arrivee.getId())) {
                cheminTrouve = true;
                break;
            }

            // Récupérer l'intersection depuis la carte pour avoir les voisins à jour
            current = carte.get(current.getId());
            List<Voisin> voisins = current.getVoisins();

//            System.out.println("Voisins actuels : " + (voisins != null ? voisins.size() : "null"));

            if (voisins == null || voisins.isEmpty()) {
//                System.out.println("Pas de voisins pour " + current.getId());
                continue;
            }

            for (Voisin voisin : voisins) {
                if (voisin == null || voisin.getDestination() == null) {
                    continue;
                }

                Long neighborId = voisin.getDestination().getId();
//                System.out.println("Analyse du voisin : " + neighborId);

                if (!carte.containsKey(neighborId)) {
//                    System.out.println("Voisin non trouvé dans la carte : " + neighborId);
                    continue;
                }

                double newDist = distances.get(current.getId()) + voisin.getLongueur();
                if (newDist < distances.getOrDefault(neighborId, Double.MAX_VALUE)) {
                    distances.put(neighborId, newDist);
                    previous.put(neighborId, current.getId());
                    queue.add(carte.get(neighborId));
//                    System.out.println("Ajout du point " + neighborId + " au chemin");
                }
            }
        }

        if (!cheminTrouve) {
//            System.out.println("Aucun chemin trouvé entre " + depart.getId() + " et " + arrivee.getId());
        }

        List<Intersection> chemin = new ArrayList<>();
        Long at = arrivee.getId();
        while (at != null) {
            Intersection intersection = carte.get(at);
            if (intersection != null) {
                chemin.add(0, intersection);
                at = previous.get(at);
            } else {
//                System.out.println("Intersection non trouvée pour l'ID : " + at);
                break;
            }
        }

//        System.out.println("Taille du chemin trouvé : " + chemin.size());
//        System.out.println("Points du chemin : " + chemin.stream()
//                .map(Intersection::getId)
//                .collect(Collectors.toList()));

        if (chemin.isEmpty()) {
//            System.out.println("Chemin vide, ajout des points de départ et d'arrivée");
            chemin.add(depart);
            if (!depart.equals(arrivee)) {
                chemin.add(arrivee);
            }
        }

        return chemin;
    }
}
