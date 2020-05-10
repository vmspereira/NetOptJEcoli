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
package jecoli.algorithm.components.solution.comparator;

import java.io.Serializable;
import java.util.Comparator;

import jecoli.algorithm.components.solution.ISolution;



// TODO: Auto-generated Javadoc
/**
 * The Class SolutionScalarFitnessComparator.
 */
public class SolutionScalarFitnessComparator<T extends ISolution<?>> implements Comparator<T>, Serializable {
	
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T firstSolution, T secondSolution){

		if(firstSolution == secondSolution)
			return 0;

		if(firstSolution.getScalarFitnessValue() > secondSolution.getScalarFitnessValue())
			return 1;

		if(firstSolution.getScalarFitnessValue() < secondSolution.getScalarFitnessValue())
			return -1;


		return 0;
	}

}