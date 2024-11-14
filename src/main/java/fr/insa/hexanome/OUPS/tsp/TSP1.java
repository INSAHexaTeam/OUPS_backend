package fr.insa.hexanome.OUPS.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * TSP sous le meme format que le template
 */
public class TSP1 extends TemplateTSP {
	@Override
	protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
