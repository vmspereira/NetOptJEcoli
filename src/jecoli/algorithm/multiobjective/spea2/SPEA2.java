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
package jecoli.algorithm.multiobjective.spea2;

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


// TODO: Auto-generated Javadoc
/**
 * The Class SPEA2.
 */
public class SPEA2<T extends IRepresentation,S extends ISolutionFactory<T>> extends AbstractAlgorithm<T,SPEA2Configuration<T,S>>{


	private static final long serialVersionUID = -8491514562204344881L;

	/**
	 * Instantiates a new SPEA2 algorithm.
	 * 
	 * @param configuration the configuration
	 * 
	 * @throws InvalidConfigurationException the invalid configuration exception
	 * @throws InvalidEvaluationFunctionInputDataException 
	 */
	public SPEA2(SPEA2Configuration<T,S> configuration) throws Exception {
		super(configuration);
	}

	/**
	 * Alterado por vitor
	 * @param algorithmState
	 * @return
	 * @throws Exception
	 */
	public ISolutionSet<T> initialize(SPEA2AlgorithmState<T> algorithmState) throws Exception{

		boolean verifyPopulationInitialization = configuration.verifyPopulationInitialization();
		ISolutionSet<T> solutionSet = null;

		if(verifyPopulationInitialization){
			solutionSet = generateInitialPopulation();
			configuration.setInitialPopulation(solutionSet);
		}else{
			solutionSet=configuration.getInitialPopulation();
		}
		
		ISolutionSet<T> archive = new SolutionSet<T>();
        configuration.getEvaluationFunction().evaluate(solutionSet);
        algorithmState.setSolutionSet(solutionSet);
        algorithmState.setArchive(archive);     
        algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(solutionSet.getNumberOfSolutions());

        
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
        SPEA2AlgorithmState<T> algorithmState = new SPEA2AlgorithmState<T>(this);

        algorithmState.initializeState();
        ISolutionSet<T> solutionSet = initialize(algorithmState);        
           

        while (!terminationCriteria.verifyAlgorithmTermination(this,algorithmState) && !cancel) {
            ISolutionSet<T> newSolutionSet = iteration(algorithmState,solutionSet);
            solutionSet = newSolutionSet;
        }
        
        /** set the final solution set to equal the archive */
        updateState(algorithmState,algorithmState.getArchive());
//        updateLastState(algorithmState,algorithmState.getArchive());
        

        System.out.println("FINAL SET:");        
        for(ISolution<T> sol : algorithmState.getSolutionSet().getListOfSolutions()){
        	System.out.print(sol.getFitnessValue(0));
        	for(int i=1; i<sol.getNumberOfObjectives();i++)
        		System.out.print(","+sol.getFitnessValue(i));
        	System.out.print("\n");
        }

        IAlgorithmResult<T> algorithmResult = algorithmState.getAlgorithmResult();
        

        algorithmState = null;

        return algorithmResult;
    }
	
	private void updateLastState(SPEA2AlgorithmState<T> algorithmState,ISolutionSet<T> archive) {
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
	public void updateArchiveState(SPEA2AlgorithmState<T> algorithmState,ISolutionSet<T> archive){
		algorithmState.updateArchiveState(archive);
	}

	@Override
	public ISolutionSet<T> iteration(AlgorithmState<T> algorithmState, ISolutionSet<T> solutionSet) throws Exception{
		
		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();
		ISolutionSet<T> newGeneration = new SolutionSet<T>();
		
		ISolutionSet<T> union = ((SPEA2AlgorithmState<T>)algorithmState).getArchive().union(solutionSet);
		MOUtils.assignSelectionValue(union, isMaximization);
		updateState(algorithmState,solutionSet);
		SolutionSet<T> newArchive = this.environmentalSelection(union, configuration.getMaximumArchiveSize(), isMaximization);
		updateArchiveState((SPEA2AlgorithmState<T>)algorithmState,newArchive);
		
		RecombinationParameters recombinationParameters = configuration.getRecombinationParameters();	
		int offSpringSize = recombinationParameters.getOffspringSize();
		
		serialRecombination(((SPEA2AlgorithmState<T>)algorithmState).getArchive(),newGeneration,isMaximization,offSpringSize);
		
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