package jecoli.algorithm.components.evolver;

import jecoli.algorithm.components.representation.IRepresentation;

/**
 * Evolves a single solution
 * 
 * @author vitor pereira
 *
 * @param <T>
 */

public interface IEvolve<T extends IRepresentation> {
	public T evolve(T solution);	
}
