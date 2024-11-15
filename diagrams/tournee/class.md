````plantuml
@startuml

!theme plain
top to bottom direction
skinparam linetype ortho

class ClusterableLivraison {
  - points: double[]
  - livraison: Livraison
}
class DemandeLivraisons {
  - entrepot: Entrepot
  + builder(): DemandeLivraisonsBuilder
  + toDTO(): DemandeLivraisonsDTO
}
class ElemMatrice {
  - intersections: List<Intersection>
  - cout: double
  + builder(): ElemMatriceBuilder
}
class ParcoursDeLivraison {
  - livraisons: List<Livraison>
  - chemin: List<Intersection>
  - entrepot: Entrepot
  + builder(): ParcoursDeLivraisonBuilder
  + toDTO(): ParcoursDeLivraisonDTO
}
class TSPGraph {
  - tsp: TSP1
  - carte: Carte
  - livraisons: List<Livraison>
  - PRECISION: int
  - matrice: ElemMatrice[][]
  + builder(): TSPGraphBuilder
  + getSolution(): Integer[]
  + getCost(int, int): int
  + isArc(int, int): boolean
}
class Tournee {
  + chemin: List<Intersection>
  + builder(): TourneeBuilder
  + toDTO(): TourneeDTO
}
class TourneeLivraison {
  + parcoursDeLivraisons: List<ParcoursDeLivraison>
  + builder(): TourneeLivraisonBuilder
  + toDTO(): TourneeLivraisonDTO
}

TSPGraph             "1" *-[#595959,plain]-> "matrice\n*" ElemMatrice          
TourneeLivraison     "1" *-[#595959,plain]-> "parcoursDeLivraisons\n*" ParcoursDeLivraison  
@enduml

````