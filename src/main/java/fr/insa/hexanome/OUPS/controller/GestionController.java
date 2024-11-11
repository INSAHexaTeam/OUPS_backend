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
import fr.insa.hexanome.OUPS.tsp.Graph;
import fr.insa.hexanome.OUPS.tsp.TSP;
import fr.insa.hexanome.OUPS.tsp.TSP1;
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


        ArrayList<DemandeLivraisons> listeDeListe = demandeLivraisonsATransformer.split(request.getCoursier());


        ArrayList<List<Intersection>> resultat = new ArrayList<>();
        for(DemandeLivraisons demandeLivraisonsCourante : listeDeListe){
            ParcoursDeLivraison parcoursParCoursier = ParcoursDeLivraison.builder()
                    .entrepot(entrepot)
                    .livraisons(new ArrayList<>())
                    .build();

            demandeLivraisonsCourante.addFirst(livraisonEntrepot);
//            demandeLivraisonsCourante.addLast(livraisonEntrepot);
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
            parcoursParCoursier.setLivraisons(solutionCourante);
            tourneeLivraison.getParcoursDeLivraisons().add(parcoursParCoursier);

        }



        return ResponseEntity.ok(tourneeLivraison.toDTO());

    }





}
