package fr.insa.hexanome.OUPS.model.tournee;

import fr.insa.hexanome.OUPS.model.carte.Entrepot;
import fr.insa.hexanome.OUPS.model.carte.Livraison;
import fr.insa.hexanome.OUPS.model.dto.DemandeLivraisonsDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class DemandeLivraisons extends ArrayList<Livraison> {
    private Entrepot entrepot;

    public DemandeLivraisons(Entrepot entrepot) {
        super();
        this.entrepot = entrepot;
    }

    public DemandeLivraisonsDTO toDTO() {
        return DemandeLivraisonsDTO.builder()
                .entrepot(entrepot.toDTO())
                .livraisons(this.stream()
                        .map(Livraison::toDTO)
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }

    public ArrayList<DemandeLivraisons> split(int nbCoursiers) {
        ArrayList<DemandeLivraisons> result = new ArrayList<>();
        for (int i = 0; i < nbCoursiers; i++) {
            result.add(new DemandeLivraisons(this.entrepot));
        }

        int nbElementParListe = this.size() / nbCoursiers;
        int nbElementRestant = this.size() % nbCoursiers;
        int index = 0;

        // Distribution des livraisons de base
        for (int i = 0; i < nbCoursiers; i++) {
            for (int j = 0; j < nbElementParListe; j++) {
                Livraison l = this.get(index++);
                result.get(i).add(l);
            }
        }

        // Distribution des livraisons restantes aux premières sous-listes
        for (int j = 0; j < nbElementRestant; j++) {
            Livraison l = this.get(index++);
            result.get(j).add(l);
        }

        return result;
    }


}