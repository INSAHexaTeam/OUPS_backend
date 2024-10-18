package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Intersection;
import fr.insa.hexanome.OUPS.model.Voisin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
public class FabriquePaterne {
    private Carte carte;
    public FabriquePaterne() {
        this.carte = new Carte();
    }
    public void chargePlan(String cheminFichier) throws ParserConfigurationException, IOException, SAXException {
        File fichier = new File(cheminFichier);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fichier);
        doc.getDocumentElement().normalize();

        //Charger noeuds
        NodeList noeuds = doc.getElementsByTagName("noeud");
        this.chargerIntersections(noeuds);

        //Charger Segments
        NodeList troncons = doc.getElementsByTagName("troncon");
        this.chargerSegment(troncons);


        System.out.println(this.carte);

    }
    private void chargerIntersections(NodeList noeuds) {
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
    }

    private void chargerSegment(NodeList nodeList){
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node noeud = nodeList.item(i);
            List<Intersection> intersections = this.carte.trouverIntersectionPourSegment(
                    Long.parseLong(noeud.getAttributes().getNamedItem("origine").getNodeValue()),
                    Long.parseLong(noeud.getAttributes().getNamedItem("destination").getNodeValue())
            );

            Voisin voisin = Voisin.builder()
                    .nomRue(noeud.getAttributes().getNamedItem("nomRue").getNodeValue())
                    .destination(intersections.get(1))
                    .longueur(Double.parseDouble(noeud.getAttributes().getNamedItem("longueur").getNodeValue()))
                    .build();
            intersections.get(0).ajouterVoisin(voisin);
        }
    }
}
