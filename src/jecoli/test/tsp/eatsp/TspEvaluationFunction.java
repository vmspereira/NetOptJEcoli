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
package jecoli.test.tsp.eatsp;

import jecoli.algorithm.components.evaluationfunction.AbstractEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import jecoli.algorithm.components.representation.permutations.PermutationRepresentation;
import jecoli.test.tsp.libtsp.Tsp;


/**
 * The Class TspEvaluationFunction: fitness function for the Traveling Salesman problem.
 */
public class TspEvaluationFunction extends AbstractEvaluationFunction<PermutationRepresentation> 
{
	
	/** The problem instance. */
	Tsp problemInstance;
	
	/**
	 * Instantiates a new tsp evaluation function.
	 * 
	 * @param problemInstance the problem instance
	 */
	public TspEvaluationFunction(Tsp problemInstance){
		super(false);  // minimization
		this.problemInstance = problemInstance;
	}
	

	@Override
	public double evaluate(PermutationRepresentation solution){
		int [] genome = solution.getGenomeAsArray();
		double fitness = problemInstance.cost(genome);
		return fitness;
	}

	@Override
	public IEvaluationFunction<PermutationRepresentation> deepCopy() {
		return new TspEvaluationFunction(problemInstance);
	}


	@Override
	public void verifyInputData()
			throws InvalidEvaluationFunctionInputDataException {
		// TODO Auto-generated method stub
		
	}
}
