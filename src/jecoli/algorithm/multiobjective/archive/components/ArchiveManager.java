package jecoli.algorithm.multiobjective.archive.components;

import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.algorithm.AbstractAlgorithm;
import jecoli.algorithm.components.algorithm.AlgorithmStateEvent;
import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmStateListener;
import jecoli.algorithm.components.evaluationfunction.EvaluationFunctionEvent;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunctionListener;
import jecoli.algorithm.components.representation.IElementsRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.solution.SolutionSet;
import jecoli.algorithm.components.solution.comparator.SolutionPureFitnessComparator;
import jecoli.algorithm.multiobjective.archive.comparator.RepresentationComparator;
import jecoli.algorithm.multiobjective.archive.plotting.IPlotter;
import jecoli.algorithm.multiobjective.archive.sorting.ISolutionSorter;
import jecoli.algorithm.multiobjective.archive.sorting.ISortingFunction;
import jecoli.algorithm.multiobjective.archive.sorting.SolutionRepresentationSorter;
import jecoli.algorithm.multiobjective.archive.trimming.ITrimmingFunction;


public class ArchiveManager<E extends Comparable<E>, T extends IElementsRepresentation<E>> implements IAlgorithmStateListener,IEvaluationFunctionListener<T>{
	
	protected int MAXIMUM_ARCHIVE_SIZE = 200;
	protected double FITNESS_THRESHOLD = 0.000002;
	
	protected InsertionStrategy insertionEventType;
	protected InsertionStrategy insertionFilter;
	protected ProcessingStrategy processingStrategy;
		
	protected IAlgorithm<T> algorithm;
	protected ISolutionSet<T> archive;
	protected int maxArchiveSize;
	
	protected List<ITrimmingFunction<T>> trimmers;
	
	protected IPlotter<T> plotter;
	
	protected ISortingFunction<T> sorter;
	
	protected ISolutionSorter<T> solutionSorter;
	
	public ArchiveManager(IAlgorithm<T> alg){
		this(alg,InsertionStrategy.ADD_ON_SINGLE_EVALUATION_FUNCTION_EVENT, InsertionStrategy.ADD_ALL, ProcessingStrategy.NO_PROCESSING); 
	}
	
	public ArchiveManager(IAlgorithm<T> alg, InsertionStrategy insertionEventType, InsertionStrategy insertionFilter, ProcessingStrategy processingStrategy){
		this.archive = new SolutionSet<T>(new SolutionPureFitnessComparator<ISolution<T>>(false));
		this.algorithm = alg;
		alg.addAlgorithmStateListener(this);
		alg.getConfiguration().getEvaluationFunction().addEvaluationFunctionListener(this);
		this.insertionEventType = insertionEventType;
		this.insertionFilter = insertionFilter;
		this.processingStrategy = processingStrategy;
	}

	@Override
	public void processAlgorithmStateEvent(AlgorithmStateEvent event) {
		String id = event.getId();    

		switch(processingStrategy){
		
			case NO_PROCESSING: break;

			case PROCESS_ARCHIVE_ON_ANY_STATE_EVENT:{
				processArchive();
			} break;

			case PROCESS_ARCHIVE_ON_ITERATION_INCREMENT:{
				if(id.equals(AbstractAlgorithm.ITERATION_INCREMENT_EVENT))
					processArchive();
			} break;

			default: break;
		}


	}
	
	
	@Override
	public void processEvaluationFunctionEvent(EvaluationFunctionEvent<T> event) {
		String id = event.getId();
				
		switch(insertionEventType){
		
			case ADD_ON_SINGLE_EVALUATION_FUNCTION_EVENT:{
				if(id.equals(EvaluationFunctionEvent.SINGLE_SOLUTION_EVALUATION_EVENT)){
					ISolution<T> solution = event.getSolution();
					processSolution(solution);
				}
			} break;
			
			case ADD_ON_SOLUTIONSET_EVALUATION_FUNCTION_EVENT:{
				if(id.equals(EvaluationFunctionEvent.SOLUTIONSET_EVALUATION_EVENT)){
					ISolutionSet<T> solutionSet = event.getSolutionSet();
					processSolutionSet(solutionSet);
				}
				break;
			}
			
			default: break;
		}
		
		switch(processingStrategy){
		
			case NO_PROCESSING: break;
			
			case PROCESS_ARCHIVE_ON_ANY_EVALUATION_FUNCTION_EVENT:{
				processArchive();
			} break;
			
			case PROCESS_ARCHIVE_ON_SINGLE_EVALUATION_FUNCTION_EVENT:{
				if(id.equals(EvaluationFunctionEvent.SINGLE_SOLUTION_EVALUATION_EVENT))
					processArchive();
			} break;
			
			case PROCESS_ARCHIVE_ON_SOLUTIONSET_EVALUATION_FUNCTION_EVENT:{
				if(id.equals(EvaluationFunctionEvent.SOLUTIONSET_EVALUATION_EVENT))
					processArchive();
			} break;
			
			default: break;
			
		}
		
		
	}
	
