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
package jecoli.algorithm.singleobjective.evolutionary;

import java.util.List;

import jecoli.algorithm.components.algorithm.AbstractAlgorithm;
import jecoli.algorithm.components.algorithm.AlgorithmState;
import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.AbstractEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.EvaluationFunctionEvent;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.operator.ISelectionOperator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionFactory;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.solution.SolutionSet;



/**
 * The Class EvolutionaryAlgorithm.
 */
public class EvolutionaryAlgorithm<T extends IRepresentation, S extends ISolutionFactory<T>> extends AbstractAlgorithm<T,EvolutionaryConfiguration<T,S>>{

	
	private static final long serialVersionUID = -6452495284244065991L;

	/**
	 * Instantiates a new evolutionary algorithm.
	 * 
	 * @param configuration the configuration
	 * 
	 * @throws InvalidConfigurationException the invalid configuration exception
	 * @throws InvalidEvaluationFunctionInputDataException 
	 */
	public EvolutionaryAlgorithm(EvolutionaryConfiguration<T,S> configuration) throws Exception {
		super(configuration);
	}

	
	@Override
	public ISolutionSet<T> initialize() {
		IRandomNumberGenerator randomGenerator = configuration.getRandomNumberGenerator();
		boolean verifyPopulationInitialization = configuration.verifyPopulationInitialization();
		ISolutionSet<T> solutionSet = null;
		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		
		if(verifyPopulationInitialization)
			solutionSet = generateInitialPopulation(randomGenerator);
		else
			solutionSet = configuration.getInitialPopulation(); 
		
		evaluationFunction.evaluate(solutionSet);
		algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(solutionSet.getNumberOfSolutions());
		return solutionSet;
	}

	/**
	 * Generate initial population.
	 * 
	 * @return the i solution set
	 */
	protected ISolutionSet<T> generateInitialPopulation(IRandomNumberGenerator randomGenerator) {
		int populationSize = configuration.getPopulationSize();
	
		ISolutionFactory<T> factory = configuration.getSolutionFactory();
		ISolutionSet<T> population = factory.generateSolutionSet(populationSize,randomGenerator);
		
		population.getListOfSolutions().iterator();
		
		
        return population;
	}

	@Override
	public ISolutionSet<T> iteration(AlgorithmState<T> algorithmState, ISolutionSet<T> solutionSet) throws Exception{
		ISolutionSet<T> newGeneration = new SolutionSet<T>();
		
		RecombinationParameters recombinationParameters = configuration.getRecombinationParameters();
		int numberOfSurvivorIndividuals = recombinationParameters.getNumberOfSurvivors();
		int elitismValue = recombinationParameters.getElitismValue();
		int offSpringSize = recombinationParameters.getOffspringSize();
		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();
		
		
		if(elitismValue > 0)
			selectElitistIndividuals(solutionSet,newGeneration,isMaximization,elitismValue);
		
		if(numberOfSurvivorIndividuals > 0)
			selectSurvivorIndividuals(solutionSet,newGeneration,isMaximization,numberOfSurvivorIndividuals);
		
		if(offSpringSize > 0)
			selectReproductionIndividuals(solutionSet,newGeneration,isMaximization,offSpringSize);

		if (configuration.getRefreshEvalutation()){
			evaluationFunction.evaluate(newGeneration);
			algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(newGeneration.getNumberOfSolutions());
		} else algorithmState.incrementCurrentIterationNumberOfFunctionEvaluations(offSpringSize);
		

		return newGeneration;
	}
	
