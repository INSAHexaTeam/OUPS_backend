package fr.insa.hexanome.OUPS.controller;


import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.dto.*;
import fr.insa.hexanome.OUPS.model.carte.Carte;
import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.exception.FileExtension;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.insa.hexanome.OUPS.services.ItineraireService.trouverCheminEntreDeuxIntersections;

/**
 * Cette classe représente le point de liaison entre le frontend et le backend
 */
@RestController
@RequestMapping("/carte")
public class GestionController {

    private final GestionService service;
    /**
     * @param service service pour la gestion des livraisons et des map
     */
    public GestionController(GestionService service) {
        this.service = service;
    }

    private Carte carte;


    /**
     * Chargement des plans
     *
     * @param stringEtat etat du chargement (CHARGEMENT,ENREGISTREMENT,ENREGISTREMENT_SIMPLE)
     * @param fichier Fichier XML a charger
     * @param cheminVersFichier CheminVersFichier à charger
     */
    @Operation(
            summary = "Charger un fichier",
            description = "Charger un plan XML",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fichier chargé avec succès",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarteDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erreur de validation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileExtension.class)
                            )
                    )
            }
    )
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

    /**
     * Chargement des demandes
     *
     * @param stringEtat etat du chargement (CHARGEMENT,ENREGISTREMENT,ENREGISTREMENT_SIMPLE)
     * @param fichier Fichier XML a charger
     * @param cheminVersFichier CheminVersFichier à charger
     */
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


    //todo : peut être retourné un objet Tournée (pas encore ajouté au modèle)
//    @PostMapping("/calculerItineraireAleatoire")
//    public  ResponseEntity<TourneeLivraisonDTO>
//    calculerItineraireAleatoire(
//            @RequestBody DemandeLivraisonsDTO request
//    ) throws ParserConfigurationException, IOException, SAXException {
//
//        Entrepot entrepot = Entrepot.builder()
//                .heureDepart(request.getEntrepot().getHeureDepart())
//                .intersection(request.getEntrepot().getIntersection())
//                .build();
//        DemandeLivraisons demandeLivraisons = request.toLivraison();
//        CalculItineraire calculItineraireAleatoire = CalculItineraire.builder()
//                .entrepots(entrepot)
//                .pointDeLivraisons(demandeLivraisons)
//                .build();
//        TourneeLivraison livraisonsAleatoires = calculItineraireAleatoire.getPointsDeLivraisonAleatoire(request.getCoursier());
//        return ResponseEntity.ok(livraisonsAleatoires.toDTO());
//    }
//
//    //todo : peut être retourné un objet Tournée (pas encore ajouté au modèle)
//    @PostMapping("/calculerItineraireCluster")
//    public  ResponseEntity<TourneeLivraisonDTO>
//    calculerItineraireCluster(
//            @RequestBody DemandeLivraisonsDTO request
//    ) throws ParserConfigurationException, IOException, SAXException {
//
//        Entrepot entrepot = Entrepot.builder()
//                .heureDepart(request.getEntrepot().getHeureDepart())
//                .intersection(request.getEntrepot().getIntersection())
//                .build();
//        DemandeLivraisons demandeLivraisons = request.toLivraison();
//        CalculItineraire calculItineraireAleatoire = CalculItineraire.builder()
//                .entrepots(entrepot)
//                .pointDeLivraisons(demandeLivraisons)
//                .build();
//        TourneeLivraison livraisonsAleatoires = calculItineraireAleatoire.getPointsDeLivraisonAleatoireCluster(request.getCoursier());
//        return ResponseEntity.ok(livraisonsAleatoires.toDTO());
//    }

