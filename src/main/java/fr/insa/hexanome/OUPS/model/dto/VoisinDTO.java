package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;
/**
 * (DATA TO OBJECT) Cette classe permet d'afficher un voisin au frontend
 */
@Data
@Builder
public class VoisinDTO {
    private IntersectionDTO destination;
    private String nomRue;
    private Double longueur;

}
