/**
* Copyright 2009,
* CCTC - Computer Science and Technology Center
* IBB-CEB - Institute for Biotechnology and  Bioengineering - Centre of Biological Engineering
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* Created inside the SysBio Research Group <http://sysbio.di.uminho.pt/>
* University of Minho
*/
package jecoli.algorithm.components.operator.reproduction.linear;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentationFactory;
import jecoli.algorithm.components.representation.linear.LinearRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.Solution;


// TODO: Auto-generated Javadoc
/**
 * The Class TwoPointCrossOver.
 */
public class TwoPointCrossOver<G> extends AbstractCrossoverOperator<ILinearRepresentation<G>,ILinearRepresentationFactory<G>> { 
	

	private static final long serialVersionUID = 2298870674231448690L;

	protected int minPosition;
	protected int maxPosition;

	
	public TwoPointCrossOver(){
		this.minPosition = 0;
		this.maxPosition = Integer.MAX_VALUE;
	}
	
	public TwoPointCrossOver(int minPosition,int maxPosition){
		this.minPosition=minPosition;
		this.maxPosition = maxPosition;
	}
	
	protected List<ISolution<ILinearRepresentation<G>>> crossOverGenomes(ILinearRepresentation<G> parentGenome,ILinearRepresentation<G> parent1Genome,ILinearRepresentationFactory<G> solutionFactory,IRandomNumberGenerator randomNumberGenerator) 
	{	
		int parentGenomeSize = parentGenome.getNumberOfElements();
		int parent1GenomeSize = parent1Genome.getNumberOfElements();
		
		int finalSize = Math.min(maxPosition,Math.min(parentGenomeSize,parent1GenomeSize))-minPosition;

		int crossoverPosition = (int) (randomNumberGenerator.nextDouble()*(finalSize-2))+minPosition;
		int crossoverPosition1 = (int) (randomNumberGenerator.nextDouble()*(finalSize-2))+minPosition;

		int position = Math.min(crossoverPosition,crossoverPosition1);
		int position1 = Math.max(crossoverPosition,crossoverPosition1);
		
		List<G> childGenome = new ArrayList<G>();	
		List<G> childGenome1 = new ArrayList<G>();

		for(int i=0; i< position; i++){
			G parentValue = parentGenome.getElementAt(i);
			childGenome.add(parentValue);
			G parent1Value = parent1Genome.getElementAt(i);
			childGenome1.add(parent1Value);
		}

		for(int i = position; i<position1; i++){
			childGenome.add(parent1Genome.getElementAt(i));
			childGenome1.add(parentGenome.getElementAt(i));
		}

		for(int i = position1; i< parent1GenomeSize; i++)
			childGenome.add(parent1Genome.getElementAt(i));

		for(int i = position1; i < parentGenomeSize;i++)
			childGenome1.add(parentGenome.getElementAt(i));

		int numObjectives = solutionFactory.getNumberOfObjectives();

		ISolution<ILinearRepresentation<G>> childSolution = new Solution<ILinearRepresentation<G>>(new LinearRepresentation<G>(childGenome),numObjectives);
		ISolution<ILinearRepresentation<G>> child1Solution = new Solution<ILinearRepresentation<G>>(new LinearRepresentation<G>(childGenome1),numObjectives);
		
		List<ISolution<ILinearRepresentation<G>>> solutionList = new ArrayList<ISolution<ILinearRepresentation<G>>>();
		solutionList.add(child1Solution); solutionList.add(childSolution);
		return solutionList;
		
	}

	/* (non-Javadoc)
	 * @see operators.reproduction.linear.AbstractCrossoverOperator#getNumberOfInputSolutions()
	 */
	@Override
	public int getNumberOfInputSolutions() {
		return NUMBER_OF_INPUT_SOLUTIONS;
	}

	/* (non-Javadoc)
	 * @see operators.reproduction.linear.AbstractCrossoverOperator#getNumberOfOutputSolutions()
	 */
	@Override
	public int getNumberOfOutputSolutions() {
		return NUMBER_OF_OUTPUT_SOLUTIONS;
	}


	@Override
	public TwoPointCrossOver<G> deepCopy() {
		return new TwoPointCrossOver<G>(this.minPosition,this.maxPosition);
	}

	public void setApplicationRange(int min,int max){
		this.minPosition=min;
		this.maxPosition=max;
	}
}
