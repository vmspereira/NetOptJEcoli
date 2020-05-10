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
/**
 * The Class RandomSelection.
 */
public class RandomSelection<T extends IRepresentation> implements ISelectionOperator<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1183623843121520154L;
	/** The select with repetitions. */
	protected boolean selectWithRepetitions;

	/**
	 * Instantiates a new random selection.
	 * 
	 * @param selectWithRepetitions the select with repetitions
	 */
	public RandomSelection(boolean selectWithRepetitions){
		this.selectWithRepetitions = selectWithRepetitions;
	}

	@Override
	public 	List<ISolution<T>> selectSolutions(int numberOfSolutionsToSelect, ISolutionSet<T> solutionSet,boolean isMaximization, IRandomNumberGenerator randomNumberGenerator) throws InvalidSelectionProcedureException,	InvalidSelectionParameterException{
		if(numberOfSolutionsToSelect < 0)
			throw new InvalidSelectionParameterException(" numberOfSolutionToSelect < 0");

		List<ISolution<T>> selectedSolutions = new ArrayList<ISolution<T>>(numberOfSolutionsToSelect);
		if(selectWithRepetitions)
			selectSolutionsWithRepetitions(numberOfSolutionsToSelect,solutionSet,selectedSolutions,randomNumberGenerator);
		else
			selectSolutionsWithoutRepetition(numberOfSolutionsToSelect,solutionSet,selectedSolutions,randomNumberGenerator);
		return selectedSolutions;
	}

	/**
	 * Select solutions without repetition.
	 * 
	 * @param numberOfSolutionsToSelect the number of solutions to select
	 * @param solutionSet the solution set
	 * @param selectedSolutions the selected solutions
	 * @param randomNumberGenerator 
	 */
	protected void selectSolutionsWithoutRepetition(int numberOfSolutionsToSelect,ISolutionSet<T> solutionSet,List<ISolution<T>> selectedSolutions, IRandomNumberGenerator randomNumberGenerator){
		List<Integer> indexPositionsList = new ArrayList<Integer>(solutionSet.getNumberOfSolutions());
		generateIndexPositionList(solutionSet.getNumberOfSolutions(),indexPositionsList,randomNumberGenerator);

		for(int i = 0;i<numberOfSolutionsToSelect;i++){
			int index = indexPositionsList.get(i);
			ISolution<T> solution = solutionSet.getSolution(index);
			selectedSolutions.add(solution);
		}
	}

	/**
	 * Generate index position list.
	 * 
	 * @param numberOfSolutions the number of solutions
	 * @param indexPositionsList the index positions list
	 * @param randomNumberGenerator 
	 */
	protected void generateIndexPositionList(int numberOfSolutions, List<Integer> indexPositionsList, IRandomNumberGenerator randomNumberGenerator) {
		for(int i = 0; i< numberOfSolutions;i++)
			indexPositionsList.add(i);

		for(int j = 0;j < numberOfSolutions;j++){
			int randomIndex = (int) (randomNumberGenerator.nextDouble()*numberOfSolutions);
			int tmpValue = indexPositionsList.get(randomIndex);
			int jPositionValue = indexPositionsList.get(j);
			indexPositionsList.set(randomIndex,jPositionValue);
			indexPositionsList.set(j,tmpValue);
		}


	}

	/**
	 * Select solutions with repetitions.
	 * 
	 * @param numberOfSolutionsToSelect the number of solutions to select
	 * @param solutionSet the solution set
	 * @param selectedSolutions the selected solutions
	 * @param randomNumberGenerator 
	 */
	protected void selectSolutionsWithRepetitions(int numberOfSolutionsToSelect,ISolutionSet<T> solutionSet,List<ISolution<T>> selectedSolutions, IRandomNumberGenerator randomNumberGenerator){
		for(int i=0;i< numberOfSolutionsToSelect;i++){
			int index =  (int)(randomNumberGenerator.nextDouble()*solutionSet.getNumberOfSolutions());
			selectedSolutions.add(solutionSet.getSolution(index));
		}
	}

	@Override
	public RandomSelection<T> deepCopy() {
		return new RandomSelection<T>(selectWithRepetitions);
	}

}