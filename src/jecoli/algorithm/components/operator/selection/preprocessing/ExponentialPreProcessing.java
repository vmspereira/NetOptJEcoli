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

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;


// TODO: Auto-generated Javadoc
/**
 * The Class ExponentialPreProcessing.
 */
public class ExponentialPreProcessing<T extends IRepresentation> implements IPreProcessing<T>{

	@Override
	public List<ScaledFitnessData<T>> preProcessFitness(boolean isMaximization,ISolutionSet<T> solutionSet) {
		int numberOfSolutions = solutionSet.getNumberOfSolutions();
		List<ScaledFitnessData<T>> scaledFitnessDataVector = new ArrayList<ScaledFitnessData<T>>();
		
		for(int i = 0; i <  numberOfSolutions; i++){
			ISolution<T> solution = solutionSet.getSolution(i);
			double fitnessValue = solution.getScalarFitnessValue();
			double scaledFitnessValue = Math.sqrt(fitnessValue+1);
			ScaledFitnessData<T> solutionFitnessData = new ScaledFitnessData<T>(solution,scaledFitnessValue);
			try {
				scaledFitnessDataVector.add(solutionFitnessData);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		return scaledFitnessDataVector;
	}

}
