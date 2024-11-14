package fr.insa.hexanome.OUPS.model.carte;

import fr.insa.hexanome.OUPS.model.dto.CarteDTO;
import fr.insa.hexanome.OUPS.model.dto.IntersectionDTO;
import fr.insa.hexanome.OUPS.model.dto.VoisinDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

/**
 * (Objet métier) permet la manipulation de données de type carte
 */
@Data
@AllArgsConstructor
@Builder
public class Carte {
    private List<Intersection> intersections;

    public Map<Long,Intersection> getIntersectionsMap(){
        Map<Long,Intersection> intersectionsMap = new HashMap<>();
        for(Intersection i : this.intersections){
            intersectionsMap.put(i.getId(),i);
        }
        return intersectionsMap;
    }
    public Carte() {
        this.intersections = new ArrayList<>();
    }

    /**
     * Cette classe permet d'ajouter une intersection a la liste des intersections de la carte
     * @param intersection objet à ajouter
     */
    public void ajouterIntersection(Intersection intersection) {
        this.intersections.add(intersection);
    }

    /**
     * trouver une intersection dans la liste des intersections de la carte
     * @param id Id de l'objet à chercher dans la liste
     * @return Intersection trouvée
     * @throws IllegalArgumentException throws une erreur si l'intersection n'a pas été trouvée. Cela signifie que le plan à mal été chargé
     */
    public Intersection trouverIntersectionParId(Long id) {
        for (Intersection intersection : this.intersections) {
            if (Objects.equals(intersection.getId(), id)) {
                return intersection;
            }
        }
        throw new IllegalArgumentException("Intersection non trouvée");
    }

    /**
     * Transforme l'objet carte en DTO
     * @return la Carte transformée en CarteDTO
     */
    public CarteDTO toDTO(){

        ArrayList<IntersectionDTO> intersectionDTOS = new ArrayList<>();

        for (Intersection i : this.intersections) {
            ArrayList<VoisinDTO> voisinsDTOS = new ArrayList<>();
            if(i.getVoisins() == null){
                continue;
            }
            for(Voisin v: i.getVoisins()){
                IntersectionDTO voisin = IntersectionDTO.builder()
                        .id(v.getDestination().getId())
                        .latitude(v.getDestination().getLatitude())
                        .longitude(v.getDestination().getLongitude())
                        .voisins(null)
                        .build();
                VoisinDTO voisinDTO = VoisinDTO.builder()
                        .destination(voisin)
                        .longueur(v.getLongueur())
                        .nomRue(v.getNomRue())
                        .build();
                voisinsDTOS.add(voisinDTO);
            }
            IntersectionDTO intersectionDTO = IntersectionDTO.builder()
                    .id(i.getId())
                    .voisins(voisinsDTOS)
                    .longitude(i.getLongitude())
                    .latitude(i.getLatitude())
                    .build();
            intersectionDTOS.add(intersectionDTO);
        }
        return CarteDTO.builder()
                .intersections(intersectionDTOS)
                .build();
    }
}
