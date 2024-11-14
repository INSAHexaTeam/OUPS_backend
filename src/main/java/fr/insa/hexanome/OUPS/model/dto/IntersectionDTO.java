package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IntersectionDTO {

    private Long id;
    private Double latitude;
    private Double longitude;
    private List<VoisinDTO> voisins;


}
