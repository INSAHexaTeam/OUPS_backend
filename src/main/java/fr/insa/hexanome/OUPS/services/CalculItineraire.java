package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.ClusterableLivraison;
import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.exception.Tournee;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

@Data
@Builder
public class CalculItineraire {
    List<Livraison> pointDeLivraisons;
    Entrepot entrepots;

    public Tournee getPointsDeLivraisonAleatoire(int nbCoursier) {
        List<Livraison> livraisonsAleatoires = new ArrayList<>(pointDeLivraisons);
        Collections.shuffle(livraisonsAleatoires); // Mélange les points de livraison
        if (nbCoursier == 0) {
            nbCoursier = 2;
        }
        List<Livraisons> livraisonsParCoursier = new ArrayList<>();
        int tailleSousListe = livraisonsAleatoires.size() / nbCoursier;
        int reste = livraisonsAleatoires.size() % nbCoursier;

        int index = 0;
        for (int i = 0; i < nbCoursier; i++) {
            int fin = index + tailleSousListe + (reste > 0 ? 1 : 0);
            reste--;

            Livraisons sousListe = Livraisons.builder()
                    .entrepot(entrepots)
                    .build();
            sousListe.addAll(livraisonsAleatoires.subList(index, fin));
            livraisonsParCoursier.add(sousListe);

            index = fin;
        }

        return Tournee.builder()
                .livraisons(livraisonsParCoursier)
                .build();
    }



    public Tournee getPointsDeLivraisonAleatoireCluster(int nbCoursier) {
        if (nbCoursier == 0) {
            nbCoursier = 2;
        }

        // Convertir les points de livraison en objets Clusterable
        List<ClusterableLivraison> clusterableLivraisons = new ArrayList<>();
        for (Livraison livraison : pointDeLivraisons) {
            clusterableLivraisons.add(new ClusterableLivraison(livraison));
        }

        // Appliquer l'algorithme K-means pour regrouper les livraisons
        KMeansPlusPlusClusterer<ClusterableLivraison> clusterer = new KMeansPlusPlusClusterer<>(nbCoursier);
        List<CentroidCluster<ClusterableLivraison>> clusters = clusterer.cluster(clusterableLivraisons);

        // Créer des listes de livraisons pour chaque coursier
        List<Livraisons> livraisonsParCoursier = new ArrayList<>();
        for (Cluster<ClusterableLivraison> cluster : clusters) {
            Livraisons sousListe = Livraisons.builder()
                    .entrepot(entrepots)
                    .build();
            for (ClusterableLivraison clusterableLivraison : cluster.getPoints()) {
                sousListe.add(clusterableLivraison.getLivraison());
            }
            livraisonsParCoursier.add(sousListe);
        }

        return Tournee.builder()
                .livraisons(livraisonsParCoursier)
                .build();
    }
}






