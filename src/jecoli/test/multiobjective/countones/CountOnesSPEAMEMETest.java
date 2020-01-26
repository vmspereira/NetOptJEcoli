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
package jecoli.test.multiobjective.countones;

import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmResult;
import jecoli.algorithm.components.algorithm.IAlgorithmStatistics;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.operator.container.ReproductionOperatorContainer;
import jecoli.algorithm.components.operator.reproduction.linear.BitFlipMutation;
import jecoli.algorithm.components.operator.reproduction.linear.OnePointCrossover;
import jecoli.algorithm.components.operator.selection.EnvironmentalSelection;
import jecoli.algorithm.components.operator.selection.TournamentSelection;
import jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.binary.BinaryRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.components.terminationcriteria.InvalidNumberOfIterationsException;
import jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import jecoli.algorithm.multiobjective.archive.plotting.Plot2DGUI;
import jecoli.algorithm.multiobjective.speameme.SPEAMEME;
import jecoli.algorithm.multiobjective.speameme.SPEAMEMEConfiguration;
import jecoli.algorithm.singleobjective.evolutionary.RecombinationParameters;
import jecoli.algorithm.singleobjective.simulatedannealing.AnnealingSchedule;

// TODO: Auto-generated Javadoc
/**
 * The Class CountOnesSPEA2Test.
 */
public class CountOnesSPEAMEMETest {

	/**
	 * The main method.
	 * 
	 * @param args the args
	 */
	public static void main(String[] args) {
		try {
			SPEAMEMEConfiguration<ILinearRepresentation<Boolean>,BinaryRepresentationFactory> configuration = new SPEAMEMEConfiguration<ILinearRepresentation<Boolean>,BinaryRepresentationFactory>();
			configuration.setStatisticsConfiguration(new StatisticsConfiguration());
			
			IRandomNumberGenerator randomNumberGenerator = new DefaultRandomNumberGenerator();
			configuration.setRandomNumberGenerator(randomNumberGenerator);
			
			IEvaluationFunction<ILinearRepresentation<Boolean>> evaluationFunction = new CountOnesMOEvaluationFunction<ILinearRepresentation<Boolean>>(true);
			configuration.setEvaluationFunction(evaluationFunction);
			
			int solutionSize = 100;
			BinaryRepresentationFactory solutionFactory = new BinaryRepresentationFactory(solutionSize,2);
			configuration.setSolutionFactory(solutionFactory);
			configuration.setNumberOfObjectives(2);
			
			AnnealingSchedule as = new AnnealingSchedule(0.1, 0.00001, 50, 10000);
			configuration.setAnnealingSchedule(as);
			
			int populationSize = 100;
			int maximumArchiveSize = 100;
			configuration.setPopulationSize(populationSize);
			configuration.setMaximumArchiveSize(maximumArchiveSize);

			int numberGenerations = 1000;
			ITerminationCriteria terminationCriteria;
			terminationCriteria = new IterationTerminationCriteria(numberGenerations);
			configuration.setTerminationCriteria(terminationCriteria);
			
			RecombinationParameters recombinationParameters = new RecombinationParameters(0,populationSize,0,true);
			configuration.setRecombinationParameters(recombinationParameters);
			
			configuration.setSelectionOperator(new TournamentSelection<ILinearRepresentation<Boolean>>(1,2));
			configuration.setEnvironmentalSelectionOperator(new EnvironmentalSelection<ILinearRepresentation<Boolean>>());
			
			ReproductionOperatorContainer reproductionOperatorContainer = new ReproductionOperatorContainer();
			reproductionOperatorContainer.addOperator(0.5,new OnePointCrossover<Boolean>());
			reproductionOperatorContainer.addOperator(0.5,new BitFlipMutation(1));
			configuration.setReproductionOperatorContainer(reproductionOperatorContainer);
			
			IAlgorithm<ILinearRepresentation<Boolean>> algorithm = new SPEAMEME<ILinearRepresentation<Boolean>,BinaryRepresentationFactory>(configuration);

			Plot2DGUI<ILinearRepresentation<Boolean>> plotter = new Plot2DGUI<ILinearRepresentation<Boolean>>(algorithm);
			plotter.setObserveArchive(true);
			plotter.run();
			
			IAlgorithmResult<ILinearRepresentation<Boolean>> result =  algorithm.run();
			IAlgorithmStatistics<ILinearRepresentation<Boolean>> stats = result.getAlgorithmStatistics();

			
			
		} catch (InvalidNumberOfIterationsException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
