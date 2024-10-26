package fr.insa.hexanome.OUPS.model;

import fr.insa.hexanome.OUPS.model.dto.TourneeLivraisonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

//La tournée est l'ensemble des livraisons à effectuer
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourneeLivraison {
    //chaque élément de la liste correspond à un coursier
    public List<ParcoursDeLivraison> parcoursDeLivraisons;

    public TourneeLivraisonDTO toDTO() {
        return TourneeLivraisonDTO.builder()
                //transforme chaque livraison de la liste en livraisonDTO
                .livraisons(parcoursDeLivraisons.stream().map(ParcoursDeLivraison::toDTO).collect(Collectors.toList()))
                .build();
    }

    @Override
    public String toString() {
        return "TourneeLivraison{" +
                "parcoursDeLivraisons=" + (parcoursDeLivraisons != null ? parcoursDeLivraisons.size() : "null") +
                '}';
    }
}
