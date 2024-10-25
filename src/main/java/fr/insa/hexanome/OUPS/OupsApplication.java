package fr.insa.hexanome.OUPS;

import fr.insa.hexanome.OUPS.model.Carte;
import fr.insa.hexanome.OUPS.model.Livraisons;
import fr.insa.hexanome.OUPS.services.CalculeTourne;
import fr.insa.hexanome.OUPS.services.FabriquePaterne;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class OupsApplication {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		SpringApplication.run(OupsApplication.class, args);
		/*
		System.out.println("OupsApplication started");
		FabriquePaterne fabriquePaterne = new FabriquePaterne();
		// Code excecuté avant de tout
		String filePath = "src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml";
		fabriquePaterne.chargePlan(filePath);
		System.out.println("XML plan a eté telechargé...");
		String filePathDemande = "src/main/resources/fichiersXMLPickupDelivery/myDeliverRequestTest.xml";
		Livraisons livraisons = fabriquePaterne.chargerDemande(filePathDemande);
		System.out.println("XML demande a eté telechargé...");
		fabriquePaterne.getCarte().getIntersections()
		CalculeTourne calculeTourne = new CalculeTourne(livraisons);
		long startTime = System.currentTimeMillis();
		calculeTourne.searchSolution(2000);
		System.out.println(calculeTourne.getSolutionCost());
		for (int i = 0; i < livraisons.size(); i++) {
			System.out.println(calculeTourne.getSolution(i));
		}
		System.out.println("llll");
		*/
//		Ouvrir ressource petitPlan.xml :
//		FabriquePaterne fabriquePaterne = new FabriquePaterne();
//		try {
//			Carte c = fabriquePaterne.chargePlan("src/main/resources/fichiersXMLPickupDelivery/petitPlan.xml");
//			System.out.println(
//					c.getIntersections().getFirst()
//			);
//		} catch (ParserConfigurationException | IOException | SAXException e) {
//			e.printStackTrace();
//		}

	}

}
