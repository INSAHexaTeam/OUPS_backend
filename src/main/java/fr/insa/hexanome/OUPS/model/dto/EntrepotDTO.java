// DTO pour Entrepot
package fr.insa.hexanome.OUPS.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntrepotDTO {
    private String heureDepart;
    private IntersectionDTO intersection;
}
