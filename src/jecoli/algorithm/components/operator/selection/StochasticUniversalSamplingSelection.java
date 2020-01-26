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
package jecoli.algorithm.components.operator.selection;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.operator.ISelectionOperator;
import jecoli.algorithm.components.operator.InvalidSelectionParameterException;
import jecoli.algorithm.components.operator.InvalidSelectionProcedureException;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;


// TODO: Auto-generated Javadoc
//PRE: orderedSolutionSet
/**
 * The Class StochasticUniversalSamplingSelection.
 */
public class StochasticUniversalSamplingSelection<T extends IRepresentation> implements ISelectionOperator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -82982404228963016L;

	@Override
	public 	List<ISolution<T>> selectSolutions(int numberOfSolutionsToSelect, ISolutionSet<T> solutionSet,boolean isMaximization, IRandomNumberGenerator randomNumberGenerator) throws InvalidSelectionProcedureException,	InvalidSelectionParameterException{
		if(numberOfSolutionsToSelect < 0)
			throw new InvalidSelectionParameterException(" numberOfSolutionToSelect < 0");
		
		double overallFitnessValue = solutionSet.calculateOverallFitness();
		double pointerDistance = overallFitnessValue/numberOfSolutionsToSelect;
		double pointerStartValue = randomNumberGenerator.nextDouble()*pointerDistance;
		
		return selectSolutionList(pointerStartValue,numberOfSolutionsToSelect,solutionSet,isMaximization);
	}

	/**
	 * Select solution list.
	 * 
	 * @param pointerStartValue the pointer start value
	 * @param numberOfSolutionsToSelect the number of solutions to select
	 * @param solutionSet the solution set
	 * @param isMaximization the is maximization
	 * 
	 * @return the list< i solution>
	 */
	protected List<ISolution<T>> selectSolutionList(double pointerStartValue,int numberOfSolutionsToSelect, ISolutionSet<T> solutionSet,boolean isMaximization) {
		List<ISolution<T>> solutionList = new ArrayList<ISolution<T>>(numberOfSolutionsToSelect);
		double currentFitnessValue = pointerStartValue;
		double currentSolutionFitnessValue = 0;
		int currentNumberOfSolutions = 0;
		int totalNumberOfSolutions = solutionSet.getNumberOfSolutions();
		int index = (totalNumberOfSolutions-1);
		
		ISolution<T> solution = null;
		
		while(currentNumberOfSolutions < numberOfSolutionsToSelect){
			solution = solutionSet.getSolution(index);
			currentSolutionFitnessValue += solution.getScalarFitnessValue();
			
			if(currentSolutionFitnessValue >= currentFitnessValue){
				currentFitnessValue +=  pointerStartValue;
				currentNumberOfSolutions++;
				solutionList.add(solution);
			}
			index--;
		}
		
		return solutionList;
	}

	@Override
	public StochasticUniversalSamplingSelection<T> deepCopy() {
		return new StochasticUniversalSamplingSelection<T>();
	}
	
	

}
