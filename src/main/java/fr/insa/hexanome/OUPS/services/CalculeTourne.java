package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Intersection;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.Voisin;
import java.util.ArrayList;
import java.util.List;

public class CalculeTourne {

    private Livraisons livraisons;
    private double bestSolCost;
    private ArrayList<Intersection> bestSol;
    private int timeLimit;
    private long startTime;


    public CalculeTourne(Livraisons livraisons) {
        this.livraisons = livraisons;
    }


    public void searchSolution(int timeLimit)
    {
        if (timeLimit <= 0) return;
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        int tailleGraph = livraisons.size();
        bestSol = new ArrayList<Intersection>(tailleGraph);
        ArrayList<Intersection> unvisited = new ArrayList<Intersection>(tailleGraph-1);
        for (int i=1; i<tailleGraph; i++) unvisited.add(livraisons.get(i).getAdresseLivraison());
        ArrayList<Intersection> visited = new ArrayList<Intersection>(tailleGraph);
        visited.add(livraisons.getFirst().getAdresseLivraison());
        bestSolCost = Integer.MAX_VALUE;
        branchAndBound(livraisons.getFirst().getAdresseLivraison(), unvisited, visited, 0);

    }

    public Intersection getSolution(int i){
        if (!livraisons.isEmpty() && i>=0 && i<livraisons.size())
            return bestSol.get(i);
        return null;
    }

    public double getSolutionCost(){
        if (!livraisons.isEmpty())
            return bestSolCost;
        return -1;
    }



    private void branchAndBound(Intersection intersectionActuelle, ArrayList<Intersection> unvisited,
                                ArrayList<Intersection> visited, double coutActuelle){

        if (System.currentTimeMillis() - startTime > timeLimit) return;
        long idDepot = 0L;
        System.out.println("pppppp");
        if (unvisited.isEmpty()){
            System.out.println("Kkkkkk");
            if (intersectionActuelle.trouverVoisin(idDepot)!=null){
                if (coutActuelle + intersectionActuelle.trouverVoisin(idDepot).getLongueur() < bestSolCost){
                    bestSol = visited;
                    bestSolCost = coutActuelle+intersectionActuelle.trouverVoisin(idDepot).getLongueur();
                }
            }
        } else if (coutActuelle+bound(intersectionActuelle,unvisited) < bestSolCost){
            List<Voisin> candidates = intersectionActuelle.getVoisins();
            for (Voisin candidate : candidates){
                Intersection intersectionSuivante = candidate.getDestination();
                visited.add(intersectionSuivante);
                unvisited.remove(intersectionSuivante);
                branchAndBound(intersectionSuivante, unvisited, visited,
                        coutActuelle+intersectionActuelle.trouverVoisin(intersectionSuivante.getId()).getLongueur());
                visited.remove(intersectionSuivante);
                unvisited.add(intersectionSuivante);
            }
        }
    }

    private int bound(Intersection intersectionActuelle, ArrayList<Intersection> unvisited) {
        return 0;
    }
}
