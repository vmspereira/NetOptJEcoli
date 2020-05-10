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
package jecoli.algorithm.components.algorithm;

import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolutionContainer;

// TODO: Auto-generated Javadoc
/**
 * The Interface IAlgorithmResult.
 */
public interface IAlgorithmResult<T extends IRepresentation> {
    
    /**
     * Process algorithm state.
     * 
     * @param algorithmState the algorithm state
     */
	void processAlgorithmState(AlgorithmState<T> algorithmState);
    
    /**
     * Gets the algorithm statistics.
     * 
     * @return the algorithm statistics
     */
    IAlgorithmStatistics<T> getAlgorithmStatistics();
    
    /**
     * Gets the solution container.
     * 
     * @return the solution container
     */
    ISolutionContainer<T> getSolutionContainer();
    
    /**
     * TODO
     * @param container
     */
    void setSolutionContainer(ISolutionContainer<T> container);
}