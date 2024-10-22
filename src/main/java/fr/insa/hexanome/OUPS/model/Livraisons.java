package fr.insa.hexanome.OUPS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Livraisons extends ArrayList<Livraison> {
    private Entrepot entrepot;

}
