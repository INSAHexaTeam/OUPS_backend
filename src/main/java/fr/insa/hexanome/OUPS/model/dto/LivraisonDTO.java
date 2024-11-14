package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDTO {
    private IntersectionDTO intersection;
    private boolean estUneLivraison;
    private double distanceParcourue;
    private LocalTime heureArrivee;
}