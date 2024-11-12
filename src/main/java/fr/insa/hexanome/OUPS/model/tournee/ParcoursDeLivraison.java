package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Intersection;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.DemandeLivraisonsDTO;
import fr.insa.hexanome.OUPS.model.dto.LivraisonDTO;
import fr.insa.hexanome.OUPS.model.dto.ParcoursDeLivraisonDTO;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ParcoursDeLivraison {
    private Entrepot entrepot;
    private List<Livraison> livraisons;
    private List<Intersection> chemin;

    public ParcoursDeLivraisonDTO toDTO() {
        List<IntersectionDTO> cheminIntersectionsDTO = chemin.stream()
                .map(Intersection::toDTO)
                .collect(Collectors.toList());

        DemandeLivraisonsDTO demandeLivraisonsDTO = DemandeLivraisonsDTO.builder()
                .entrepot(entrepot.toDTO())
                .livraisons(livraisons.stream()
                        .map(livraison -> LivraisonDTO.builder()
                                .intersection(livraison.getIntersection().toDTO())
                                .distanceParcourue(livraison.getDistance())
                                .heureArrivee(livraison.getHeureArrivee())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        return ParcoursDeLivraisonDTO.builder()
                .cheminIntersections(cheminIntersectionsDTO)
                .livraisons(demandeLivraisonsDTO)
                .build();
    }

    @Override
    public String toString() {
        return "ParcoursDeLivraison{" +
                "entrepot=" + (entrepot != null ? entrepot : "null") +
                ", livraisons=" + (livraisons != null ? livraisons.size() : "null") +
                '}';
    }
}
