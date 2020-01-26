package jecoli.algorithm.components.evolver;

import jecoli.algorithm.components.representation.IRepresentation;

public class DefaultEvolver<T extends IRepresentation> implements IEvolve<T> {

	
	@Override
	public T evolve(T solution) {
		return solution;
	}

}
