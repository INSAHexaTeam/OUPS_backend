package fr.insa.hexanome.OUPS.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourneeDTO {
    public List<IntersectionDTO> chemin;
}
