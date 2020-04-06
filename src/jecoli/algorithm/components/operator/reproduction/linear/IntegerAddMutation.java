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
* 
* 
* Vitor 08/02/2017 corrigido 
*/
package jecoli.algorithm.components.operator.reproduction.linear;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.integer.IntegerRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentationFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class IntegerAddMutation.
 */
@SuppressWarnings("serial")
public class IntegerAddMutation extends AbstractMutationOperator<ILinearRepresentation<Integer>,ILinearRepresentationFactory<Integer>> {
	
	
	
	/** The maximum of number genes to mutate.
	 *  At least one gene is always mutated. 
	 **/
	int numberGenesToMutate = 1; 
	
	/**
	 * Positions between which the mutation is applied
	 */
	protected int minPosition;
	protected int maxPosition;

	
	public IntegerAddMutation(int numberGenesToMutate){
		this.numberGenesToMutate = numberGenesToMutate;
		this.minPosition = 0;
		this.maxPosition = Integer.MAX_VALUE;
	}
	
	
	public IntegerAddMutation(int numberGenesToMutate, int minPosition,int maxPosition){
		this.numberGenesToMutate = numberGenesToMutate;
		this.minPosition = minPosition;
		this.maxPosition = maxPosition;
	}
	
	@Override
	protected void mutateGenome(ILinearRepresentation<Integer> childGenome,ILinearRepresentationFactory<Integer> solutionFactory,IRandomNumberGenerator randomNumberGenerator) 
	{

		int size = Math.min(childGenome.getNumberOfElements(),this.maxPosition)-this.minPosition;
		int n = 1;
		if(this.numberGenesToMutate>1) 
			n = (int) (randomNumberGenerator.nextDouble()*(this.numberGenesToMutate-1)+1); 

	
		for(int j=0; j<n; j++)
		{
			int pos = (int) (randomNumberGenerator.nextDouble()*(size))+this.minPosition;
			
			int maxValue = ((IntegerRepresentationFactory)solutionFactory).getUpperBoundGeneLimitList().get(pos);
			int minValue = ((IntegerRepresentationFactory)solutionFactory).getLowerBoundGeneLimitList().get(pos);
			
			int value=childGenome.getElementAt(pos);
			if( randomNumberGenerator.nextDouble() > 0.5) 
			if( randomNumberGenerator.nextDouble() > 0.5) 
			{	
				value++;
				if(value>maxValue)
					value = minValue;
			}
			else{
				value--;
				if(value<minValue)
					value = maxValue;
			}
			childGenome.setElement(pos,value);
		}	
	}

	@Override
	public IntegerAddMutation deepCopy() {
		return new IntegerAddMutation(numberGenesToMutate,this.minPosition,this.maxPosition);
	}
	
	
	public void setApplicationRange(int min,int max){
		this.minPosition=min;
		this.maxPosition=max;
	}
}
