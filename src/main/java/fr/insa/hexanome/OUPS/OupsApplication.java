package fr.insa.hexanome.OUPS;

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

	public static void main(String[] args) {
//		SpringApplication.run(OupsApplication.class, args);
//		Ouvrir ressource petitPlan.xml :
		FabriquePaterne fabriquePaterne = new FabriquePaterne();
		try {
			fabriquePaterne.chargePlan("src/main/resources/fichiersXMLPickupDelivery/grandPlan.xml");
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}

	}

}
