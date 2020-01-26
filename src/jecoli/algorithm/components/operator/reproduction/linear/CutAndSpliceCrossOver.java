package jecoli.algorithm.components.operator.reproduction.linear;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentationFactory;
import jecoli.algorithm.components.representation.linear.LinearRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.Solution;


public class CutAndSpliceCrossOver<G> extends AbstractCrossoverOperator<ILinearRepresentation<G>,ILinearRepresentationFactory<G>> {

	

	protected List<ISolution<ILinearRepresentation<G>>> crossOverGenomes(ILinearRepresentation<G> parentGenome,ILinearRepresentation<G> parent1Genome,
			                                                             ILinearRepresentationFactory<G> solutionFactory,IRandomNumberGenerator randomNumberGenerator) {
		int parentGenomeSize = parentGenome.getNumberOfElements();
		int parent1GenomeSize = parent1Genome.getNumberOfElements();
		
		int parentCrossoverPosition = (int) (randomNumberGenerator.nextDouble()*(parentGenomeSize-1))+1; 
		int parent1CrossoverPosition = (int) (randomNumberGenerator.nextDouble()*(parent1GenomeSize-1))+1; 
		
		
		List<G> childGenome = new ArrayList<G>();	
		List<G> childGenome1 = new ArrayList<G>();

		for(int i=0; i< parentCrossoverPosition; i++){
			G parentValue = parentGenome.getElementAt(i);
			childGenome.add(parentValue);
		}
		
		int childEndPosition = parentCrossoverPosition+ (parent1GenomeSize-parent1CrossoverPosition);
		for(int j = parentCrossoverPosition; j < childEndPosition;j++){
			int parent1GeneIndex = parent1CrossoverPosition+(j-parentCrossoverPosition);
			G parent1Value = parent1Genome.getElementAt(parent1GeneIndex);
			childGenome.add(parent1Value);
		}
		
		
		for(int i=0; i< parent1CrossoverPosition; i++){
			G parent1Value = parent1Genome.getElementAt(i);
			childGenome1.add(parent1Value);
		}
		
		int child1EndPosition = parent1CrossoverPosition +(parentGenomeSize-parentCrossoverPosition);
		for(int j = parent1CrossoverPosition; j < child1EndPosition;j++){
			int parentGeneIndex = parentCrossoverPosition+(j-parent1CrossoverPosition);
			G parentValue = parentGenome.getElementAt(parentGeneIndex);
			childGenome1.add(parentValue);
		}
		
		ISolution<ILinearRepresentation<G>> childSolution = new Solution<ILinearRepresentation<G>>(new LinearRepresentation<G>(childGenome));
		ISolution<ILinearRepresentation<G>> child1Solution = new Solution<ILinearRepresentation<G>>(new LinearRepresentation<G>(childGenome1));
		List<ISolution<ILinearRepresentation<G>>> solutionList = new ArrayList<ISolution<ILinearRepresentation<G>>>();
		solutionList.add(child1Solution); solutionList.add(childSolution);
		return solutionList;
	}

	@Override
	public CutAndSpliceCrossOver<G>  deepCopy() {
		return new CutAndSpliceCrossOver<G>();
	}
}
