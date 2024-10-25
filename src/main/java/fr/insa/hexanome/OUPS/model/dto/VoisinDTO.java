package fr.insa.hexanome.OUPS.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.insa.hexanome.OUPS.model.Intersection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoisinDTO {
    private IntersectionDTO destination;
    private String nomRue;
    private Double longueur;

}
