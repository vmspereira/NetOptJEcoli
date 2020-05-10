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
package jecoli.algorithm.multiobjective.speameme;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.algorithm.AbstractAlgorithm;
import jecoli.algorithm.components.algorithm.AlgorithmState;
import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmResult;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.operator.ISelectionOperator;
import jecoli.algorithm.components.operator.InvalidNumberOfInputSolutionsException;
import jecoli.algorithm.components.operator.InvalidNumberOfOutputSolutionsException;
import jecoli.algorithm.components.operator.container.IOperatorContainer;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionContainer;
import jecoli.algorithm.components.solution.ISolutionFactory;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.solution.SolutionContainer;
import jecoli.algorithm.components.solution.SolutionSet;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import jecoli.algorithm.multiobjective.MOUtils;
import jecoli.algorithm.singleobjective.evolutionary.RecombinationParameters;
import jecoli.algorithm.singleobjective.simulatedannealing.IAnnealingSchedule;


// TODO: Auto-generated Javadoc
/**
 * The Class SPEAMEME.
 */
public class SPEAMEME<T extends IRepresentation,S extends ISolutionFactory<T>> extends AbstractAlgorithm<T,SPEAMEMEConfiguration<T,S>>{


	private static final long serialVersionUID = -8491514562204344881L;

	private boolean debug = false;

	/**
	 * Instantiates a new SPEA2 algorithm.
	 * 
	 * @param configuration the configuration
	 * 
	 * @throws InvalidConfigurationException the invalid configuration exception
	 * @throws InvalidEvaluationFunctionInputDataException 
	 */
	public SPEAMEME(SPEAMEMEConfiguration<T,S> configuration) throws Exception {
		super(configuration);
	}

	public ISolutionSet<T> initialize(SPEAMEMEAlgorithmState<T> algorithmState) throws Exception{

		algorithmState.initializeState();

		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		ISolutionSet<T> solutionSet = generateInitialPopulation();

		evaluationFunction.evaluate(solutionSet);
		algorithmState.setSolutionSet(solutionSet);
		algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(solutionSet.getNumberOfSolutions());

		ISolutionSet<T> archive = new SolutionSet<T>();
		algorithmState.setArchive(archive);     
		configuration.setInitialPopulation(solutionSet);
		return solutionSet;
	}

	/**
	 * Generate initial population.
	 * 
	 * @return the i solution set
	 */
	protected ISolutionSet<T> generateInitialPopulation() {
		int populationSize = configuration.getPopulationSize();
		ISolutionFactory<T> factory = configuration.getSolutionFactory();
		IRandomNumberGenerator randomNumberGenerator = configuration.getRandomNumberGenerator();
		ISolutionSet<T> population = factory.generateSolutionSet(populationSize,randomNumberGenerator);
		return population;
	}

	@Override
	public IAlgorithmResult<T> run() throws Exception {
		ITerminationCriteria terminationCriteria = configuration.getTerminationCriteria();
		SPEAMEMEAlgorithmState<T> algorithmState = new SPEAMEMEAlgorithmState<T>(this);
		setAlgorithmState(algorithmState);

		algorithmState.initializeState();
		ISolutionSet<T> solutionSet = initialize(algorithmState);        
		
		
		while (!terminationCriteria.verifyAlgorithmTermination(this,algorithmState)) {
			ISolutionSet<T> newSolutionSet = iteration(algorithmState,solutionSet);
			solutionSet = newSolutionSet;
		}

		/** set the final solution set to equal the archive */
		updateState(algorithmState,algorithmState.getArchive());
		updateLastState(algorithmState,algorithmState.getArchive());


		System.out.println("FINAL SET:");        
		for(ISolution<T> sol : algorithmState.getSolutionSet().getListOfSolutions()){
			System.out.print(sol.getFitnessValue(0));
			for(int i=1; i<sol.getNumberOfObjectives();i++)
				System.out.print(","+sol.getFitnessValue(i));
			System.out.print("\n");
		}

		ISolutionSet<T> finalSet = algorithmState.getArchive();

		ISolutionSet<T> annealingSet = anneal(algorithmState,finalSet);

		System.out.println("ANNEALING SET:");        
		for(ISolution<T> sol : annealingSet.getListOfSolutions()){
			System.out.print(sol.getFitnessValue(0));
			for(int i=1; i<sol.getNumberOfObjectives();i++)
				System.out.print(","+sol.getFitnessValue(i));
			System.out.print("\n");
		}

		IAlgorithmResult<T> algorithmResult = algorithmState.getAlgorithmResult();


		algorithmState = null;

		return algorithmResult;
	}

	private void updateLastState(SPEAMEMEAlgorithmState<T> algorithmState,ISolutionSet<T> archive) {
		ISolutionContainer<T> container = new SolutionContainer<T>(configuration.getMaximumArchiveSize());
		container.addSpecificSolutions(
				algorithmState.getSolutionSet(),
				algorithmState.getCurrentIteration(),
				configuration.getEvaluationFunction().isMaximization());
		algorithmState.getAlgorithmResult().setSolutionContainer(container);

	}

