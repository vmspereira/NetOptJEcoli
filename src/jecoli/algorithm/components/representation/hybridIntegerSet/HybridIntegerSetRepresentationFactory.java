//package jecoli.algorithm.components.representation.hybridIntegerSet;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TreeSet;
//
//import jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
//import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
//import jecoli.algorithm.components.solution.ISolution;
//import jecoli.algorithm.components.solution.ISolutionFactory;
//import jecoli.algorithm.components.solution.ISolutionSet;
//import jecoli.algorithm.components.solution.Solution;
//import jecoli.algorithm.components.solution.SolutionSet;
//
//public class HybridIntegerSetRepresentationFactory implements ISolutionFactory<HybridIntegerSetRepresentation>, Serializable{
//
//	
//	private static final long serialVersionUID = -2184959231856681867L;
//
//	/** Random generator*/
//	protected IRandomNumberGenerator randomGenerator;
//	
//	/** Maximum set size */
//	protected int maxSetSize;
//	
//	/** Minimum set size */
//	protected int minSetSize;
//	
//	/** Maximum set value */
//	protected int maxSetValue;
//	
//	/** Maximum integer value */
//	protected int maxIntegerValue;
//	
//	/** Minimum integer value */
//	protected int minIntegerValue;
//	
//	/** Number of objectives set representation */
//	protected int numberOfObjectives;
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentationFactory
//	 * 
//	 * @param minSetSize
//	 * @param maxSetSize
//	 * @param maxSetValue
//	 * @param minIntegerValue
//	 * @param maxIntegerValue
//	 * @param numberOfObjectives
//	 */
//	public HybridIntegerSetRepresentationFactory(int minSetSize, int maxSetSize, int maxSetValue, int minIntegerValue, int maxIntegerValue, int numberOfObjectives){
//		this.maxSetSize = maxSetSize;
//		this.minSetSize = minSetSize;
//		this.maxSetValue = maxSetValue;
//		this.maxIntegerValue = maxIntegerValue;
//		this.minIntegerValue = minIntegerValue;
//		this.numberOfObjectives = numberOfObjectives;
//		randomGenerator = new DefaultRandomNumberGenerator();
//	}
//	
//	
//	@Override
//	public ISolution<HybridIntegerSetRepresentation> copySolution(ISolution<HybridIntegerSetRepresentation> solutionToCopy) {
//		
//		HybridIntegerSetRepresentation solutionGenome = solutionToCopy.getRepresentation();
//		
//		TreeSet<Integer> newGenomeSetValues = new TreeSet<Integer>();
//		List<Integer> newGenomeIntegerValues = new ArrayList<Integer>();
//		
//		int size = solutionGenome.getSize();
//		
//		for(int index=0; index<size ;index++)
//		{
//			newGenomeSetValues.add(solutionGenome.getSetValueAtIndex(index));
//			newGenomeIntegerValues.add(solutionGenome.getIntegerValueAtIndex(index));
//		}
//		
//		ISolution<HybridIntegerSetRepresentation> newSolution = new Solution<HybridIntegerSetRepresentation>(new HybridIntegerSetRepresentation(newGenomeIntegerValues, newGenomeSetValues),numberOfObjectives);
//		
//		return newSolution;
//	}
//	
//	@Override
//	public ISolutionSet<HybridIntegerSetRepresentation> generateSolutionSet(int numberOfSolutions, IRandomNumberGenerator randomGenerator) {
//		ISolutionSet<HybridIntegerSetRepresentation> solutionSet = new SolutionSet<HybridIntegerSetRepresentation>();
//		
//		for(int i=0;i<numberOfSolutions;i++)
//		{
//			ISolution<HybridIntegerSetRepresentation> solution = generateSolution(randomGenerator);
//			solutionSet.add(solution);
//		}
//		
//		return solutionSet;
//	}
//	
//	public TreeSet<Integer> generateSetValues(int numberOfValuesToGenerate){
//		TreeSet<Integer> genome = new TreeSet<Integer>();
//		
//		for (int i = 0; i < numberOfValuesToGenerate; i++) {
//			Integer element;
//			do {
//				element = generateSetValue();
//			} while(genome.contains(element));
//			genome.add(element);
//		}
//		
//		return genome;
//	}
//	
//	public int generateSetValue(){
//		return (int) (randomGenerator.nextDouble()*(maxSetValue-1));
//	}
//	
//	public int generateIntegerValue(){
//		return generateIntegerValue(minIntegerValue,maxIntegerValue);
//	}
//	
//	public int generateIntegerValue(int lowerBound,int upperBound){
//		int generatedIntegerValue;
//		do{
//			generatedIntegerValue = lowerBound + (int) (randomGenerator.nextDouble() * ((upperBound-lowerBound) + 1));
//		}while(generatedIntegerValue == 0.0);
//		
//		return generatedIntegerValue; 
//	}
//	
//	public List<Integer> generateIntegerValues(int lowerBound, int upperBound, int numberOfValuesToGenerate){
//		List<Integer> integerList = new ArrayList<Integer>();
//		
//		for(int i=0; i<numberOfValuesToGenerate ;i++)
//			integerList.add(generateIntegerValue(lowerBound, upperBound));
//		
//		return integerList;
//	}
//	
//	public ISolution<HybridIntegerSetRepresentation> generateSolution(int numberOfValuesToGenerate,IRandomNumberGenerator randomGenerator)
//	{
//		List<Integer> integerValues = generateIntegerValues(minIntegerValue, maxIntegerValue, numberOfValuesToGenerate);
//		TreeSet<Integer> setvalues = generateSetValues(numberOfValuesToGenerate);
//				
//		HybridIntegerSetRepresentation newSolution = new HybridIntegerSetRepresentation(integerValues, setvalues);
//		
//		return new Solution<HybridIntegerSetRepresentation>(newSolution,numberOfObjectives);
//	}
//	
//	@Override
//	public ISolution<HybridIntegerSetRepresentation> generateSolution(IRandomNumberGenerator randomGenerator) {
//		int setRepresentationSize = minSetSize + (int) (randomGenerator.nextDouble() * ((maxSetSize-minSetSize) + 1));
//		return generateSolution(setRepresentationSize, randomGenerator);
//	}
//	
//	@Override
//	public ISolutionFactory<HybridIntegerSetRepresentation> deepCopy() {
//		return new HybridIntegerSetRepresentationFactory(minSetSize, maxSetSize, maxSetValue, minIntegerValue, maxIntegerValue, numberOfObjectives);	
//	}
//
//
//	@Override
//	public int getNumberOfObjectives() {
//		return numberOfObjectives;
//	}
//
//
//	/**
//	 * @return the randomGenerator
//	 */
//	public IRandomNumberGenerator getRandomGenerator() {
//		return randomGenerator;
//	}
//
//
//	/**
//	 * @param randomGenerator the randomGenerator to set
//	 */
//	public void setRandomGenerator(IRandomNumberGenerator randomGenerator) {
//		this.randomGenerator = randomGenerator;
//	}
//
//
//	/**
//	 * @return the maxSetSize
//	 */
//	public int getMaxSetSize() {
//		return maxSetSize;
//	}
//
//
//	/**
//	 * @param maxSetSize the maxSetSize to set
//	 */
//	public void setMaxSetSize(int maxSetSize) {
//		this.maxSetSize = maxSetSize;
//	}
//
//
//	/**
//	 * @return the minSetSize
//	 */
//	public int getMinSetSize() {
//		return minSetSize;
//	}
//
//
//	/**
//	 * @param minSetSize the minSetSize to set
//	 */
//	public void setMinSetSize(int minSetSize) {
//		this.minSetSize = minSetSize;
//	}
//
//
//	/**
//	 * @return the maxSetValue
//	 */
//	public int getMaxSetValue() {
//		return maxSetValue;
//	}
//
//
//	/**
//	 * @param maxSetValue the maxSetValue to set
//	 */
//	public void setMaxSetValue(int maxSetValue) {
//		this.maxSetValue = maxSetValue;
//	}
//
//
//	/**
//	 * @return the maxIntegerValue
//	 */
//	public int getMaxIntegerValue() {
//		return maxIntegerValue;
//	}
//
//
//	/**
//	 * @param maxIntegerValue the maxIntegerValue to set
//	 */
//	public void setMaxIntegerValue(int maxIntegerValue) {
//		this.maxIntegerValue = maxIntegerValue;
//	}
//
//
//	/**
//	 * @return the minIntegerValue
//	 */
//	public int getMinIntegerValue() {
//		return minIntegerValue;
//	}
//
//
//	/**
//	 * @param minIntegerValue the minIntegerValue to set
//	 */
//	public void setMinIntegerValue(int minIntegerValue) {
//		this.minIntegerValue = minIntegerValue;
//	}
//	
//}
