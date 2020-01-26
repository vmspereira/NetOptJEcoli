package jecoli.jUnitsTests;

import java.util.ArrayList;

import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.operator.InvalidSelectionParameterException;
import jecoli.algorithm.components.operator.container.ReproductionOperatorContainer;
import jecoli.algorithm.components.operator.reproduction.linear.BitFlipMutation;
import jecoli.algorithm.components.operator.reproduction.linear.LinearGenomeRandomMutation;
import jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
import jecoli.algorithm.components.representation.binary.BinaryRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.solution.SolutionSet;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.components.terminationcriteria.InvalidNumberOfIterationsException;
import jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import jecoli.algorithm.singleobjective.simulatedannealing.AnnealingSchedule;
import jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealing;
import jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealingConfiguration;



public class SimulatedAnnealingTest extends AbstractAlgorithmTest<ILinearRepresentation<Boolean>, BinaryRepresentationFactory>{

	public SimulatedAnnealingTest()
	{
		this.testName = "SimulatedAnnealing";
		this.randomNumberGenerator = new DefaultRandomNumberGenerator();
	}
	
	protected void setCountingOnesConfigure()
	{
		try {
			
			SimulatedAnnealingConfiguration<ILinearRepresentation<Boolean>, BinaryRepresentationFactory> algorithmConfiguration = new SimulatedAnnealingConfiguration<ILinearRepresentation<Boolean>, BinaryRepresentationFactory>();
			
			IEvaluationFunction<ILinearRepresentation<Boolean>> evaluationFunction = new AlgorithmCountingOnesPopulation(this.isMaximization);
			
			algorithmConfiguration.setRandomNumberGenerator(this.randomNumberGenerator);
			algorithmConfiguration.setProblemBaseDirectory("nullDirectory");
			algorithmConfiguration.setAlgorithmStateFile("nullFile");
			algorithmConfiguration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
			algorithmConfiguration.setPopulationSize(populationSize);
			algorithmConfiguration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<ILinearRepresentation<Boolean>>>());
			algorithmConfiguration.setStatisticsConfiguration(new StatisticsConfiguration());
			
			algorithmConfiguration.setEvaluationFunction(evaluationFunction);
			BinaryRepresentationFactory solutionFactory = new BinaryRepresentationFactory(this.populationSize);
			algorithmConfiguration.setInitialPopulation(new SolutionSet<ILinearRepresentation<Boolean>>(this.initialPopulation));
			
			algorithmConfiguration.setSolutionFactory(solutionFactory);
			
			
			ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(this.numberOfGenerations);
			algorithmConfiguration.setTerminationCriteria(terminationCriteria);
			
			ReproductionOperatorContainer operatorContainer = new ReproductionOperatorContainer();
			operatorContainer.addOperator(0.5,new BitFlipMutation(2));
			operatorContainer.addOperator(0.5,new LinearGenomeRandomMutation<Boolean>(3));
			algorithmConfiguration.setMutationOperatorContainer(operatorContainer);
			
			AnnealingSchedule schedule = new AnnealingSchedule(2, 2, 14);
			algorithmConfiguration.setAnnealingSchedule(schedule);
			
			this.algorithm = new SimulatedAnnealing<ILinearRepresentation<Boolean>,BinaryRepresentationFactory>(algorithmConfiguration);
		}
		catch (InvalidNumberOfIterationsException e) {e.printStackTrace();} 
		catch (InvalidSelectionParameterException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
		catch (Exception e) {e.printStackTrace();} 
	}
	
	@Override
	protected void specificAlgorithmTests() {
		
	}

	@Override
	public void setTestValues() {
		setCountingOnesConfigure();
		
	}

}
