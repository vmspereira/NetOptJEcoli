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
package jecoli.algorithm.components.statistics;

import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;

// TODO: Auto-generated Javadoc
/**
 * The Interface IAlgorithmIterationStatisticCell.
 */
public interface IAlgorithmIterationStatisticCell<T extends IRepresentation> {
	
	/**
	 * Gets the iteration number.
	 * 
	 * @return the iteration number
	 */
	int getIterationNumber();
	
	/**
	 * Gets the number of function evaluations.
	 * 
	 * @return the number of function evaluations
	 */
	int getNumberOfFunctionEvaluations();
	
	/**
	 * Gets the execution time.
	 * 
	 * @return the execution time
	 */
	long getExecutionTime();
	
	/**
	 * Gets the number of objectives.
	 * 
	 * @return the number of objectives
	 */
	int getNumberOfObjectives();
	
	/**
	 * Gets the objective statistic cell.
	 * 
	 * @param objectivePosition the objective position
	 * 
	 * @return the objective statistic cell
	 */
	ObjectiveStatisticCell getObjectiveStatisticCell(int objectivePosition);
	
	/**
	 * Gets the scalar fitness cell.
	 * 
	 * @return the scalar fitness cell
	 */
	ObjectiveStatisticCell getScalarFitnessCell();
	
	/**
	 * Gets the number of best solutions.
	 * 
	 * @return the number of best solutions
	 */
	int getNumberOfBestSolutions();
	
	/**
	 * Gets the best solution.
	 * 
	 * @param solutionPosition the solution position
	 * 
	 * @return the best solution
	 */
	ISolution<T> getBestSolution(int solutionPosition);
}