	/**
	 * Update archive state.
	 * 
	 * @param archive the archive
	 */
	public void updateArchiveState(SPEAMEMEAlgorithmState<T> algorithmState,ISolutionSet<T> archive){
		algorithmState.updateArchiveState(archive);
	}

	@Override
	public ISolutionSet<T> iteration(AlgorithmState<T> algorithmState, ISolutionSet<T> solutionSet) throws Exception{

//		updateState(algorithmState,solutionSet);

		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();
		ISolutionSet<T> newGeneration = new SolutionSet<T>();

		ISolutionSet<T> union = ((SPEAMEMEAlgorithmState<T>)algorithmState).getArchive().union(solutionSet);
		MOUtils.assignSelectionValue(union, isMaximization);
		updateState(algorithmState,solutionSet);
		SolutionSet<T> newArchive = this.environmentalSelection(union, configuration.getMaximumArchiveSize(), isMaximization);
		updateArchiveState((SPEAMEMEAlgorithmState<T>)algorithmState,newArchive);

		RecombinationParameters recombinationParameters = configuration.getRecombinationParameters();	
		int offSpringSize = recombinationParameters.getOffspringSize();

		serialRecombination(((SPEAMEMEAlgorithmState<T>)algorithmState).getArchive(),newGeneration,isMaximization,offSpringSize);

		evaluationFunction.evaluate(newGeneration);

		algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(offSpringSize);		

		return newGeneration;
	}

	/**
	 * Serial recombination.
	 * 
	 * @param solutionSet the solution set
	 * @param newGeneration the new generation
	 * @param isMaximization the is maximization
	 * @param offSpringSize the off spring size
	 * 
	 * @throws Exception the exception
	 */
	protected void serialRecombination(ISolutionSet<T> solutionSet,ISolutionSet<T> newGeneration, boolean isMaximization, int offSpringSize) throws Exception {

		int currentNumberOfIndividuals = 0;
		List<ISolution<T>> offspring = null;

		IOperatorContainer<IReproductionOperator<T,S>> crossoverOperators = configuration.getCrossoverOperatorsContainer();
		IOperatorContainer<IReproductionOperator<T,S>> mutationOperators = configuration.getMutationOperatorsContainer();

		ISelectionOperator<T> selectionOperator = configuration.getSelectionOperator();

		/** serial application of crossover and mutation to breed offspring */
		while(currentNumberOfIndividuals < offSpringSize){
			IReproductionOperator<T,S> cxOperator = crossoverOperators.selectOperator();

			int numberOfInputIndividuals = cxOperator.getNumberOfInputSolutions();

			List<ISolution<T>> parents = selectionOperator.selectSolutions(numberOfInputIndividuals, solutionSet, isMaximization, configuration.getRandomNumberGenerator());

			offspring = cxOperator.apply(parents, configuration.getSolutionFactory(),configuration.getRandomNumberGenerator());

			for(ISolution<T> sol : offspring) {

				List<ISolution<T>> auxPopulation = new ArrayList<ISolution<T>>();
				auxPopulation.add(sol);

				IReproductionOperator<T,S> mutOperator = mutationOperators.selectOperator();
				int numberOfOutputIndividualsFromMutation = mutOperator.getNumberOfOutputSolutions();

				auxPopulation = mutOperator.apply(auxPopulation, configuration.getSolutionFactory(),configuration.getRandomNumberGenerator());				
				currentNumberOfIndividuals += numberOfOutputIndividualsFromMutation;				
				newGeneration.add(auxPopulation);

			}
		}

	}


	/**
	 * Environmental selection.
	 * 
	 * @param solutionSet the solution set
	 * @param size the size
	 * @param isMaximization the is maximization
	 * 
	 * @return the solution set
	 * 
	 * @throws Exception the exception
	 */
	protected SolutionSet<T> environmentalSelection(ISolutionSet<T> solutionSet, int size,boolean isMaximization) throws Exception{
		ISelectionOperator<T> selectionOperator = configuration.getEnvironmentalSelectionOperator();
		List<ISolution<T>> selected = selectionOperator.selectSolutions(size, solutionSet, isMaximization, null);

		return new SolutionSet<T>(selected);
	}

