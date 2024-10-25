package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Objet métier) Permet de représenter une livraison (un point à livrer)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Livraison {
    @JsonUnwrapped
    private Intersection adresseLivraison;
}
