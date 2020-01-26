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
package jecoli.algorithm.components.operator.selection.preprocessing;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class ScaledFitnessDataComparator.
 */
public class ScaledFitnessDataComparator implements Comparator<ScaledFitnessData>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ScaledFitnessData o1, ScaledFitnessData o2) {
		
		if(o1.getScaledFitnessValue() >= o2.getScaledFitnessValue())
			return 1;
		
		if(o1.getScaledFitnessValue() < o2.getScaledFitnessValue())
			return -1;
		
		return 0;
		
	}

}
