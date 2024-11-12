package fr.insa.hexanome.OUPS.services;

import lombok.Getter;

/**
 * Calcul des temps d'exécution pour optimisation
 */
@Getter
public class TempsExec {
    private long tempsExec;

    public TempsExec() {
        this.tempsExec = 0;
    }

    public void start() {
        this.tempsExec = System.currentTimeMillis();
    }

    public long stop() {
        return System.currentTimeMillis() - this.tempsExec;
    }

    public void reset() {
        this.tempsExec = 0;
    }

    public void print() {
        System.out.println("Temps d'exécution : " + this.stop() + " ms");
    }
}
