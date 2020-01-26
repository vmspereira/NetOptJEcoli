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
package jecoli.algorithm.components.operator.reproduction.permutation;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.permutations.PermutationRepresentation;
import jecoli.algorithm.components.representation.permutations.PermutationRepresentationFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class PermutationNonAdjacentSwapMutation.
 */
public class PermutationNonAdjacentSwapMutation extends AbstractPermutationMutationOperator {
	
	
	/**
	 * Positions between which the mutation is applied
	 */
	protected int minPosition;
	protected int maxPosition;


	/**
	 * 
	 */
	private static final long serialVersionUID = 320340013308073657L;

	
	
	public PermutationNonAdjacentSwapMutation(){
		this.minPosition = 0;
		this.maxPosition = Integer.MAX_VALUE;
	}
	
	public PermutationNonAdjacentSwapMutation(int minPosition,int maxPosition){
		this.minPosition = minPosition;
		this.maxPosition = maxPosition;
	}

	
	
	/* (non-Javadoc)
	 * @see operators.reproduction.permutations.AbstractPermutationMutationOperator#mutateGenome(core.representation.permutations.PermutationRepresentation)
	 */
	@Override
	protected void mutateGenome(PermutationRepresentation childGenome,PermutationRepresentationFactory solutionFactory,IRandomNumberGenerator randomNumberGenerator) 
	{
		int numberOfGenes = Math.min(childGenome.getNumberOfElements(),maxPosition)-minPosition;
		
		int pos, pos1;
		
		pos = (int) (randomNumberGenerator.nextDouble()*(numberOfGenes))+minPosition;
		do
		{
		  	pos1 = (int) (randomNumberGenerator.nextDouble()*(numberOfGenes))+minPosition;
		} while (pos == pos1);

		Integer aux = childGenome.getElement(pos);
		childGenome.setElement(pos, childGenome.getElement(pos1) );
		childGenome.setElement(pos1, aux );
	}

	@Override
	public PermutationNonAdjacentSwapMutation deepCopy() throws Exception {
		return new PermutationNonAdjacentSwapMutation(this.minPosition,this.maxPosition);
	}

}
