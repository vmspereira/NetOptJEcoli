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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import jecoli.algorithm.components.operator.ISelectionOperator;
import jecoli.algorithm.components.operator.InvalidSelectionParameterException;
import jecoli.algorithm.components.operator.InvalidSelectionProcedureException;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;


/**
 * The Class TournamentSelection.
 */
public class TournamentSelection<T extends IRepresentation> implements ISelectionOperator<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3910808335308388027L;

	/** The k. */
	protected int k;
	
	/** The number of solutions per tournment. */
	protected int numberOfSolutionsPerTournment;

	/**
	 * Instantiates a new tournament selection.
	 * 
	 * @param k the k
	 * @param numberOfSolutionsPerTournment the number of solutions per tournment
	 * 
	 * @throws InvalidSelectionParameterException the invalid selection parameter exception
	 */
	public TournamentSelection(int k,int numberOfSolutionsPerTournment) throws InvalidSelectionParameterException {
		if(k < 1)
			throw new InvalidSelectionParameterException("k < 0");

		if(numberOfSolutionsPerTournment < 1)
			throw new InvalidSelectionParameterException("numberOfSolutionsPerTournment < 1");

		if(k > numberOfSolutionsPerTournment)
			throw new InvalidSelectionParameterException("k > numberOfSolutionsPerTournment");

		
		this.k = k;
		this.numberOfSolutionsPerTournment = numberOfSolutionsPerTournment;
	}


	/* (non-Javadoc)
	 * @see operators.ISelectionOperator#selectSolutions(int, core.interfaces.ISolutionSet, boolean)
	 */
	@Override
	public List<ISolution<T>> selectSolutions(int numberOfSolutionsToSelect,ISolutionSet<T> solutionSet, boolean isMaximization, IRandomNumberGenerator randomNumberGenerator) throws InvalidSelectionProcedureException, InvalidSelectionParameterException {
		if(numberOfSolutionsToSelect < 0)
			throw new InvalidSelectionParameterException(" numberOfSolutionToSelect < 0");

		if(k > numberOfSolutionsToSelect)
			throw new InvalidSelectionParameterException("k > numberOfSolutionToSelect");

		int modNumberSolutions = numberOfSolutionsToSelect % k;

		if(modNumberSolutions != 0)
			throw new InvalidSelectionParameterException(" numberOfSolutionsToSelect mod k != 0");

		List<ISolution<T>> solutionList = new ArrayList<ISolution<T>>(numberOfSolutionsToSelect);

		int currentNumberOfSolutions = 0;

		while(currentNumberOfSolutions < numberOfSolutionsToSelect){
			tournment(solutionList,solutionSet,isMaximization,randomNumberGenerator);
			currentNumberOfSolutions += k;
		}


		return solutionList;
	}


	/**
	 * Tournment.
	 * 
	 * @param solutionList the solution list
	 * @param solutionSet the solution set
	 * @param isMaximization the is maximization
	 * @param randomNumberGenerator 
	 */
	protected void tournment(List<ISolution<T>> solutionList, ISolutionSet<T> solutionSet,boolean isMaximization, IRandomNumberGenerator randomNumberGenerator) {
		List<ISolution<T>> randomPooledSolutionList = selectRandomSolutionList(solutionSet,randomNumberGenerator);
		orderSolutionList(solutionSet,randomPooledSolutionList);
		
		ISolution<T> solution = null;
		int solutionIndex = 0;
		
		for(int i = 0; i < k;i++){
			if(isMaximization)
				solutionIndex = randomPooledSolutionList.size()-1;
			else
				solutionIndex = 0;
			
			solution = randomPooledSolutionList.remove(solutionIndex);
			
			solutionList.add(solution);
		}
	}

	protected void orderSolutionList(ISolutionSet<T> solutionSet, List<ISolution<T>> randomPooledSolutionList) {
		Comparator<? super ISolution<T>> comparator = solutionSet.getComparator();
		Collections.sort(randomPooledSolutionList, comparator);
	}


	/**
	 * Select random ordered set.
	 * 
	 * @param solutionSet the solution set
	 * @param randomNumberGenerator 
	 * 
	 * @return the tree set< i solution>
	 */
	protected List<ISolution<T>> selectRandomSolutionList(ISolutionSet<T> solutionSet, IRandomNumberGenerator randomNumberGenerator) {
		List<ISolution<T>>  solutionList = new ArrayList<ISolution<T>>();
		int numberOfSolutions = solutionSet.getNumberOfSolutions();
	
		while(solutionList.size() < numberOfSolutionsPerTournment){
			int randomIndex = (int) (randomNumberGenerator.nextDouble()*numberOfSolutions);
			ISolution<T> solution = solutionSet.getSolution(randomIndex);
			solutionList.add(solution);
		}
		
		return solutionList;
	}


	@Override
	public TournamentSelection<T> deepCopy() {
		try {
			return new TournamentSelection<T>(k, numberOfSolutionsPerTournment);
		} catch (InvalidSelectionParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

}