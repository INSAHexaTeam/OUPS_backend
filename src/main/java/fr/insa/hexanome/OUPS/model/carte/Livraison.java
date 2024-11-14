package fr.insa.hexanome.OUPS.model.carte;

import fr.insa.hexanome.OUPS.model.dto.LivraisonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Livraison {
    private Intersection intersection;
    private boolean estUneLivraison;
    private double distance;
    //heure d'arrivée :
    private LocalTime heureArrivee;

//    public LocalTime calculerHeureDarrivee(){
//        LocalTime heureDepart = LocalTime.of(8,0);
//        double distanceKm = distance/1000;
//        double tempsTrajet = distanceKm/15;
//        int heures = (int)tempsTrajet;
//        int minutes = (int)((tempsTrajet - heures)*60);
//        this.heureArrivee = heureDepart.plusHours(heures).plusMinutes(minutes);
//        return heureArrivee;
//    }
    public LivraisonDTO toDTO() {
        return LivraisonDTO.builder()
                .intersection(intersection != null ? intersection.toDTO() : null)
                .estUneLivraison(estUneLivraison)
                .distanceParcourue(distance)
                .heureArrivee(this.getHeureArrivee())
                .build();
    }

    public static Livraison fromDTO(LivraisonDTO dto) {
        if (dto == null) return null;
        return Livraison.builder()
                .intersection(Intersection.fromDTO(dto.getIntersection()))
                .estUneLivraison(dto.isEstUneLivraison())
                .distance(dto.getDistanceParcourue())
                .heureArrivee(dto.getHeureArrivee())
                .build();
    }
    @Override
    public String toString() {
        return "Livraison{" +
                "intersection=" + (intersection != null ? intersection.getId() : "null") +
                ", estUneLivraison=" + estUneLivraison +
                '}';
    }

}