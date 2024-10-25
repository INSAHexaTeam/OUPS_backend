package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import lombok.*;

import java.util.ArrayList;

/**
 * (Objet métier) Liste de livraisons qui part d'un entrepot
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Livraisons extends ArrayList<Livraison> {
    private Entrepot entrepot;

    /**
     * Convertis une Livraison en livraisonsDTO
     * @return
     */
    public LivraisonsDTO toDTO() {
        return new LivraisonsDTO(this.entrepot, this);
    }
}