package jecoli.test.tsp.eatsp;

import java.util.ArrayList;

import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmResult;
import jecoli.algorithm.components.algorithm.IAlgorithmStatistics;
import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.operator.container.ReproductionOperatorContainer;
import jecoli.algorithm.components.operator.reproduction.linear.LinearGenomeRandomMutation;
import jecoli.algorithm.components.operator.reproduction.linear.UniformCrossover;
import jecoli.algorithm.components.operator.selection.TournamentSelection;
import jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
import jecoli.algorithm.components.representation.integer.OrdinalRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.SolutionCellContainer;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import jecoli.algorithm.singleobjective.evolutionary.EvolutionaryAlgorithm;
import jecoli.algorithm.singleobjective.evolutionary.EvolutionaryConfiguration;
import jecoli.algorithm.singleobjective.evolutionary.RecombinationParameters;
import jecoli.test.tsp.libtsp.EucTsp;
import jecoli.test.tsp.libtsp.Tsp;

public class EATspOrdinal {
	/** The problem instance. */
	Tsp problemInstance;
	
	/** The algorithm. */
	IAlgorithm<ILinearRepresentation<Integer>> algorithm;
	
	/** The results. */
	IAlgorithmResult<ILinearRepresentation<Integer>> results;
	
	/** The statistics. */
	IAlgorithmStatistics<ILinearRepresentation<Integer>> statistics;
	
	/**
	 * Instantiates a new eA tsp.
	 * 
	 * @param problemInstance the problem instance
	 */
	public EATspOrdinal(Tsp problemInstance)
	{
		this.problemInstance = problemInstance;
	}
	

	/**
	 * Configure ea.
	 * 
	 * @param populationSize the population size
	 * @param numberGenerations the number generations
	 * 
	 * @throws Exception the exception
	 * @throws InvalidConfigurationException the invalid configuration exception
	 */
	public void configureEA (int populationSize, int numberGenerations) throws Exception, InvalidConfigurationException
	{
		EvolutionaryConfiguration<ILinearRepresentation<Integer>,OrdinalRepresentationFactory> configuration = 
				new EvolutionaryConfiguration<ILinearRepresentation<Integer>,OrdinalRepresentationFactory>();
				
		IEvaluationFunction<ILinearRepresentation<Integer>> evaluationFunction = 
				new TspOrdinalEvaluationFunction(problemInstance);
		configuration.setEvaluationFunction(evaluationFunction);
		
		int solutionSize = problemInstance.getDimension();
		OrdinalRepresentationFactory solutionFactory = new OrdinalRepresentationFactory(solutionSize);
		configuration.setSolutionFactory(solutionFactory);

		configuration.setPopulationSize(populationSize);
		ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(numberGenerations);
		configuration.setTerminationCriteria(terminationCriteria);
		
		configuration.setRandomNumberGenerator(new DefaultRandomNumberGenerator());
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<ILinearRepresentation<Integer>>>());
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		
		RecombinationParameters recombinationParameters = new RecombinationParameters(populationSize);
		
		configuration.setRecombinationParameters(recombinationParameters);
		
		configuration.setSelectionOperators(new TournamentSelection<ILinearRepresentation<Integer>>(1,2));
		
		ReproductionOperatorContainer operatorContainer = new ReproductionOperatorContainer();
		operatorContainer.addOperator(0.5,new UniformCrossover<Integer>());
		operatorContainer.addOperator(0.5,new LinearGenomeRandomMutation<Integer>());
		configuration.setReproductionOperatorContainer(operatorContainer);
		
		this.algorithm = 
				new EvolutionaryAlgorithm<ILinearRepresentation<Integer>,OrdinalRepresentationFactory>(configuration);

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
		
		return results.getSolutionContainer().getBestSolutionCellContainer(false).getSolution().getScalarFitnessValue();
	}
	
	
	/**
	 * Gets the best solution.
	 * 
	 * @return the best solution
	 */
	public int[] getBestSolution()
	{
		SolutionCellContainer<ILinearRepresentation<Integer>> container = 
				results.getSolutionContainer().getBestSolutionCellContainer(false);
		
		ILinearRepresentation<Integer> solution = container.getSolution().getRepresentation();
		
		int[] genome = new int[solution.getNumberOfElements()];
		for(int i=0; i < solution.getNumberOfElements(); i++)
			genome[i] = solution.getElementAt(i);
		
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (int i=0; i < genome.length; i++) order.add(i, i);
		
		int[] perm = new int[genome.length];
		for(int i= 0; i < genome.length; i++)
		{
			int index = genome[i];
			perm[i] = order.get(index);
			order.remove(index);
		}
		
		return perm;
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

	
			try 
			{
				Tsp problemInstance = new EucTsp("./src/jecoli/test/tsp/eil51.cit");
				
				EATsp ea = new EATsp(problemInstance);
				
				int populationSize = 100;
				int numberGenerations = 100;
				
				ea.configureEA(populationSize, numberGenerations);
				
				ea.run();
				
			} 
			catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		
}
