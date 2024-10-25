package fr.insa.hexanome.OUPS.model;

import lombok.*;

import java.util.Date;

/**
 * (Objet métier) Représentation d'un entrepot
 */
@Data
@Builder
public class Entrepot  {
    private String heureDepart;
    private Intersection intersection;

}
