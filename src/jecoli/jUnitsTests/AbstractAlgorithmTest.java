package jecoli.jUnitsTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.configuration.IConfiguration;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.operator.InvalidNumberOfInputSolutionsException;
import jecoli.algorithm.components.operator.InvalidNumberOfOutputSolutionsException;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionContainer;
import jecoli.algorithm.components.solution.ISolutionFactory;

import org.junit.Test;


public abstract class AbstractAlgorithmTest<T extends IRepresentation,F extends ISolutionFactory<T>> extends AbstractTestClass<T,F>{

	protected IAlgorithm<T> algorithm;
	
	protected Boolean isMaximization;
	protected Integer populationSize;
	protected Integer numberOfGenerations;
	
	protected abstract void specificAlgorithmTests();
	
	@Test
	public void testSolutions(List<ISolution<T>> actualResults,List<ISolution<T>> expectedSolutions) 
								throws InvalidNumberOfInputSolutionsException, InvalidNumberOfOutputSolutionsException {
		assertTrue("Acutal results size is bigger than expected solutions size.",actualResults.size() <= expectedSolutions.size());
		IConfiguration<T> configuration = algorithm.getConfiguration();
		boolean isMaximization = configuration.getEvaluationFunction().isMaximization();
		
		for(ISolution<T> solution:actualResults)
			assertTrue(this.testName+": Is Maximization: "+isMaximization,verifyExpectedSolutionList(solution, expectedSolutions));
		
	}
	
	protected boolean verifyExpectedSolutionList(ISolution<T> solution, List<ISolution<T>> expectedSolutionList) {
		T solutionRepresentation = solution.getRepresentation();
		
		for(ISolution<T> expectedSolution:expectedSolutionList){
			T expectedSolutionRepresentation = expectedSolution.getRepresentation();
			if(expectedSolutionRepresentation.stringRepresentation().equals(solutionRepresentation.stringRepresentation()))
				return true;
		}
		
		return false;
	}


	public Boolean getIsMaximization() {
		return isMaximization;
	}

	public void setIsMaximization(Boolean isMaximization) {
		this.isMaximization = isMaximization;
	}

	public Integer getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(Integer populationSize) {
		this.populationSize = populationSize;
	}

	public Integer getNumberOfGenerations() {
		return numberOfGenerations;
	}

	public void setNumberOfGenerations(Integer numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}

	public IAlgorithm<T> getAlgorithm() {
		return algorithm;
	}

	protected ISolutionContainer<T> runAlgorithm()
	{
		try {			
			 return algorithm.run().getSolutionContainer();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void testSolution(ISolutionContainer<T> solutionContainer)
	{
		IConfiguration<T> configuration = algorithm.getConfiguration();
		boolean isMaximization = configuration.getEvaluationFunction().isMaximization();
		
		try {
			List<ISolution<T>> actualList = new ArrayList<ISolution<T>>();
			actualList.add(solutionContainer.getBestSolutionCellContainer(isMaximization).getSolution());
	
			testSolutions(actualList, this.expectedSolutions);
		} 
		catch (InvalidNumberOfInputSolutionsException e) {e.printStackTrace();} 
		catch (InvalidNumberOfOutputSolutionsException e) {e.printStackTrace();}
	}
	
	@Override
	public void test()
	{
		ISolutionContainer<T> solutionContainer = runAlgorithm();
		testSolution(solutionContainer);
		specificAlgorithmTests();
	}
	
	@Override
	public void validate() throws Exception {
		if(this.randomNumberGenerator == null) throw new Exception(this.testName+": Validation error due to a missing set in Seed atribute.");
		if(this.initialPopulation == null) throw new Exception(this.testName+": Validation error due to a missing set in InitialPopulation atribute.");
		if(this.expectedSolutions == null) throw new Exception(this.testName+": Validation error due to a missing set in ExpectedPopulation atribute.");
		if(this.populationSize == null) throw new Exception(this.testName+": Validation error due to a missing set in PopulationSize atribute.");
		if(this.numberOfGenerations == null) throw new Exception(this.testName+": Validation error due to a missing set in NumberOfGenerations atribute.");
		if(this.isMaximization == null) throw new Exception(this.testName+": Validation error due to a missing set in IsMaximization atribute.");
		
	}


	@Override
	public void deepcopyTest() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
