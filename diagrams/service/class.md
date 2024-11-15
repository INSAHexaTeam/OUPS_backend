````plantuml
@startuml

!theme plain
top to bottom direction
skinparam linetype ortho

class CalculItineraire {
  - entrepot: Entrepot
  - livraisons: List<Livraison>
  - carte: Carte
  - matrice: ElemMatrice[][]
  + builder(): CalculItineraireBuilder
  + calculDijkstra(): void
  + dijkstraEntreDeuxPoints(Intersection, Intersection, Carte): ElemMatrice
}
enum EtatType << enumeration >> {
  + ENREGISTREMENT_SIMPLE: 
  + ENREGISTREMENT: 
  + CHARGEMENT: 
}
class FabriquePaterne {
  - carte: Carte
  + builder(): FabriquePaterneBuilder
  + chargePlan(String): Carte
  + chargerDemande(String): DemandeLivraisons
}
class GestionService {
  - fabriquePaterne: FabriquePaterne
  - CHEMIN_DEFAULT: String
  - enregistrement(MultipartFile): void
  + chargerCarteDepuisXML(EtatType, MultipartFile, String): Carte
  + chargerLivraisonsDepuisXML(EtatType, MultipartFile, String): DemandeLivraisons
}
class ItineraireService {
  + trouverCheminEntreDeuxIntersections(Intersection, Intersection, Map<Long, Intersection>): List<Intersection>
}

GestionService    "1" *-[#595959,plain]-> "fabriquePaterne\n1" FabriquePaterne   
GestionService     -[#595959,dashed]->  FabriquePaterne   : "«create»"
@enduml

````