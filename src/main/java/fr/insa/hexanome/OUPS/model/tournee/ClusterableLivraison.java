package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Livraison;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.ml.clustering.Clusterable;

/**
 * Gère les clusters
 */
@Getter
@Setter
public class ClusterableLivraison implements Clusterable {

        private final Livraison livraison;
        private final double[] points;

        public ClusterableLivraison(Livraison livraison) {
            this.livraison = livraison;
            this.points = new double[]{livraison.getIntersection().getLatitude(), livraison.getIntersection().getLongitude()};
        }

        @Override
        public double[] getPoint() {
            return points;
        }
}

