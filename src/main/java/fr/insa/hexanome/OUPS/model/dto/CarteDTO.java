package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class CarteDTO {
    private List<IntersectionDTO> intersections;

}
