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
package jecoli.test.motifs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmResult;
import jecoli.algorithm.components.algorithm.IAlgorithmStatistics;
import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.operator.container.ReproductionOperatorContainer;
import jecoli.algorithm.components.operator.reproduction.linear.IntegerAddMutation;
import jecoli.algorithm.components.operator.reproduction.linear.LinearGenomeRandomMutation;
import jecoli.algorithm.components.operator.reproduction.linear.UniformCrossover;
import jecoli.algorithm.components.operator.selection.RankingSelection;
import jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.integer.IntegerRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import jecoli.algorithm.singleobjective.evolutionary.EvolutionaryAlgorithm;
import jecoli.algorithm.singleobjective.evolutionary.EvolutionaryConfiguration;
import jecoli.algorithm.singleobjective.evolutionary.RecombinationParameters;
import jecoli.algorithm.singleobjective.simulatedannealing.AnnealingSchedule;
import jecoli.algorithm.singleobjective.simulatedannealing.IAnnealingSchedule;
import jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealing;
import jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealingConfiguration;

// TODO: Auto-generated Javadoc
/**
 * The Class EAMotifs.
 */
public class EAMotifs {

	
	/** The sequences. */
	SeqMotifs sequences; 
	
	/** The algorithm. */
	IAlgorithm<ILinearRepresentation<Integer>> algorithm;
	
	/** The results. */
	IAlgorithmResult<ILinearRepresentation<Integer>> results;
	
	/** The statistics. */
	IAlgorithmStatistics<ILinearRepresentation<Integer>> statistics;
	
	
	
	/**
	 * Instantiates a new eA motifs.
	 * 
	 * @param sequences the sequences
	 */
	public EAMotifs(SeqMotifs sequences)
	{
		this.sequences = sequences;
	}
	
	
	/**
	 * Configure evolutionary algorithm.
	 * 
	 * @param populationSize the population size
	 * @param numberGenerations the number generations
	 * 
	 * @throws Exception the exception
	 * @throws InvalidConfigurationException the invalid configuration exception
	 */
	public void configureEvolutionaryAlgorithm (int populationSize, int numberGenerations) 
	throws Exception, InvalidConfigurationException
	{
		// size of a solution
		int isize = sequences.getNumSequences();
		// max value of a gene
		int maxgene = sequences.getSeqSize()-sequences.getMotifSize()+1;

		//IRandomNumberGenerator randomGenerator = new RandomAtmosfericGenerator();
		IRandomNumberGenerator randomGenerator = new DefaultRandomNumberGenerator();

		EvolutionaryConfiguration<ILinearRepresentation<Integer>,IntegerRepresentationFactory> configuration = new EvolutionaryConfiguration<ILinearRepresentation<Integer>,IntegerRepresentationFactory>();
		
		IEvaluationFunction<ILinearRepresentation<Integer>> evaluationFunction = new MotifsEvaluationFunction(sequences);
		
		configuration.setEvaluationFunction(evaluationFunction);
		
		IntegerRepresentationFactory solutionFactory = new IntegerRepresentationFactory(isize,maxgene);
		configuration.setSolutionFactory(solutionFactory);
		
		configuration.setPopulationSize(populationSize);

		ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(numberGenerations);
		configuration.setTerminationCriteria(terminationCriteria);
		
		RecombinationParameters recombinationParameters = new RecombinationParameters(populationSize);
		configuration.setRecombinationParameters(recombinationParameters);
		
		configuration.setRandomNumberGenerator(randomGenerator);
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<ILinearRepresentation<Integer>>>());
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		
		
		configuration.setSelectionOperators(new RankingSelection<ILinearRepresentation<Integer>>());

		ReproductionOperatorContainer operatorContainer = new ReproductionOperatorContainer();
		operatorContainer.addOperator(0.5,	new UniformCrossover<Integer>());
		operatorContainer.addOperator(0.25,	new LinearGenomeRandomMutation<Integer>(0.003));	
		operatorContainer.addOperator(0.25,	new IntegerAddMutation(3) );
		configuration.setReproductionOperatorContainer(operatorContainer);
		