	public SolutionSet<T> anneal(AlgorithmState<T> algorithmState, ISolutionSet<T> solutionSet) throws InvalidNumberOfInputSolutionsException, InvalidNumberOfOutputSolutionsException{
		IAnnealingSchedule annealingSchedule = configuration.getAnnealingSchedule();
		int numberOfAcceptedMoves = 0;
		int numberOfRejectedMoves = 0;
		IEvaluationFunction<?> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();

		int numSols = solutionSet.getNumberOfSolutions();
		for(int currSolIndex=0; currSolIndex< numSols ; currSolIndex++){
			//		int currSolIndex = random.nextInt(solutionSet.getNumberOfSolutions());
			
			System.out.println(">>>>>>>>>>>>>>>>> CURRENT SOL: "+currSolIndex +" / " + numSols);
			ISolution<T> currentSolution = solutionSet.getSolution(currSolIndex);


			while(!annealingSchedule.isEquilibriumState(numberOfAcceptedMoves,numberOfRejectedMoves)){

				if (debug) System.out.println("starting trial cycle ...");

				ISolution<T> trialSolution = createTrialSolution(algorithmState,currentSolution,solutionSet,currSolIndex);
				double currentSolutionFitnessValue = currentSolution.getScalarFitnessValue();
				double trialSolutionFitnessValue = trialSolution.getScalarFitnessValue();

				if (acceptSolution(currentSolutionFitnessValue,	trialSolutionFitnessValue)){

					if (debug) System.out.println("accepting solution ...");

					currentSolution = trialSolution;
					numberOfAcceptedMoves++;

					if (debug) System.out.println("number of accepted: " + numberOfAcceptedMoves);

				}else {
					//numberOfRejectedMoves++; // doesn't make sense
					double randomNumber = Math.random();
					double acceptSolutionProbability = 0; 

					if(isMaximization) 
						acceptSolutionProbability = annealingSchedule.caculateAcceptSolutionProbability(currentSolutionFitnessValue,trialSolutionFitnessValue);
					else
						acceptSolutionProbability = annealingSchedule.caculateAcceptSolutionProbability(trialSolutionFitnessValue,currentSolutionFitnessValue);

					if (randomNumber < acceptSolutionProbability) {

						if (debug) System.out.println("accepting worse solution ...");

						currentSolution = trialSolution;
						numberOfAcceptedMoves++;
						if (debug) System.out.println("number of accepted: " + numberOfAcceptedMoves);
					} else
					{
						if (debug) System.out.println("rejecting solution ...");
						numberOfRejectedMoves++;
						if (debug) System.out.println("number of rejected: " + numberOfRejectedMoves);
					}
				}
				//			annealingSchedule.getFitnessFunctionData(currentSolution.getScalarFitnessValue()); // NAO FAZ NADA !!!
			}

			if (debug) System.out.println("ended trial cycle ...");

			solutionSet.add(currentSolution);

			if (debug) System.out.println("added current solution; calculating new temp");

			annealingSchedule.calculateNewTemperature();

			if (debug) System.out.println("ending iteration ...");

		}
		return (SolutionSet<T>) solutionSet;
	}

	public ISolution<T> createTrialSolution(AlgorithmState<T> algorithmState, ISolution<T> currentSolution, ISolutionSet<T> solutionSet, int currSolIndex) throws InvalidNumberOfInputSolutionsException, InvalidNumberOfOutputSolutionsException
	{

		if (debug) System.out.println("creating trial solution ...");

		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();

		if (debug) System.out.println("selecting mutation operator ...");

		IReproductionOperator<T,S> reproductionOperator = configuration.selectMutationOperator();

		if (debug) System.out.println("done selecting mutation operator ...");

		List<ISolution<T>> selectedSolutionList = new ArrayList<ISolution<T>>(0);
		selectedSolutionList.add(currentSolution);

		if (debug) System.out.println("added current solution; applying rep op ...");

		List<ISolution<T>> trialSolutionList = reproductionOperator.apply(selectedSolutionList,configuration.getSolutionFactory(),configuration.getRandomNumberGenerator());

		if (debug) System.out.println("done applying rep op ...");

		ISolution<T> trialSolution = trialSolutionList.get(0);


		if (debug) System.out.println("evaluating ...");

		// context specific evaluation
		evaluationFunction.evaluateSingleSolution(trialSolution);


		// backup solution
		ISolution<T> backSol = solutionSet.getSolution(currSolIndex);

		// replace and recompute fitnesses
		solutionSet.setSolution(currSolIndex, trialSolution);
		//		MOUtils.assignFitness(solutionSet, evaluationFunction.isMaximization());

		double fit = MOUtils.computeZitzlerSelection4SingleSolution(trialSolution, solutionSet, evaluationFunction.isMaximization());
		trialSolution.setFitnessValue(fit);

		// place old solution again and recompute fitnesses
		solutionSet.setSolution(currSolIndex, backSol);
		//		MOUtils.assignFitness(solutionSet, evaluationFunction.isMaximization());


		if (debug) System.out.println("done evaluating ...");

		algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations();

		if (debug) System.out.println("end creating trial solution ...");

		return trialSolution;
	}

	protected boolean acceptSolution(double currentFitnessValue,double trialSolutionFitnessValue) {
		IEvaluationFunction<?> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();
		boolean isCurrentFitnessBigger =  (currentFitnessValue > trialSolutionFitnessValue);

		if(isMaximization)
			return !isCurrentFitnessBigger;

		return isCurrentFitnessBigger;
	}

	@Override
	public ISolutionSet<T> initialize() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAlgorithm<T> deepCopy() throws Exception {
		return null;
	}


}