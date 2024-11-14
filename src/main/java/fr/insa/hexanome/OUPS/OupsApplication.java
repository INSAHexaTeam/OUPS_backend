package fr.insa.hexanome.OUPS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class OupsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OupsApplication.class, args);
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
