package fr.insa.hexanome.OUPS.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.insa.hexanome.OUPS.model.Voisin;
import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
 * (DATA TO OBJECT) Cette classe permet d'afficher une Intersection au frontend
 */
@Data
@Builder
public class IntersectionDTO {

    private Long id;
    private Double latitude;
    private Double longitude;
    private List<VoisinDTO> voisins;


}
