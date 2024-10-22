package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Intersection;
import fr.insa.hexanome.OUPS.model.Voisin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Service
public class FabriquePaterne {
    private Carte carte;
    public FabriquePaterne() {
        this.carte = new Carte();
    }
    public Carte chargePlan(String cheminFichier) throws ParserConfigurationException, IOException, SAXException {
        this.carte = new Carte();
        File fichier = new File(cheminFichier);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fichier);
        doc.getDocumentElement().normalize();

        //Charger noeuds
        NodeList noeuds = doc.getElementsByTagName("noeud");
        for (int i = 0; i < noeuds.getLength(); i++) {
            Node noeud = noeuds.item(i);
            Intersection intersection = Intersection.builder()
                    .id(Long.parseLong(noeud.getAttributes().getNamedItem("id").getNodeValue()))
                    .longitude(Double.parseDouble(noeud.getAttributes().getNamedItem("longitude").getNodeValue()))
                    .latitude(Double.parseDouble(noeud.getAttributes().getNamedItem("latitude").getNodeValue()))
                    .voisins(new ArrayList<>())
                    .build();

            this.carte.ajouterIntersection(intersection);
        }

        //Charger Segments
        NodeList troncons = doc.getElementsByTagName("troncon");
        for (int i = 0; i < troncons.getLength(); i++) {

            Node noeud = troncons.item(i);
            Intersection origine = this.carte.trouverIntersectionParId(
                    Long.parseLong(noeud.getAttributes().getNamedItem("origine").getNodeValue())
            );
            Intersection destination = this.carte.trouverIntersectionParId(
                    Long.parseLong(noeud.getAttributes().getNamedItem("destination").getNodeValue())
            );
            //TODO modifier les intersections.get(0) et intersections.get(1) pour qu'ils utilisent le find by id de base (de carte)
            Voisin voisin = Voisin.builder()
                    .nomRue(noeud.getAttributes().getNamedItem("nomRue").getNodeValue())
                    .destination(destination)
                    .longueur(Double.parseDouble(noeud.getAttributes().getNamedItem("longueur").getNodeValue()))
                    .build();
            origine.ajouterVoisin(voisin);
        }
        return this.carte;
    }
    public void chargerDemande(String cheminFichier){


    }
}
