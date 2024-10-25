package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * (DATA TO OBJECT) Cette classe permet d'afficher une Carte au frontend
 */
@Data
@Builder
public class CarteDTO {
    private List<IntersectionDTO> intersections;

}
