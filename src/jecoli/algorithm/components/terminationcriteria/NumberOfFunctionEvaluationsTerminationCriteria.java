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
package jecoli.algorithm.components.terminationcriteria;

import jecoli.algorithm.components.algorithm.AlgorithmState;
import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.representation.IRepresentation;


/**
 * The Class NumberOfFunctionEvaluationsTerminationCriteria.
 */
public class NumberOfFunctionEvaluationsTerminationCriteria implements ITerminationCriteria{
	
	/** The maximum number of function evaluations. */
	protected int maximumNumberOfFunctionEvaluations;

	/**
	 * Instantiates a new number of function evaluations termination criteria.
	 * 
	 * @param maximumNumberOfFunctionEvaluations the maximum number of function evaluations
	 * @throws InvalidTerminationCriteriaParameter 
	 */
	public NumberOfFunctionEvaluationsTerminationCriteria(int maximumNumberOfFunctionEvaluations) throws InvalidTerminationCriteriaParameter {
		if(maximumNumberOfFunctionEvaluations < 0)
			throw new InvalidTerminationCriteriaParameter("maximumNumberOfFunctionEvaluations < 0 ");
		this.maximumNumberOfFunctionEvaluations = maximumNumberOfFunctionEvaluations;
	}

	@Override
	public <T extends IRepresentation> boolean verifyAlgorithmTermination(IAlgorithm<T> algorithm, AlgorithmState<T> algorithmState) {
		
		int totalNumberOfFunctionEvaluation = algorithmState.getTotalNumberOfFunctionEvaluations();
		
		if(totalNumberOfFunctionEvaluation >= maximumNumberOfFunctionEvaluations)
			return true;
		
		return false;
	}

	@Override
	public ITerminationCriteria deepCopy() throws InvalidTerminationCriteriaParameter {
		return new NumberOfFunctionEvaluationsTerminationCriteria(maximumNumberOfFunctionEvaluations);
	}

}
