//package jecoli.algorithm.components.representation.hybridRealSet;
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
//public class HybridRealSetRepresentationFactory implements ISolutionFactory<HybridRealSetRepresentation>, Serializable{
//
//	private static final long serialVersionUID = -638115902506941607L;	
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
//	/** Maximum real value */
//	protected double maxRealValue;
//	
//	/** Minimum real value */
//	protected double minRealValue;
//	
//	/** Number of objectives set representation */
//	protected int numberOfObjectives;
//	
//	/** Number of steps between minRealValue and maxRealValue */
//	protected int numberOfSteps;
//	
//	/** Continuous or Discrete representation */
//	protected boolean isContinuous;
//	
//	protected static final double MINREALVALUE = 0.0;
//	
//	protected static final double MAXREALVALUE = 2.0;
//
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentationFactory
//	 * 
//	 * @param maxSetValue
//	 * @param minSetSize
//	 * @param maxSetSize
//	 * @param minRealValue
//	 * @param maxRealValue
//	 * @param numberOfObjectives
//	 */
//	public HybridRealSetRepresentationFactory(int maxSetValue,int minSetSize,int maxSetSize,double minRealValue,double maxRealValue,int numberOfObjectives)
//	{	
//		this(maxSetValue,minSetSize,maxSetSize,minRealValue,maxRealValue,numberOfObjectives,0,true);
//	}
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentationFactory
//	 * 
//	 * @param maxSetValue
//	 * @param minSetSize
//	 * @param maxSetSize
//	 * @param minRealValue
//	 * @param maxRealValue
//	 * @param numberOfObjectives
//	 * @param numberOfSteps
//	 */
//	public HybridRealSetRepresentationFactory(int maxSetValue,int minSetSize,int maxSetSize,double minRealValue,double maxRealValue,int numberOfObjectives,int numberOfSteps){	
//		this(maxSetValue,minSetSize,maxSetSize,minRealValue,maxRealValue,numberOfObjectives,numberOfSteps,false);
//	}
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentationFactory
//	 * 
//	 * @param maxSetValue
//	 * @param minSetSize
//	 * @param maxSetSize
//	 * @param minRealValue
//	 * @param maxRealValue
//	 * @param numberOfObjectives
//	 * @param numberOfSteps
//	 * @param isContinuous
//	 */
//	private HybridRealSetRepresentationFactory(int maxSetValue,int minSetSize,int maxSetSize,double minRealValue,double maxRealValue,int numberOfObjectives,int numberOfSteps,boolean isContinuous)
//	{	
//		this.maxSetSize = maxSetSize;
//		this.minSetSize = minSetSize;
//		
//		this.minRealValue = minRealValue;
//		this.maxRealValue = maxRealValue;
//		
//		this.maxSetValue = maxSetValue;
//		
//		this.numberOfObjectives = numberOfObjectives;
//
//		this.randomGenerator = new DefaultRandomNumberGenerator();
//		
//		this.numberOfSteps = numberOfSteps;
//		this.isContinuous = isContinuous;
//	}
//	
//	
//	
//	
//	@Override
//	public ISolution<HybridRealSetRepresentation> copySolution(ISolution<HybridRealSetRepresentation> solutionToCopy) {
//		
//		HybridRealSetRepresentation solutionGenome = solutionToCopy.getRepresentation();
//		
//		TreeSet<Integer> newGenomeSetValues = new TreeSet<Integer>();
//		List<Double> newGenomeRealValues = new ArrayList<Double>();
//		
//		int size = solutionGenome.getSize();
//		
//		for(int index=0; index<size ;index++)
//		{
//			newGenomeSetValues.add(solutionGenome.getSetValueAtIndex(index));
//			newGenomeRealValues.add(solutionGenome.getRealValueAtIndex(index));
//		}
//		
//		ISolution<HybridRealSetRepresentation> newSolution = new Solution<HybridRealSetRepresentation>(new HybridRealSetRepresentation(newGenomeRealValues, newGenomeSetValues),numberOfObjectives);
//		
//		return newSolution;
//	}
//	
//	
//	@Override
//	public ISolutionSet<HybridRealSetRepresentation> generateSolutionSet(int numberOfSolutions, IRandomNumberGenerator randomGenerator) {
//		ISolutionSet<HybridRealSetRepresentation> solutionSet = new SolutionSet<HybridRealSetRepresentation>();
//		
//		for(int i=0;i<numberOfSolutions;i++)
//		{
//			ISolution<HybridRealSetRepresentation> solution = generateSolution(randomGenerator);
//			solutionSet.add(solution);
//		}
//		
//		return solutionSet;
//	}
//	
//	private double generateDiscreteRealValue(double lowerBound,double upperBound){
//		double stepSize = upperBound / numberOfSteps;
//		int chosenStep = randomGenerator.nextInt((numberOfSteps+1));
//		return lowerBound+chosenStep*stepSize;
//	}
//	
//	private double generateContinuousRealValue(double lowerBound,double upperBound){
//		double generatedvalue = randomGenerator.nextDouble()*(upperBound-lowerBound)+lowerBound; 
//		return generatedvalue;
//	}
//	
//	public double generateRealValue(){
//		if(isContinuous)
//			return generateContinuousRealValue(minRealValue,maxRealValue);
//		else
//			return generateDiscreteRealValue(minRealValue,maxRealValue);
//	}
//	
//	public List<Double> generateRealvalue(double lowerBound,double upperBound,int numberOfValuesToGenerate)
//	{
//		List<Double> generatedValues = new ArrayList<Double>();
//		for(int i=0;i<numberOfValuesToGenerate;i++){
//			if(isContinuous)
//				generatedValues.add(generateContinuousRealValue(lowerBound, upperBound));
//			else
//				generatedValues.add(generateDiscreteRealValue(lowerBound, upperBound));
//		}
//		return generatedValues;
//	}
//	
//	public double generateRealValue(double lowerBound,double upperBound){
//		if(isContinuous)
//			return generateContinuousRealValue(lowerBound, upperBound);
//		else
//			return generateDiscreteRealValue(lowerBound, upperBound);
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
//	public ISolution<HybridRealSetRepresentation> generateSolution(int numberOfValuesToGenerate,IRandomNumberGenerator randomGenerator)
//	{
//		List<Double> realvalues = generateRealvalue(minRealValue, maxRealValue, numberOfValuesToGenerate);
//		TreeSet<Integer> setvalues = generateSetValues(numberOfValuesToGenerate);
//				
//		HybridRealSetRepresentation newSolution = new HybridRealSetRepresentation(realvalues, setvalues);
//		
//		return new Solution<HybridRealSetRepresentation>(newSolution,numberOfObjectives);
//	}
//		
//	@Override
//	public ISolution<HybridRealSetRepresentation> generateSolution(IRandomNumberGenerator randomGenerator) {
//		int setRepresentationSize = (int) (randomGenerator.nextDouble()*maxSetSize+minSetSize);		
//		return generateSolution(setRepresentationSize, randomGenerator);
//	}
//	
//	@Override
//	public ISolutionFactory<HybridRealSetRepresentation> deepCopy() {
//		return new HybridRealSetRepresentationFactory(maxSetValue, minSetSize, maxSetSize, minRealValue, maxRealValue, numberOfObjectives,numberOfSteps,isContinuous);	
//	}
//	
//	@Override
//	public int getNumberOfObjectives() {
//		return numberOfObjectives;
//	}
//
//	public int getMaxSetSize() {
//		return maxSetSize;
//	}
//
//
//	public void setMaxSetSize(int maxSetSize) {
//		this.maxSetSize = maxSetSize;
//	}
//
//
//	public int getMinSetSize() {
//		return minSetSize;
//	}
//
//
//	public void setMinSetSize(int minSetSize) {
//		this.minSetSize = minSetSize;
//	}
//
//
//	public int getMaxSetValue() {
//		return maxSetValue;
//	}
//
//
//	public void setMaxSetValue(int maxSetValue) {
//		this.maxSetValue = maxSetValue;
//	}
//
//
//	public double getMaxRealValue() {
//		return maxRealValue;
//	}
//
//
//	public void setMaxRealValue(double maxRealValue) {
//		this.maxRealValue = maxRealValue;
//	}
//
//
//	public double getMinRealValue() {
//		return minRealValue;
//	}
//
//
//	public void setMinRealValue(double minRealValue) {
//		this.minRealValue = minRealValue;
//	}
//
//
//	public void setNumberOfObjectives(int numberOfObjectives) {
//		this.numberOfObjectives = numberOfObjectives;
//	}
//
//	public int getNumberOfSteps() {
//		return numberOfSteps;
//	}
//
//	public void setNumberOfSteps(int numberOfSteps) {
//		this.numberOfSteps = numberOfSteps;
//	}
//
//	public boolean isContinuous() {
//		return isContinuous;
//	}
//
//	public void setContinuous(boolean isContinuous) {
//		this.isContinuous = isContinuous;
//	}
//	
//	
//}
