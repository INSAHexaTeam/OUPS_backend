package fr.insa.hexanome.OUPS.model;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
public class Entrepot extends Intersection{
    private Integer heureDepart;
    public Entrepot(Long id, Double latitude, Double longitude, Integer heureDepart) {
        super(id, latitude, longitude);
        this.heureDepart = heureDepart;
    }

}