//    @PostMapping("/calculerItineraireClusterOptimal")
//    public  ResponseEntity<TourneeLivraisonDTO>
//    calculerItineraire(
//            @RequestBody DemandeLivraisonsDTO request
//    ) throws ParserConfigurationException, IOException, SAXException {
//
//        Entrepot entrepot = Entrepot.builder()
//                .heureDepart(request.getEntrepot().getHeureDepart())
//                .intersection(Intersection.fromDTO(request.getEntrepot().getIntersection()))
//                .build();
//
//        DemandeLivraisons demandeLivraisons = request.toDemandeLivraisons();
//        CalculItineraire calculItineraireAleatoire = CalculItineraire.builder()
//                .entrepots(entrepot)
//                .pointDeLivraisons(demandeLivraisons)
//                .build();
//        TourneeLivraison livraisonsOptimales = calculItineraireAleatoire.getPointsDeLivraisonClusterOptimise(request.getCoursier(),carte.getIntersectionsMap());
//        System.out.println(livraisonsOptimales);
//        return ResponseEntity.ok(livraisonsOptimales.toDTO());
//    }


    @PostMapping("/calculerItineraireOrdonne")
    public ResponseEntity<TourneeLivraisonDTO> calculItineraireOrdonne(
            @RequestBody TourneeLivraisonDTO request
    ) {
        try {
            // Créer une nouvelle tournée qui contiendra les chemins calculés
            TourneeLivraison tourneeLivraisonResultat = TourneeLivraison.builder()
                    .parcoursDeLivraisons(new ArrayList<>())
                    .build();

            // Pour chaque parcours de livraison dans la requête
            for (ParcoursDeLivraisonDTO parcoursDTO : request.getLivraisons()) {
                // Convertir le DTO en modèle
                DemandeLivraisons demandeLivraisons = parcoursDTO.getLivraisons().toDemandeLivraisons();
                demandeLivraisons.removeFirst(); // On enlève l'entrepôt

                // Créer un nouveau parcours pour ce coursier
                ParcoursDeLivraison parcoursDeLivraison = ParcoursDeLivraison.builder()
                        .entrepot(demandeLivraisons.getEntrepot())
                        .livraisons(demandeLivraisons)
                        .chemin(new ArrayList<>())
                        .build();

                // Récupérer l'entrepôt
                Intersection depart = demandeLivraisons.getEntrepot().getIntersection();

                // Récupérer la map des intersections pour dijkstra
                Map<Long, Intersection> intersectionsMap = carte.getIntersectionsMap();

                // Calculer le chemin de l'entrepôt à la première livraison
                if (!demandeLivraisons.isEmpty()) {
                    List<Intersection> cheminInitial = ItineraireService.trouverCheminEntreDeuxIntersections(
                            depart,
                            demandeLivraisons.get(0).getIntersection(),
                            intersectionsMap
                    );
                    parcoursDeLivraison.getChemin().addAll(cheminInitial);

                    // Calculer le chemin entre chaque livraison
                    for (int i = 0; i < demandeLivraisons.size() - 1; i++) {
                        List<Intersection> cheminLivraison = ItineraireService.trouverCheminEntreDeuxIntersections(
                                demandeLivraisons.get(i).getIntersection(),
                                demandeLivraisons.get(i + 1).getIntersection(),
                                intersectionsMap
                        );
                        parcoursDeLivraison.getChemin().addAll(cheminLivraison);
                    }

                    // Calculer le chemin de la dernière livraison à l'entrepôt
                    List<Intersection> cheminFinal = ItineraireService.trouverCheminEntreDeuxIntersections(
                            demandeLivraisons.get(demandeLivraisons.size() - 1).getIntersection(),
                            depart,
                            intersectionsMap
                    );
                    parcoursDeLivraison.getChemin().addAll(cheminFinal);
                }

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
        Livraison livraisonEntrepot = Livraison.builder()
                .intersection(entrepot.getIntersection())
                .estUneLivraison(false)
                .build();

        if (livraisonEntrepot.getIntersection() == null) {
            throw new IllegalArgumentException("L'entrepôt n'a pas été défini.");
        }

        TourneeLivraison tourneeLivraison = TourneeLivraison.builder()
                .parcoursDeLivraisons(new ArrayList<>())
                .build();


        // Clustering des livraisons (code existant...)
        List<ClusterableLivraison> clusterableLivraisons = new ArrayList<>();
        for (Livraison livraison : demandeLivraisonsATransformer) {
            clusterableLivraisons.add(new ClusterableLivraison(livraison));
        }

        //fix une graine pour avoir des résultats reproductibles (voir la fonction du dessus pour voir comment faire sans graine)
        RandomGenerator random = new org.apache.commons.math3.random.JDKRandomGenerator(42); // Graine fixe
        KMeansPlusPlusClusterer<ClusterableLivraison> clusterer =
                new KMeansPlusPlusClusterer<>(request.getCoursier(), 1000, new EuclideanDistance(), random);
        List<CentroidCluster<ClusterableLivraison>> clusters = clusterer.cluster(clusterableLivraisons);


        for(Cluster<ClusterableLivraison> clusterLivraison : clusters){

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
            List<Livraison> solutionCourante = new ArrayList<>();
            List<Intersection> chemin = new ArrayList<>();
            for(int j =0;j<sol.length;j++){
                solutionCourante.add(demandeLivraisonsCourante.get(sol[j]));
                //récupère les chemins dans la matrice de test
                if(j<sol.length-1){
                    ElemMatrice elem = test.getMatrice()[sol[j]][sol[j+1]];
                    chemin.addAll(elem.getIntersections());
                }
            }
            ElemMatrice elem = test.getMatrice()[sol[sol.length-1]][sol[0]];
            chemin.addAll(elem.getIntersections());

            parcoursParCoursier.setChemin(chemin);
            solutionCourante.removeFirst(); //on enlève l'entrepot de la liste des livraisons
            parcoursParCoursier.setLivraisons(solutionCourante);
            tourneeLivraison.getParcoursDeLivraisons().add(parcoursParCoursier);

        }



        return ResponseEntity.ok(tourneeLivraison.toDTO());

    }





}
