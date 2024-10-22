package fr.insa.hexanome.OUPS.model;

import lombok.*;

import java.util.Date;
@Data
@Builder
public class Entrepot  {
    private String heureDepart;
    private Intersection intersection;

}