		algorithm = new EvolutionaryAlgorithm<ILinearRepresentation<Integer>,IntegerRepresentationFactory>(configuration);
		IAlgorithmResult<ILinearRepresentation<Integer>> result = algorithm.run();

	}
	
	/**
	 * Configure simulated annealing.
	 * 
	 * @param numberGenerations the number generations
	 * 
	 * @throws Exception the exception
	 * @throws InvalidConfigurationException the invalid configuration exception
	 */
	public void configureSimulatedAnnealing(int numberGenerations) 	throws Exception, InvalidConfigurationException
	{
		// size of a solution
		int isize = sequences.getNumSequences();
		// max value of a gene
		int maxgene = sequences.getSeqSize()-sequences.getMotifSize()+1;
		
		IRandomNumberGenerator randomGenerator = new DefaultRandomNumberGenerator();
		
		SimulatedAnnealingConfiguration<ILinearRepresentation<Integer>,IntegerRepresentationFactory> configuration = new SimulatedAnnealingConfiguration<ILinearRepresentation<Integer>,IntegerRepresentationFactory>();
		configuration.setEvaluationFunction( new MotifsEvaluationFunction(sequences) );
		IntegerRepresentationFactory solutionFactory = new IntegerRepresentationFactory(isize,maxgene);
		configuration.setSolutionFactory(solutionFactory); 
		 
		IAnnealingSchedule annealingSchedule = new  AnnealingSchedule(100,0.99,5.0);
		configuration.setAnnealingSchedule(annealingSchedule);
		
		configuration.setRandomNumberGenerator(randomGenerator);
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<ILinearRepresentation<Integer>>>());
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		

		ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(numberGenerations);
		configuration.setTerminationCriteria(terminationCriteria);
		
		ReproductionOperatorContainer operatorContainer = new ReproductionOperatorContainer();
		operatorContainer.addOperator(0.5,	new LinearGenomeRandomMutation<Integer>(0.003));	
		operatorContainer.addOperator(0.5,	new IntegerAddMutation(3) );
		configuration.setMutationOperatorContainer(operatorContainer);
		
		algorithm = new SimulatedAnnealing<ILinearRepresentation<Integer>,IntegerRepresentationFactory>(configuration);
	}
	
	/**
	 * Run.
	 * 
	 * @throws Exception the exception
	 */
	public double run() throws Exception
	{
		results =  algorithm.run();
		statistics = results.getAlgorithmStatistics();
		
		return results.getSolutionContainer().getBestSolutionCellContainer(true).getSolution().getScalarFitnessValue();
	}
	
	
	/**
	 * Gets the best solution.
	 * 
	 * @return the best solution
	 */
	public int[] getBestSolution()
	{
		ISolution<ILinearRepresentation<Integer>> bestSolution = results.getSolutionContainer().getBestSolutionCellContainer(true).getSolution();
		
		ILinearRepresentation<Integer> genome = bestSolution.getRepresentation();
		
		int[] res = new int[genome.getNumberOfElements()];
		
		for(int i=0; i < genome.getNumberOfElements(); i++)
			res[i] = genome.getElementAt(i);
		return res;
	}
	
    public static void printa(double [] arr)
	{
	 for(int i=0;i<arr.length;i++)
	    System.out.print(arr[i]+" ");
	}
    
    public static void printa(int [] arr)
	{
	 for(int i=0;i<arr.length;i++)
	    System.out.print(arr[i]+" ");
	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		try {
			int numberOfRuns = 30;
			for(int i=0;i<numberOfRuns;i++)
			{
				FileWriter fw = new FileWriter("/Users/emanuel/Documents/optfluxWorkspace/JecoFinalVersion/results/MotifsSA2.txt",true);
//				FileWriter fw = new FileWriter("/Users/emanuel/Documents/optfluxWorkspace/JecoFinalVersion/results/MotifsEA2.txt",true);
				BufferedWriter w = new BufferedWriter(fw);
				
				String filename = "./src/jecoli/test/motifs/exemploMotifs.txt";
				String symbols = "actg";
				int sizeMotifs = 8;
				
				SeqMotifs sm = new SeqMotifs(filename, symbols, sizeMotifs);
	
				int populationSize = 200;
				int numberGenerations = 2000;
				
				EAMotifs ea = new EAMotifs(sm);
				
				ea.configureEvolutionaryAlgorithm(populationSize, numberGenerations);
		
//				ea.configureSimulatedAnnealing(numberGenerations);
				
				double result = ea.run();
				
				fw.append(result+"\n");
				
				
				int[] bestSolution = ea.getBestSolution();
				System.out.println("Best solution:");
				printa(bestSolution);
				System.out.println("Score:" + sm.score(bestSolution));
				System.out.println("Consensus" + sm.consensus(bestSolution));
				
				fw.close();
				w.close();
			}			

		}
		catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
