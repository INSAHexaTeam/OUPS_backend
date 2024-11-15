````plantuml
@startuml

!theme plain
top to bottom direction
skinparam linetype ortho

class CompleteGraph {
  ~ nbVertices: int
  ~ cost: int[][]
  - MAX_COST: int
  - MIN_COST: int
  + getCost(int, int): int
  + getNbVertices(): int
  + isArc(int, int): boolean
}
interface Graph << interface >> {
  + getCost(int, int): int
  + getNbVertices(): int
  + isArc(int, int): boolean
}
class RunTSP {
  + main(String[]): void
}
class SeqIter {
  - candidates: Integer[]
  - nbCandidates: int
  + next(): Integer
  + hasNext(): boolean
  + remove(): void
}
interface TSP << interface >> {
  + getSolution(int): Integer
  + searchSolution(int, Graph): void
  + getSolutionCost(): int
}
class TSP1 {
  # bound(Integer, Collection<Integer>): int
  # iterator(Integer, Collection<Integer>, Graph): Iterator<Integer>
}
class TemplateTSP {
  # g: Graph
  - timeLimit: int
  - bestSol: Integer[]
  - bestSolCost: int
  - startTime: long
  + getFullSolution(): Integer[]
  # iterator(Integer, Collection<Integer>, Graph): Iterator<Integer>
  - branchAndBound(int, Collection<Integer>, Collection<Integer>, int): void
  + getSolutionCost(): int
  # bound(Integer, Collection<Integer>): int
  + searchSolution(int, Graph): void
  + getSolution(int): Integer
}

CompleteGraph  -[#008200,dashed]-^  Graph         
RunTSP         -[#595959,dashed]->  CompleteGraph : "«create»"
RunTSP         -[#595959,dashed]->  TSP1          : "«create»"
TSP1           -[#595959,dashed]->  SeqIter       : "«create»"
TSP1           -[#000082,plain]-^  TemplateTSP   
TemplateTSP   "1" *-[#595959,plain]-> "g\n1" Graph         
TemplateTSP    -[#008200,dashed]-^  TSP           
@enduml

````