package fr.insa.hexanome.OUPS.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.insa.hexanome.OUPS.model.Entrepot;
import fr.insa.hexanome.OUPS.model.Livraison;
import fr.insa.hexanome.OUPS.model.dto.LivraisonsDTO;
import lombok.*;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Livraisons extends ArrayList<Livraison> {
    private Entrepot entrepot;

    public LivraisonsDTO toDTO() {
        return new LivraisonsDTO(this.entrepot, this);
    }
}