	public void processSolution(ISolution<T> solution){
		
		switch(insertionFilter){
			case ADD_ALL:{
				addSolution(solution);
			} break;
			
			case ADD_ALL_ORDERED:{				
//TODO:			addOrdered(solution);
			} break;
			
			case ADD_NO_REPEAT:{
				addNoRepeat(solution);
			} break;
			
			case ADD_NO_REPEAT_ORDERED:{
//TODO:			addNoRepeatOrdered(solution);
			} break;
			
			case ADD_SMART:{
				addSmart(solution);
			} break;
			
			default: break;
		}
	}
	
	public void processSolutionSet(ISolutionSet<T> solutionSet){
		for(ISolution<T> solution : solutionSet.getListOfSolutions())
			processSolution(solution);
	}

	public void processArchive() {
		if(trimmers!=null)
			for(ITrimmingFunction<T> t : trimmers)
				archive = t.trimm(archive);	
		
		if(plotter!=null){
			plotter.plot(archive);
		}
		
	}

	public void sort(){
		if(sorter!=null)
			sorter.sort(archive);
	}
	
	public void sortSolution(ISolution<T> solution){
		if(solutionSorter==null)
			solutionSorter = new SolutionRepresentationSorter<T>();
		solutionSorter.sort(solution);
	}
	
	
	/**
	 * Assumes solution is sorted and solutionset is ordered
	 *  
	 * @param solution
	 * @return
	 */
	public int findSolution(ISolution<T> solution){
		int c = -1;
		int i = 0;
		
		RepresentationComparator<T> comp = new RepresentationComparator<T>();
		
		while(c < 0 && i< archive.getNumberOfSolutions()){
			ISolution<T> sol = archive.getSolution(i);
			c = comp.compare(solution.getRepresentation(), sol.getRepresentation());
			
			if (c==0) return i;
			else if (c < 0) i++;
			else return -1;
		}
		
		return -1;
	}
	
	public void printArchive(){
		for(ISolution<T> sol : archive.getListOfSolutions())
			System.out.println(sol.getRepresentation().stringRepresentation());
	}
	
	public void addSolution(ISolution<T> solution){
		if(archive == null)
			archive = new SolutionSet<T>();
		
		archive.add(solution);	
	}
	
	public void addSolution(int index, ISolution<T> solution){
		if(archive == null)
			archive = new SolutionSet<T>();
		
		archive.add(index, solution);
	}
	
	public void addSmart(ISolution<T> solution){
		if(archive==null)
			archive = new SolutionSet<T>();
		
		if(!checkIfSolutionExists(solution))
			archive.add(solution);
	}
	
	/**
	 * Adds a solution respecting the order of its representation
	 * 
	 * @param solution the solution to be added
	 * 
	 * @return the index where the solution was inserted into the archive
	 */
	public int addOrdered(ISolution<T> solution){
		if(archive == null)
			archive = new SolutionSet<T>();
		
		sortSolution(solution);
		
		if (findSolution(solution)>=0) {
			return -1;
		}
		
		if(archive.getNumberOfSolutions()==0){
			archive.getListOfSolutions().add(0, solution);
			return 0;
		}
		else {
			for(int i=0; i< archive.getNumberOfSolutions(); i++){
				ISolution<T> sol = archive.getSolution(i);
				int sizeLocal = sol.getRepresentation().getNumberOfElements();
				int minSize = Math.min(sizeLocal,solution.getRepresentation().getNumberOfElements());
				for(int j = 0; j < minSize; j++){
					E kLocal = sol.getRepresentation().getElementAt(j);
					E kCandidate = solution.getRepresentation().getElementAt(j);
					int comp = kLocal.compareTo(kCandidate);
					if(comp==0){
						if(j == minSize-1 && sizeLocal>minSize){
							addSolution(i, solution);
							return i;
						}
						continue;
					}
					else if(comp<0)
						break;
					else{
						addSolution(i, solution);
						return i;
					}
				}			
			}
			archive.add(solution);
			
			return archive.getNumberOfSolutions();
		}		
	}
	
