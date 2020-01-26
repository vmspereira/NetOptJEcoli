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

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentationFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class LinearGenomeGrowMutation.
 */
public class LinearGenomeGrowMutation<E> extends AbstractMutationOperator<ILinearRepresentation<E>,ILinearRepresentationFactory<E>>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 524079788457372369L;
	/** The number genes to add. */
	int numberGenesToAdd = 1;
		
	/**
	 * Instantiates a new linear genome grow mutation.
	 * 
	 * @param solutionFactory the solution factory
	 * @param numberGenesToAdd the number genes to add
	 */
	public LinearGenomeGrowMutation(int numberGenesToAdd){
		this.numberGenesToAdd = numberGenesToAdd;
	}
	

	@Override
	protected void mutateGenome(ILinearRepresentation<E> childGenome,ILinearRepresentationFactory<E> solutionFactory ,IRandomNumberGenerator randomNumberGenerator) 
	{
		int numberOfGenes = childGenome.getNumberOfElements();
		int genesToAdd = 1;
	 	if(this.numberGenesToAdd > 1) genesToAdd = this.numberGenesToAdd;
	 	
	 	int N = (int) (randomNumberGenerator.nextDouble()*(genesToAdd)+1);
	 
	 	for(int k=0; k<N; k++)
	 	{
	 		int pos = (int) (randomNumberGenerator.nextDouble()*(numberOfGenes+k));
	 		E newGeneValue = solutionFactory.generateGeneValue(0,randomNumberGenerator);
	 		childGenome.addElementAt(pos, newGeneValue);
	 	} 	
	}

	/**
	 * Gets the number genes to add.
	 * 
	 * @return the number genes to add
	 */
	public int getNumberGenesToAdd() {
		return numberGenesToAdd;
	}

	/**
	 * Sets the number genes to add.
	 * 
	 * @param numberGenesToAdd the new number genes to add
	 */
	public void setNumberGenesToAdd(int numberGenesToAdd) {
		this.numberGenesToAdd = numberGenesToAdd;
	}

	@Override
	public LinearGenomeGrowMutation<E> deepCopy() {
		return new LinearGenomeGrowMutation<E>(numberGenesToAdd);
	}

}
