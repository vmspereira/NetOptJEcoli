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
package jecoli.algorithm.singleobjective.randomsearch;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmResult;
import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.AbstractConfiguration;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.representation.binary.BinaryRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.ISolutionFactory;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.components.terminationcriteria.InvalidNumberOfIterationsException;
import jecoli.algorithm.components.terminationcriteria.InvalidTerminationCriteriaParameter;
import jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import jecoli.test.countones.CountOnesEvaluationFunction;

// TODO: Auto-generated Javadoc
/**
 * The Class RandomSearchConfiguration.
 */
public class RandomSearchConfiguration<T extends IRepresentation> extends AbstractConfiguration<T> {
	
	/** The solution factory. */
	protected ISolutionFactory<T> solutionFactory;

	/**
	 * Gets the solution factory.
	 * 
	 * @return the solution factory
	 */
	public ISolutionFactory<T> getSolutionFactory() {
		return solutionFactory;
	}

	/**
	 * Sets the solution factory.
	 * 
	 * @param solutionFactory the new solution factory
	 */
	public void setSolutionFactory(ISolutionFactory<T> solutionFactory){
		this.solutionFactory = solutionFactory;
	}

	public void verifyConfiguration() throws InvalidConfigurationException{
		
		if(terminationCriteria == null)
			throw new InvalidConfigurationException("Termination Criteria is null") ;

		if(evaluationFunction == null)
			throw new InvalidConfigurationException("Evaluation Function is null") ;

		if(solutionFactory == null)
			throw new InvalidConfigurationException("Solution Factory is null") ;

	}

	@Override
	public void setStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration) {
		this.statisticsConfiguration = statisticsConfiguration;
	}

	public RandomSearchConfiguration<T> deepCopy() throws Exception {
		RandomSearchConfiguration<T> configurationCopy = new RandomSearchConfiguration<T>();
		configurationCopy.setTerminationCriteria(terminationCriteria.deepCopy());
		configurationCopy.setEvaluationFunction(evaluationFunction.deepCopy());
		configurationCopy.setSolutionFactory(solutionFactory.deepCopy());
		configurationCopy.setUID(new String(UID));
		
		configurationCopy.setProblemBaseDirectory(new String(problemBaseDirectory));
		configurationCopy.setAlgorithmStateFile(new String(saveAlgorithmStateFile));
		configurationCopy.setSaveAlgorithmStateIterationInterval(new Integer(saveAlgorithmStateIterationInterval));
		configurationCopy.setSaveAlgorithmStateDirectoryPath(new String(saveAlgorithmStateDirectoryPath));
		
		List<IAlgorithmResultWriter<T>> algorithmResultWriterListCopy = new ArrayList<IAlgorithmResultWriter<T>>();
		for(IAlgorithmResultWriter<T> algorithmResultWriter:algorithmResultWriterList)
			algorithmResultWriterListCopy.add(algorithmResultWriter.deepCopy());
		
		configurationCopy.setAlgorithmResultWriterList(algorithmResultWriterListCopy);
		return configurationCopy;
	}



}