	/**
	 * Select reproduction individuals.
	 * 
	 * @param solutionSet the solution set
	 * @param newGeneration the new generation
	 * @param isMaximization the is maximization
	 * @param offSpringSize the off spring size
	 * 
	 * @throws Exception the exception
	 */
	protected void selectReproductionIndividuals(ISolutionSet<T> solutionSet,ISolutionSet<T> newGeneration, boolean isMaximization, int offSpringSize) throws Exception {
		int currentNumberOfIndividuals = 0;
		
		ISolutionSet<T> listOffspring = new SolutionSet<T>();
		
		while(currentNumberOfIndividuals < offSpringSize){
			ISelectionOperator<T> selectOperator = configuration.getSelectionOperator();
			IReproductionOperator<T,S> operator = configuration.selectReproductionOperator();
			int numberOfInputIndividuals = operator.getNumberOfInputSolutions();
			int numberOfOutputIndividuals = operator.getNumberOfOutputSolutions();
			IRandomNumberGenerator randomNumberGenerator = configuration.getRandomNumberGenerator();
			List<ISolution<T>> selectedIndividuals = selectOperator.selectSolutions(numberOfInputIndividuals, solutionSet, isMaximization,randomNumberGenerator);
			S solutionFactory = configuration.getSolutionFactory();
			List<ISolution<T>> newIndividuals = operator.apply(selectedIndividuals,solutionFactory,randomNumberGenerator);
			currentNumberOfIndividuals += numberOfOutputIndividuals;
			
			if(currentNumberOfIndividuals > offSpringSize){
				int numberOfExcessIndividuals = currentNumberOfIndividuals -offSpringSize;
				newIndividuals = newIndividuals.subList(0,numberOfExcessIndividuals);
			}
			
			/* Miguel: added this if to save evaluations if refresh evaluation is on */
			if (!configuration.getRefreshEvalutation())
				configuration.getEvaluationFunction().evaluate(new SolutionSet<T>(newIndividuals));
			
			if(configuration.steadyStateReplacement()){
				List<ISolution<T>> steadyStateSolutionList = selectFittestIndividuals(newIndividuals,selectedIndividuals);
				listOffspring.add(steadyStateSolutionList);
			}else 
				listOffspring.add(newIndividuals);
			
		}
		
		newGeneration.add(listOffspring.getListOfSolutions());
	}

	protected List<ISolution<T>> selectFittestIndividuals(List<ISolution<T>> newIndividualList,List<ISolution<T>> selectedIndividualList) {
		ISolutionSet<T> steadyStateSolutionSet = new SolutionSet<T>();
		
		for(ISolution<T> newIndividual:newIndividualList)
			steadyStateSolutionSet.add(newIndividual);
		for(ISolution<T> selectedIndividual:selectedIndividualList)
			steadyStateSolutionSet.add(selectedIndividual);
		
		if(configuration.getEvaluationFunction().isMaximization())
			return steadyStateSolutionSet.getHighestValuedSolutions(newIndividualList.size());
		
		return steadyStateSolutionSet.getLowestValuedSolutions(newIndividualList.size());
	}

	/**
	 * Select survivor individuals.
	 * 
	 * @param solutionSet the solution set
	 * @param newGeneration the new generation
	 * @param isMaximization the is maximization
	 * @param numberOfSurvivorIndividuals the number of survivor individuals
	 * 
	 * @throws Exception the exception
	 */
	protected void selectSurvivorIndividuals(ISolutionSet<T> solutionSet,ISolutionSet<T> newGeneration, boolean isMaximization, int numberOfSurvivorIndividuals) throws Exception{
		ISelectionOperator<T> selectionOperator = configuration.selectSurvivorSelectionOperator();
		IRandomNumberGenerator randomNumberGenerator = configuration.getRandomNumberGenerator();
		List<ISolution<T>> selectedGenomes = selectionOperator.selectSolutions(numberOfSurvivorIndividuals,solutionSet,isMaximization, randomNumberGenerator);

		newGeneration.add(selectedGenomes);
	}

	/**
	 * Select elitist individuals.
	 * 
	 * @param solutionSet the solution set
	 * @param newGeneration the new generation
	 * @param isMaximization the is maximization
	 * @param elitismValue the elitism value
	 */
	protected void selectElitistIndividuals(ISolutionSet<T> solutionSet,ISolutionSet<T> newGeneration, boolean isMaximization, int elitismValue) {
		List<ISolution<T>> solutionList = null;

		if(isMaximization)
			solutionList = solutionSet.getHighestValuedSolutions(elitismValue);
		else
			solutionList = solutionSet.getLowestValuedSolutions(elitismValue);

		newGeneration.add(solutionList);
	}

	@Override
	public IAlgorithm<T> deepCopy() throws Exception {
		EvolutionaryConfiguration<T,S> configurationCopy = configuration.deepCopy(); 
		return new EvolutionaryAlgorithm<T,S>(configurationCopy);
	}
}