	public boolean checkIfSolutionExists(ISolution<T> solution){

		if (findSolution(solution)>=0) {
			return true;
		}

		Double[] fitsSol = solution.getFitnessValuesArray();

		List<ISolution<T>> ss = findSubsetSolutions(solution);
		for(int k=0; k < ss.size(); k++){

			ISolution<T> sol = ss.get(k);
			Double[] fitsSub = sol.getFitnessValuesArray();			
			boolean allBetter = true;
			for(int j=0; j < fitsSol.length; j++)
				if (fitsSol[j] - fitsSub[j] > FITNESS_THRESHOLD) 
					allBetter = false; 
			if (allBetter) {
				return true;
			}
		}

		return false;
	}
	
	public List<ISolution<T>> findSubsetSolutions (ISolution<T> targetSolution){
		List<ISolution<T>> list = new ArrayList<ISolution<T>>();
		
		for(int i=0; i< archive.getNumberOfSolutions();i++){
			ISolution<T> sol = archive.getSolution(i); 
			T solRep = sol.getRepresentation();
			T targetRep = targetSolution.getRepresentation();
			
			if (targetRep.containsRepresentation(solRep)) 
				list.add(sol);
		}
		
		return list;
	}

	public List<ISolution<T>> findSupersetSolutions (ISolution<T> targetSolution){
		List<ISolution<T>> list = new ArrayList<ISolution<T>>();
		
		for(int i=0; i< archive.getNumberOfSolutions(); i++){
			ISolution<T> sol = archive.getSolution(i);
			T solRep = sol.getRepresentation();
			T targetRep= targetSolution.getRepresentation();

			if (targetRep.isContainedInRepresentation(solRep))
				list.add(sol);
		}
		
		return list;
	}
	
	public void addNoRepeat(ISolution<T> solution){
		if(archive == null)
			archive = new SolutionSet<T>();
		
		if(!archive.containsGenomeOnly(solution))
			archive.add(solution);
		
//		System.out.println("=== SOLUTION ===");
//		ArrayList<ISolution<T>> solist = new ArrayList<ISolution<T>>();
//		solist.add(solution);
//		print(solist,"\t");
////		printArchive();
//		System.out.println("\t SUBSET:");
//		print(this.findSubsetSolutions(solution),"\t\t");
//		System.out.println("\t SUPERSET:");
//		print(this.findSupersetSolutions(solution),"\t\t");
//		
//		System.out.println("=== END ===\n");
	}
	
	public void print(List<ISolution<T>> sols,String t){
		for(ISolution<T> sol: sols)
			System.out.println(t+sol.getRepresentation().stringRepresentation());
	}
	
	public List<ITrimmingFunction<T>> getTrimmers(){
		return trimmers;
	}
	
	public void addTrimmingFunction(ITrimmingFunction<T> trimmingFunction) throws IllegalArgumentException{
		if(trimmers ==null)
			trimmers = new ArrayList<ITrimmingFunction<T>>();
		
		if(!trimmers.contains(trimmingFunction))
			trimmers.add(trimmingFunction);
		else throw new IllegalArgumentException("Trimming function ["+ trimmingFunction.getClass().getCanonicalName()+"] already exists");
			
	}

	public IPlotter<T> getPlotter() {
		return plotter;
	}

	public void setPlotter(IPlotter<T> plotter) {
		this.plotter = plotter;
	}

	public ISolutionSet<T> getArchive() {
		return archive;
	}

	/**
	 * @return the fITNESS_THRESHOLD
	 */
	public double getFITNESS_THRESHOLD() {
		return FITNESS_THRESHOLD;
	}

	/**
	 * @param fITNESS_THRESHOLD the fITNESS_THRESHOLD to set
	 */
	public void setFITNESS_THRESHOLD(double fITNESS_THRESHOLD) {
		FITNESS_THRESHOLD = fITNESS_THRESHOLD;
	}

	/**
	 * @return the mAXIMUM_ARCHIVE_SIZE
	 */
	public int getMaximumArchiveSize() {
		return MAXIMUM_ARCHIVE_SIZE;
	}

	/**
	 * @param maximumArchiveSize the mAXIMUM_ARCHIVE_SIZE to set
	 */
	public void setMaximumArchiveSize(int maximumArchiveSize) {
		MAXIMUM_ARCHIVE_SIZE = maximumArchiveSize;
	}
	
	
}
