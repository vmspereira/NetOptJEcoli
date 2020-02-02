package jecoli.algorithm.components.evaluationfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.multiobjective.archive.aggregation.IAggregationFunction;
import jecoli.SystemConf;

public abstract class AbstractMultiobjectiveEvaluationFunction<T extends IRepresentation>
		implements IEvaluationFunction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 608090232509317733L;

	/** The is maximization. */
	protected boolean isMaximization;
	/** use multiprocessing **/
	protected boolean mp = true;
	/** By default use half of the available processors **/
	protected int n_threads = SystemConf.getPropertyInt("multithread.nmp", Runtime.getRuntime().availableProcessors()/2);
	protected List<ComputeUnit> workers = null;

	protected List<IEvaluationFunctionListener<T>> listeners = null;

	protected IAggregationFunction fitnessAggregation = null;

	protected boolean performFitnessAggregation = false;

	/**
	 * Instantiates a new evaluation function.
	 * 
	 * @param isMaximization the is maximization
	 */
	public AbstractMultiobjectiveEvaluationFunction(boolean isMaximization) {
		this.isMaximization = isMaximization;
	}

	@Override
	public void evaluate(ISolutionSet<T> solutionSet) {
		if (!this.mp) {
			for (int i = 0; i < solutionSet.getNumberOfSolutions(); i++) {
				ISolution<T> solution = solutionSet.getSolution(i);
				evaluateSingleSolution(solution);
			}
		} else {
			try {
				evaluateMP(solutionSet);
			} catch (Exception e) {
				for (int i = 0; i < solutionSet.getNumberOfSolutions(); i++) {
					ISolution<T> solution = solutionSet.getSolution(i);
					evaluateSingleSolution(solution);
				}
			}
		}
		if (listeners != null && !listeners.isEmpty())
			notifyEvaluationFunctionListeners(EvaluationFunctionEvent.SOLUTIONSET_EVALUATION_EVENT,
					String.valueOf(solutionSet.getNumberOfSolutions()), solutionSet);
	}
	
	public boolean isMPEnable(){
		boolean enabled = false;
		if(this.mp){
			try {
				IEvaluationFunction<T> evaluator=this.deepCopy();
				if(evaluator!=null)
					enabled = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return enabled;
	}

	private void evaluateMP(ISolutionSet<T> solutionSet) throws Exception {
		if (this.workers == null) {
			workers = new ArrayList<ComputeUnit>();
			for (int i = 0; i < this.n_threads; i++) {
				IEvaluationFunction<T> evaluator=this.deepCopy();
				if(evaluator==null){
					this.mp = false;
					this.workers = null;
					throw new Exception("DeepCopy Interface needs to be implemented to use multiprocessing");
				}
				workers.add(new ComputeUnit(evaluator));
			}
			System.out.println("Using "+this.n_threads+" threads.");
		}
		
		
		ExecutorService exec = Executors.newFixedThreadPool(n_threads);
		CountDownLatch latch = new CountDownLatch(n_threads);
		
		List<ISolution<T>> list = solutionSet.getListOfSolutions();
		
		int n = solutionSet.getNumberOfSolutions()/n_threads;
		int t = solutionSet.getNumberOfSolutions()%n_threads;
		int from = 0;
		for(int i=0;i<n_threads;i++){	
			int to= from+n;
			if(t>0){to+=1;t--;}
			List<ISolution<T>> sub =list.subList(from,to);
			workers.get(i).setList(sub);
			from = to;
		}
		
		for(ComputeUnit r : workers) {
		    r.setLatch(latch);
		    exec.execute(r);
		}
		latch.await();
		exec.shutdown();
	}
	
	public void setNumberOfProcessors(int mp) {
		this.n_threads = mp;
	}
	
	public int getNumberOfProcessors() {
		return this.n_threads;
	}

	@Override
	public void evaluateSingleSolution(ISolution<T> solution) {
		Double[] fitnessValues = null;

		try {
			fitnessValues = evaluateMO(solution.getRepresentation());

			boolean fitsOK = true;

			for (int i = 0; i < fitnessValues.length; i++) {
				if (fitnessValues[i] == null) {
					if (isMaximization)
						fitnessValues[i] = Double.MIN_VALUE;
					else
						fitnessValues[i] = Double.MAX_VALUE;

					fitsOK = false;
				}

			}

			solution.setFitnessValues(fitnessValues);
			if (performFitnessAggregation) {
				double rawfitness;
				if (fitsOK)
					rawfitness = fitnessAggregation.aggregate(fitnessValues);
				else
					rawfitness = (isMaximization) ? Double.MIN_VALUE : Double.MAX_VALUE;

				solution.setFitnessValue(rawfitness);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (isMaximization)
				solution.setFitnessValue(Double.MIN_VALUE);
			else
				solution.setFitnessValue(Double.MAX_VALUE);
		}

		if (listeners != null && !listeners.isEmpty())
			notifyEvaluationFunctionListeners(EvaluationFunctionEvent.SINGLE_SOLUTION_EVALUATION_EVENT, "", solution);
	}

	public boolean isMaximization() {
		return isMaximization;
	}

	public abstract int getNumberOfObjectives();

	/**
	 * Evaluate.
	 * 
	 * @param solutionRepresentation the solution representation
	 * 
	 * @return the list
	 * 
	 * @throws Exception the exception
	 */
	public abstract Double[] evaluateMO(T solutionRepresentation) throws Exception;

	public void notifyEvaluationFunctionListeners(String id, String message, ISolution<T> solution) {

		EvaluationFunctionEvent<T> evaluationFunctionEvent = new EvaluationFunctionEvent<T>(this, id, message,
				solution);

		for (IEvaluationFunctionListener<T> listenerObject : listeners)
			listenerObject.processEvaluationFunctionEvent(evaluationFunctionEvent);
	}

	public void notifyEvaluationFunctionListeners(String id, String message, ISolutionSet<T> solutionSet) {

		EvaluationFunctionEvent<T> evaluationFunctionEvent = new EvaluationFunctionEvent<T>(this, id, message,
				solutionSet);

		for (IEvaluationFunctionListener<T> listenerObject : listeners)
			listenerObject.processEvaluationFunctionEvent(evaluationFunctionEvent);
	}

	public List<IEvaluationFunctionListener<T>> getEvaluationFunctionListeners() {
		return listeners;
	}

	public void setListeners(List<IEvaluationFunctionListener<T>> listeners) {
		this.listeners = listeners;
	}

	public void addEvaluationFunctionListener(IEvaluationFunctionListener<T> listener) {
		if (listeners == null)
			listeners = new ArrayList<IEvaluationFunctionListener<T>>();

		listeners.add(listener);
	}

	public IAggregationFunction getFitnessAggregation() {
		return fitnessAggregation;
	}

	public void setFitnessAggregation(IAggregationFunction fitnessAggregation) {
		this.fitnessAggregation = fitnessAggregation;
		if (fitnessAggregation != null)
			performFitnessAggregation = true;
	}

	private class ComputeUnit implements Runnable {

		private IEvaluationFunction<T> evaluator;
		private CountDownLatch latch;
		private List<ISolution<T>> solutionSet;

		public ComputeUnit(IEvaluationFunction<T> evaluator) {
			this.evaluator = evaluator;
		}

		public void setList(List<ISolution<T>> sub) {
			this.solutionSet = sub;
		}

		public void setLatch(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			for (int i = 0; i < this.solutionSet.size(); i++) {
				ISolution<T> solution = solutionSet.get(i);
				evaluator.evaluateSingleSolution(solution);
			}
			latch.countDown();
		}

	}

}
