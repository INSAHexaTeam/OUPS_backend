package fr.insa.hexanome.OUPS.model;

import org.apache.commons.math3.ml.clustering.Clusterable;

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

        public Livraison getLivraison() {
            return livraison;
        }
}

