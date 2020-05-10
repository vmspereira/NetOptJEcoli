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
package jecoli.algorithm.components.solution;

import jecoli.algorithm.components.IDeepCopy;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;




// TODO: Auto-generated Javadoc
/**
 * A factory for creating ISolution objects.
 */
public interface ISolutionFactory<T extends IRepresentation> extends IDeepCopy{
    
    /**
     * Copy solution.
     * 
     * @param solutionToCopy the solution to copy
     * 
     * @return the i solution
     */
    ISolution<T> copySolution(ISolution<T> solutionToCopy);
    
    /**
     * Generate solution set.
     * 
     * @param numberOfSolutions the number of solutions
     * 
     * @return the i solution set
     */
    ISolutionSet<T> generateSolutionSet(int numberOfSolutions,IRandomNumberGenerator randomGenerator);
	
	ISolution<T> generateSolution(IRandomNumberGenerator randomGenerator);

	ISolutionFactory<T> deepCopy();

	/**
	 * Gets the number of objectives.
	 * 
	 * @return the number of objectives
	 */
	int getNumberOfObjectives();

}