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
package jecoli.algorithm.components.configuration;

import java.io.Serializable;
import java.util.List;

import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import jecoli.algorithm.components.evolver.DefaultEvolver;
import jecoli.algorithm.components.evolver.IEvolve;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionFactory;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.solution.SolutionSet;
import jecoli.algorithm.components.statistics.StatisticsConfiguration;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;

/**
 * The Class Configuration.
 */
public abstract class AbstractConfiguration<T extends IRepresentation> implements Serializable,IConfiguration<T> {

	private static final long serialVersionUID = 4104016123384292504L;

	/** The initial population. */
	protected ISolutionSet<T> initialPopulation;
	
	protected IRandomNumberGenerator randomNumberGenerator;
    /** The termination criteria. */
    protected ITerminationCriteria terminationCriteria;
    
    /** The evaluation function. */
    protected IEvaluationFunction<T> evaluationFunction;
    
    /** The statistic configuration. */
    protected StatisticsConfiguration statisticsConfiguration;
    
    protected Integer numberOfObjectives;

	protected Integer populationSize;
	
	protected String saveAlgorithmStateDirectoryPath;
	
	protected String saveAlgorithmStateFile;
	
	protected int saveAlgorithmStateIterationInterval;

	protected String problemBaseDirectory;
	
	protected List<IAlgorithmResultWriter<T>> algorithmResultWriterList;
	
	protected String UID;
	
	protected boolean doPopulationInitialization;
	
	protected IEvolve<T> evolver;
	
	public boolean isDoPopulationInitialization() {
		return doPopulationInitialization;
	}

	public void setDoPopulationInitialization(boolean doPopulationInitialization) {
		this.doPopulationInitialization = doPopulationInitialization;
	}

	public void setPopulationSize(Integer populationSize) {
		this.populationSize = populationSize;
	}
	
	
	public AbstractConfiguration(){
		UID ="";
	}
	
	public AbstractConfiguration(String UID){
		this.UID = UID;
	}
	
	public void setUID(String UID){
		this.UID = UID;
	}
	
	public String getUID(){
		return UID;
	}
	
	public void setProblemBaseDirectory(String directoryPath){
		problemBaseDirectory = directoryPath;
	}
	
	public String getProblemBaseDirectory(){
		return problemBaseDirectory;
	}
    
	
	protected void verifyDefaultConfigurationAttributes() throws InvalidConfigurationException, InvalidEvaluationFunctionInputDataException{
		if(UID ==  null)
			throw new InvalidConfigurationException("UID is null");
		
		if(problemBaseDirectory == null)
			throw new InvalidConfigurationException("Problem Base Directory null");
		
		if(saveAlgorithmStateIterationInterval < 0)
			throw new InvalidConfigurationException("Save Algorithm State Iteration Interval < 0");
		
		if(saveAlgorithmStateFile == null)
			throw new InvalidConfigurationException("Save Algorithm State File null");
		
		if(saveAlgorithmStateDirectoryPath == null)
			throw new InvalidConfigurationException("Save Algorithm State Directory Path");
		
		if((doPopulationInitialization == false) && (initialPopulation == null))
			throw new InvalidConfigurationException("Initial Population not defined") ;
		
		if(randomNumberGenerator == null) 
			throw new InvalidConfigurationException("Random Number Generator null");
		
		if(terminationCriteria == null) 
			throw new InvalidConfigurationException("Termination Criteria null");
		
		if(evaluationFunction == null) 
			throw new InvalidConfigurationException("Evaluation Function null");
		
		evaluationFunction.verifyInputData();
		
		if(statisticsConfiguration == null) 
			throw new InvalidConfigurationException("Statistics Configuration null");
		
		if(numberOfObjectives == null) 
			throw new InvalidConfigurationException("Number of objectives null");
		
		if(populationSize == null) 
			throw new InvalidConfigurationException("Population Size null");
	}
	
    public IRandomNumberGenerator getRandomNumberGenerator() {
		return randomNumberGenerator;
	}

	public void setRandomNumberGenerator(IRandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}


	public StatisticsConfiguration getStatisticConfiguration() {
		return statisticsConfiguration;
	}

	/**
	 * Sets the statistic configuration.
	 * 
	 * @param statisticConfiguration the new statistic configuration
	 */
	@Override
	public void setStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration) {
		this.statisticsConfiguration = statisticsConfiguration;
	}

	
	public ITerminationCriteria getTerminationCriteria() {
        return terminationCriteria;
    }

   
    public IEvaluationFunction<T> getEvaluationFunction() {
        return evaluationFunction;
    }

    
    public void setEvaluationFunction(IEvaluationFunction<T> evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    
    public void setTerminationCriteria(ITerminationCriteria terminationCriteria) {
        this.terminationCriteria = terminationCriteria;
    }
    
    /**
	 * Gets the user initial population.
	 * 
	 * @return the user initial population
	 */
	public ISolutionSet<T> getInitialPopulation() {
		return initialPopulation;
	}

	/**
	 * Sets the user initial population.
	 * 
	 * @param initialPopulation the new user initial population
	 */
	public void setInitialPopulation(ISolutionSet<T> initialPopulation){
		this.initialPopulation = initialPopulation;
	}  
	
	public Integer getNumberOfObjectives() {
		return numberOfObjectives;
	}

	public void setNumberOfObjectives(Integer numberOfObjectives) {
		this.numberOfObjectives = numberOfObjectives;
	}

	public StatisticsConfiguration getStatisticsConfiguration() {
		return statisticsConfiguration;
	}
	
	protected ISolutionSet<T> copyInitialPopulation(ISolutionSet<T> initialPopulation,ISolutionFactory<T> solutionFactory){
		ISolutionSet<T> initialSolutionSetCopy = new SolutionSet<T>();
		for(int i = 0; i< initialPopulation.getNumberOfSolutions();i++){
			ISolution<T> solution = initialPopulation.getSolution(i);
			ISolution<T> solutionToCopy = solutionFactory.copySolution(solution);
			initialSolutionSetCopy.add(solutionToCopy);
		}
		
		return initialSolutionSetCopy;
	}
	
	public int getPopulationSize(){
		return populationSize;
	}
	
	public void setPopulationSize(int populationSize){
		this.populationSize = populationSize;
	}
	
	@Override
	public void setSaveAlgorithmStateDirectoryPath(String directoryPath) {
		this.saveAlgorithmStateDirectoryPath = directoryPath;
	}

	@Override
	public void setSaveAlgorithmStateIterationInterval(int iterationInterval) {
		this.saveAlgorithmStateIterationInterval = iterationInterval;
	}

	@Override
	public String getSaveAlgorithmStateDirectoyPath() {
		return saveAlgorithmStateDirectoryPath;
	}

	@Override
	public int getSaveAlgorithmIterationInterval() {
		return saveAlgorithmStateIterationInterval;
	}
	
	@Override
	public String getSaveAlgorithmStateFile(){
		return saveAlgorithmStateFile;
	}
	
	public void setAlgorithmStateFile(String fileName){
		this.saveAlgorithmStateFile = fileName;
	}
	
	public List<IAlgorithmResultWriter<T>> getAlgorithmResultWriterList(){
		return algorithmResultWriterList;
	}
	
	public void setAlgorithmResultWriterList(List<IAlgorithmResultWriter<T>> algorithmResultWriterList){
		this.algorithmResultWriterList = algorithmResultWriterList;
	}

	public IEvolve<T> getEvolver(){
		if(this.evolver==null);
			setEvolver(new DefaultEvolver<T>());
		return evolver;
	}
	public void setEvolver(IEvolve<T> evolver){
		this.evolver=evolver;
	}

}