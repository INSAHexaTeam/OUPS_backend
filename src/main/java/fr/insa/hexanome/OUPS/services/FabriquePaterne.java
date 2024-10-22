package fr.insa.hexanome.OUPS.services;

import fr.insa.hexanome.OUPS.model.*;
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

        //TODO check if user can upload other type of doc



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
            Voisin voisin = Voisin.builder()
                    .nomRue(noeud.getAttributes().getNamedItem("nomRue").getNodeValue())
                    .destination(destination)
                    .longueur(Double.parseDouble(noeud.getAttributes().getNamedItem("longueur").getNodeValue()))
                    .build();
            origine.ajouterVoisin(voisin);
        }
        return this.carte;
    }
    public Livraisons chargerDemande(String cheminFichier) throws ParserConfigurationException, IOException, SAXException {
        if(carte == null || this.carte.getIntersections().isEmpty()){
            throw new IllegalArgumentException("Carte non chargée");
        }
        System.out.println(this.carte);
        File fichier = new File(cheminFichier);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fichier);
        doc.getDocumentElement().normalize();

        Node nodeEntrepot = doc.getElementsByTagName("entrepot").item(0);
        Long idEntrepot = Long.parseLong(nodeEntrepot.getAttributes().getNamedItem("adresse").getNodeValue());

        Intersection intersectionEntrepot = this.carte.trouverIntersectionParId(idEntrepot);

        Entrepot entrepot = Entrepot.builder()
                .intersection(intersectionEntrepot)
                .heureDepart(nodeEntrepot.getAttributes().getNamedItem("heureDepart").getNodeValue())
                .build();
        Livraisons livraisons = new Livraisons(entrepot);

        NodeList nodeListLivraisons = doc.getElementsByTagName("livraison");
        for(int i = 0; i < nodeListLivraisons.getLength(); i++){
            Node nodeLivraison = nodeListLivraisons.item(i);
            Long idLivraison = Long.parseLong(nodeLivraison.getAttributes().getNamedItem("adresseLivraison").getNodeValue());
            Intersection intersectionLivraison = this.carte.trouverIntersectionParId(idLivraison);
            Livraison livraison = Livraison.builder()
                    .adresseLivraison(intersectionLivraison)
                    .build();
            livraisons.add(livraison);
        }
        return livraisons;
    }
}
