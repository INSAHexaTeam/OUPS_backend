package fr.insa.hexanome.OUPS.model.exception;

import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.model.dto.TourneeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

//La tournée est l'ensemble des livraisons à effectuer
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tournee {
    //chaque élément de la liste correspond à un coursier
    public List<Livraisons> livraisons;

    public TourneeDTO toDTO() {
        return TourneeDTO.builder()
                //transforme chaque livraison de la liste en livraisonDTO
                .livraisons(livraisons.stream().map(Livraisons::toDTO).collect(Collectors.toList()))
                .build();
    }
}
