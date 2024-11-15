package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.dto.*;
import fr.insa.hexanome.OUPS.model.carte.Carte;
import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.tournee.*;
import fr.insa.hexanome.OUPS.services.CalculItineraire;
import fr.insa.hexanome.OUPS.services.EtatType;
import fr.insa.hexanome.OUPS.services.GestionService;
import fr.insa.hexanome.OUPS.services.ItineraireService;
import fr.insa.hexanome.OUPS.tsp.Graph;
import fr.insa.hexanome.OUPS.tsp.TSP;
import fr.insa.hexanome.OUPS.tsp.TSP1;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static fr.insa.hexanome.OUPS.services.ItineraireService.trouverCheminEntreDeuxIntersections;

@RestController
@RequestMapping("/carte")
public class GestionController {
    GestionService service;
    public GestionController(GestionService service) {
        this.service = service;
    }

    private Carte carte;


    @PostMapping("/charger")
    public ResponseEntity<CarteDTO> charger(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        Carte carteRequest = this.service.chargerCarteDepuisXML(etatType, fichier, cheminVersFichier);
        this.carte = carteRequest;
        return ResponseEntity.ok(carteRequest.toDTO());
    }

    @PostMapping("/livraisons")
    public ResponseEntity<?> livraisons(
            @RequestParam("etat") String stringEtat,
            @RequestParam(value = "fichier", required = false ) MultipartFile fichier,
            @RequestParam(value = "cheminVersFichier", required = false) String cheminVersFichier
    ) throws ParserConfigurationException, IOException, SAXException {
        EtatType etatType = EtatType.valueOf(stringEtat);
        try {
            DemandeLivraisons demandeLivraisons = this.service.chargerLivraisonsDepuisXML(etatType, fichier, cheminVersFichier);
            return ResponseEntity.ok(demandeLivraisons.toDTO());
        } catch (Exception e) {
            // Créer un objet de réponse d'erreur personnalisé contenant le nom de l'exception et le message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getClass().getSimpleName()); // Nom de l'exception
            errorResponse.put("message", e.getMessage()); // Message de l'exception

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/calculerItineraireOrdonne")
    public ResponseEntity<TourneeLivraisonDTO> calculItineraireOrdonne(
            @RequestBody TourneeLivraisonDTO request
    ) {
        try {
            if (request == null || request.getLivraisons() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Créer une nouvelle tournée qui contiendra les chemins calculés
            TourneeLivraison tourneeLivraisonResultat = TourneeLivraison.builder()
                    .parcoursDeLivraisons(new ArrayList<>())
                    .build();

            // Pour chaque parcours de livraison dans la requête
            for (ParcoursDeLivraisonDTO parcoursDTO : request.getLivraisons()) {
                if (parcoursDTO == null || parcoursDTO.getLivraisons() == null) {
                    continue;
                }

                // Convertir le DTO en modèle
                DemandeLivraisons demandeLivraisons = parcoursDTO.getLivraisons().toDemandeLivraisons();
                if (demandeLivraisons == null || demandeLivraisons.isEmpty()) {
                    continue;
                }

                //Intersection.fromDTO(livraisonDTO.getIntersection())

                List<Livraison> livraisonsCompletes = new ArrayList<>();
                for (LivraisonDTO livraisonDTO : parcoursDTO.getLivraisons().getLivraisons()) {
                    Livraison livraison = Livraison.builder()
                            .intersection(carte.trouverIntersectionParId(livraisonDTO.getIntersection().getId()))
                            .estUneLivraison(true)
                            .build();
                    livraisonsCompletes.add(livraison);
                }


                // Créer un nouveau parcours pour ce coursier
                ParcoursDeLivraison parcoursDeLivraison = ParcoursDeLivraison.builder()
                        .entrepot(demandeLivraisons.getEntrepot())
                        .livraisons(new ArrayList<>(livraisonsCompletes))
                        .chemin(new ArrayList<>())
                        .build();

                // Calculer la matrice des chemins les plus courts
                CalculItineraire calculItineraire = CalculItineraire.builder()
                        .matrice(new ElemMatrice[livraisonsCompletes.size()][livraisonsCompletes.size()])
                        .carte(carte)
                        .livraisons(livraisonsCompletes)
                        .build();

                calculItineraire.calculDijkstra();

                // Construire le chemin complet et mettre à jour les heures d'arrivée
                List<Intersection> cheminComplet = new ArrayList<>();
                LocalTime heureArrivee = LocalTime.of(8,0);

                // Parcourir les points dans l'ordre
                for (int i = 0; i < livraisonsCompletes.size() - 1; i++) {
                    Livraison livraisonCourante = livraisonsCompletes.get(i);

                    // Mettre à jour l'heure d'arrivée
                    if (i > 0) {
                        ElemMatrice elemPrecedent = calculItineraire.getMatrice()[i-1][i];
                        if (elemPrecedent != null) {
                            double distanceMetre = elemPrecedent.getCout();
                            heureArrivee = heureArrivee.plusMinutes((long) (distanceMetre/1000/15*60));
                            livraisonCourante.setHeureArrivee(heureArrivee);
                        }
                    }

                    // Ajouter le chemin vers le point suivant
                    ElemMatrice elem = calculItineraire.getMatrice()[i][i+1];
                    if (elem != null && elem.getIntersections() != null) {
                        cheminComplet.addAll(elem.getIntersections());
                    }
                }

                // Mettre à jour l'heure d'arrivée pour le dernier point
                if (livraisonsCompletes.size() > 1) {
                    Livraison derniereLivraison = livraisonsCompletes.get(livraisonsCompletes.size() - 1);
                    ElemMatrice elemDernier = calculItineraire.getMatrice()[livraisonsCompletes.size() - 2][livraisonsCompletes.size() - 1];
                    if (elemDernier != null) {
                        double distanceFinale = elemDernier.getCout();
                        heureArrivee = heureArrivee.plusMinutes((long) (distanceFinale/1000/15*60));
                        derniereLivraison.setHeureArrivee(heureArrivee);
                    }

                    parcoursDeLivraison.getLivraisons().getFirst().setHeureArrivee(LocalTime.of(8,0));
                }

                // Finaliser le parcours
                parcoursDeLivraison.setChemin(cheminComplet);
                tourneeLivraisonResultat.getParcoursDeLivraisons().add(parcoursDeLivraison);
            }

            return ResponseEntity.ok(tourneeLivraisonResultat.toDTO());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/graph")
    public  ResponseEntity<TourneeLivraisonDTO>
    calculerGraph(
            @RequestBody DemandeLivraisonsDTO request
    ) throws ParserConfigurationException, IOException, SAXException {

        Entrepot entrepot = Entrepot.builder()
                .heureDepart(request.getEntrepot().getHeureDepart())
                .intersection(Intersection.fromDTO(request.getEntrepot().getIntersection()))
                .build();

        DemandeLivraisons demandeLivraisonsATransformer = request.toDemandeLivraisons();


        TourneeLivraison tourneeLivraison = TourneeLivraison.builder()
                .parcoursDeLivraisons(new ArrayList<>())
                .build();


        // Clustering des livraisons (code existant...)
        List<ClusterableLivraison> clusterableLivraisons = new ArrayList<>();
        for (Livraison livraison : demandeLivraisonsATransformer) {
            Livraison livraisonClean = Livraison.builder()
                    .intersection(carte.trouverIntersectionParId(livraison.getIntersection().getId()))
                    .estUneLivraison(true)
                    .build();
            clusterableLivraisons.add(new ClusterableLivraison(livraisonClean));
        }

        //fix une graine pour avoir des résultats reproductibles (voir la fonction du dessus pour voir comment faire sans graine)
        RandomGenerator random = new org.apache.commons.math3.random.JDKRandomGenerator(42); // Graine fixe
        KMeansPlusPlusClusterer<ClusterableLivraison> clusterer =
                new KMeansPlusPlusClusterer<>(request.getCoursier(), 1000, new EuclideanDistance(), random);
        List<CentroidCluster<ClusterableLivraison>> clusters = clusterer.cluster(clusterableLivraisons);


        for(Cluster<ClusterableLivraison> clusterLivraison : clusters){

            Livraison livraisonEntrepot = Livraison.builder()
                    .intersection(entrepot.getIntersection())
                    .estUneLivraison(false)
                    .heureArrivee(LocalTime.of(8,0))
                    .build();

            Livraison livraisonEntrepotFin = Livraison.builder()
                    .intersection(entrepot.getIntersection())
                    .estUneLivraison(false)
                    .heureArrivee(LocalTime.of(15,0))
                    .build();

            if (livraisonEntrepot.getIntersection() == null) {
                throw new IllegalArgumentException("L'entrepôt n'a pas été défini.");
            }

            List<Livraison> demandeLivraisonsCourante = new ArrayList<>
                    (clusterLivraison.getPoints().stream().map(ClusterableLivraison::getLivraison).toList());


            ParcoursDeLivraison parcoursParCoursier = ParcoursDeLivraison.builder()
                    .entrepot(entrepot)
                    .livraisons(new ArrayList<>())
                    .build();

            demandeLivraisonsCourante.addFirst(livraisonEntrepot);

            CalculItineraire test = CalculItineraire.builder()
                    .matrice(new ElemMatrice[demandeLivraisonsCourante.size()][demandeLivraisonsCourante.size()])
                    .carte(carte)
                    .livraisons(demandeLivraisonsCourante)
                    .build();

            test.calculDijkstra(); //rempli la matrice d'adjacence

            TSPGraph graph = TSPGraph.builder() //TSP du prof
                    .carte(carte)
                    .livraisons(demandeLivraisonsCourante)
                    .matrice(test.getMatrice())
                    .build();

            Integer[] sol = graph.getSolution(); //sera la liste des indices des livraisons dans l'ordre de passage
            demandeLivraisonsCourante.addLast(livraisonEntrepotFin); //on ajoute l'entrepot à la fin*

            List<Livraison> solutionCourante = new ArrayList<>();
            List<Intersection> chemin = new ArrayList<>();
            LocalTime heureArrivee = livraisonEntrepot.getHeureArrivee(); //on commence à décompter les heures à partir de l'heure de départ de l'entrepot
            for(int j =0;j<sol.length;j++){
                Livraison livraison = demandeLivraisonsCourante.get(sol[j]);
                if (j>0){
                    double distanceMetre = test.getMatrice()[sol[j-1]][sol[(j)%sol.length]].getCout();
                    heureArrivee = heureArrivee.plusMinutes((long) (distanceMetre/1000/15*60));
                    livraison.setHeureArrivee(heureArrivee);
                }
                solutionCourante.add(livraison);
                //récupère les chemins dans la matrice de test
                if(j<sol.length-1){
                    ElemMatrice elem = test.getMatrice()[sol[j]][sol[j+1]];
                    chemin.addAll(elem.getIntersections());
                }
            }
            ElemMatrice elem = test.getMatrice()[sol[sol.length-1]][sol[0]];//pour aller du dernier à l'entrepot

            Livraison entrepotFinal = livraisonEntrepotFin;
            double distanceMetre = test.getMatrice()[sol[sol.length-1]][sol[(0)]].getCout();//on récupère la distance entre entrepot et dernière livraison
            heureArrivee = heureArrivee.plusMinutes((long) (distanceMetre/1000/15*60));
            entrepotFinal.setHeureArrivee(heureArrivee);
            chemin.addAll(elem.getIntersections());
            solutionCourante.add(entrepotFinal);

            parcoursParCoursier.setChemin(chemin);
            parcoursParCoursier.setLivraisons(solutionCourante);
            //parcoursParCoursier.getLivraisons().getFirst().setHeureArrivee(LocalTime.of(8,0));
            tourneeLivraison.getParcoursDeLivraisons().add(parcoursParCoursier);
        }

        return ResponseEntity.ok(tourneeLivraison.toDTO());

    }


    @PostMapping("/calculerItineraireOrdonneOpti/{coursierId}")
    public ResponseEntity<ParcoursDeLivraisonDTO> calculItineraireOrdonnePourCoursier(
            @PathVariable int coursierId,
            @RequestBody TourneeLivraisonDTO request
    ) {
        try {
            if (request == null || request.getLivraisons() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Créer une nouvelle tournée qui contiendra les chemins calculés
//            TourneeLivraison tourneeLivraisonResultat = TourneeLivraison.builder()
//                    .parcoursDeLivraisons(new ArrayList<>())
//                    .build();

            // Pour chaque parcours de livraison dans la requête
            for (ParcoursDeLivraisonDTO parcoursDTO : request.getLivraisons()) {


            }
                ParcoursDeLivraisonDTO parcoursDTO = request.getLivraisons().get(coursierId);
//                if (parcoursDTO == null || parcoursDTO.getLivraisons() == null) {
//                    continue;
//                }

                // Convertir le DTO en modèle
                DemandeLivraisons demandeLivraisons = parcoursDTO.getLivraisons().toDemandeLivraisons();
//                   if (demandeLivraisons == null || demandeLivraisons.isEmpty()) {
//                    continue;
//                }

                //Intersection.fromDTO(livraisonDTO.getIntersection())

                List<Livraison> livraisonsCompletes = new ArrayList<>();
                for (LivraisonDTO livraisonDTO : parcoursDTO.getLivraisons().getLivraisons()) {
                    Livraison livraison = Livraison.builder()
                            .intersection(carte.trouverIntersectionParId(livraisonDTO.getIntersection().getId()))
                            .estUneLivraison(true)
                            .build();
                    livraisonsCompletes.add(livraison);
                }


                // Créer un nouveau parcours pour ce coursier
                ParcoursDeLivraison parcoursDeLivraison = ParcoursDeLivraison.builder()
                        .entrepot(demandeLivraisons.getEntrepot())
                        .livraisons(new ArrayList<>(livraisonsCompletes))
                        .chemin(new ArrayList<>())
                        .build();

                // Calculer la matrice des chemins les plus courts
                CalculItineraire calculItineraire = CalculItineraire.builder()
                        .matrice(new ElemMatrice[livraisonsCompletes.size()][livraisonsCompletes.size()])
                        .carte(carte)
                        .livraisons(livraisonsCompletes)
                        .build();

                calculItineraire.calculDijkstra();

                // Construire le chemin complet et mettre à jour les heures d'arrivée
                List<Intersection> cheminComplet = new ArrayList<>();
                LocalTime heureArrivee = LocalTime.of(8,0);

                // Parcourir les points dans l'ordre
                for (int i = 0; i < livraisonsCompletes.size() - 1; i++) {
                    Livraison livraisonCourante = livraisonsCompletes.get(i);

                    // Mettre à jour l'heure d'arrivée
                    if (i > 0) {
                        ElemMatrice elemPrecedent = calculItineraire.getMatrice()[i-1][i];
                        if (elemPrecedent != null) {
                            double distanceMetre = elemPrecedent.getCout();
                            heureArrivee = heureArrivee.plusMinutes((long) (distanceMetre/1000/15*60));
                            livraisonCourante.setHeureArrivee(heureArrivee);
                        }
                    }

                    // Ajouter le chemin vers le point suivant
                    ElemMatrice elem = calculItineraire.getMatrice()[i][i+1];
                    if (elem != null && elem.getIntersections() != null) {
                        cheminComplet.addAll(elem.getIntersections());
                    }
                }

                // Mettre à jour l'heure d'arrivée pour le dernier point
                if (livraisonsCompletes.size() > 1) {
                    Livraison derniereLivraison = livraisonsCompletes.get(livraisonsCompletes.size() - 1);
                    ElemMatrice elemDernier = calculItineraire.getMatrice()[livraisonsCompletes.size() - 2][livraisonsCompletes.size() - 1];
                    if (elemDernier != null) {
                        double distanceFinale = elemDernier.getCout();
                        heureArrivee = heureArrivee.plusMinutes((long) (distanceFinale/1000/15*60));
                        derniereLivraison.setHeureArrivee(heureArrivee);
                    }

                    parcoursDeLivraison.getLivraisons().getFirst().setHeureArrivee(LocalTime.of(8,0));
                }

                // Finaliser le parcours
                parcoursDeLivraison.setChemin(cheminComplet);
//              tourneeLivraisonResultat.getParcoursDeLivraisons().add(parcoursDeLivraison);

            return ResponseEntity.ok(parcoursDeLivraison.toDTO());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
