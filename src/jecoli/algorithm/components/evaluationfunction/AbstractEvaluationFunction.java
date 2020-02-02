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
package jecoli.algorithm.components.evaluationfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jecoli.SystemConf;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionSet;

/**
 * The Class EvaluationFunction.
 */
public abstract class AbstractEvaluationFunction<T extends IRepresentation> implements IEvaluationFunction<T> {

	private static final long serialVersionUID = 7948503042698103889L;

	/** The is maximization. */
	protected boolean isMaximization;
	protected List<IEvaluationFunctionListener<T>> listeners = null;

	protected boolean mp = true;
	/** By default use half of the available processors **/
	protected int n_threads = SystemConf.getPropertyInt("multithread.nmp",
			Runtime.getRuntime().availableProcessors() / 2);
	protected List<ComputeUnit> workers = null;

	/**
	 * Instantiates a new evaluation function.
	 * 
	 * @param isMaximization
	 *            the is maximization
	 */
	public AbstractEvaluationFunction(boolean isMaximization) {
		this.isMaximization = isMaximization;
	}

	@Override
	public void evaluate(ISolutionSet<T> solutionSet) {
		if (this.mp) {
			try {
				evaluateMP(solutionSet);
			} catch (Exception e) {
				for (int i = 0; i < solutionSet.getNumberOfSolutions(); i++) {
					ISolution<T> solution = solutionSet.getSolution(i);
					evaluateSingleSolution(solution);
				}
			}
		} else
			for (int i = 0; i < solutionSet.getNumberOfSolutions(); i++) {
				ISolution<T> solution = solutionSet.getSolution(i);
				evaluateSingleSolution(solution);
			}
		if (listeners != null && !listeners.isEmpty())
			notifyEvaluationFunctionListeners(EvaluationFunctionEvent.SOLUTIONSET_EVALUATION_EVENT,
					String.valueOf(solutionSet.getNumberOfSolutions()), solutionSet);
	}

	@Override
	public void evaluateSingleSolution(ISolution<T> solution) {
		try {
			Double fitnessValue = null;
			fitnessValue = evaluate(solution.getRepresentation());
			solution.setFitnessValue(fitnessValue);

		} catch (Exception e) {
			// e.printStackTrace();
			if (isMaximization)
				solution.setFitnessValue(Double.NEGATIVE_INFINITY);
			else
				solution.setFitnessValue(Double.POSITIVE_INFINITY);
		}

		if (listeners != null && !listeners.isEmpty())
			notifyEvaluationFunctionListeners(EvaluationFunctionEvent.SINGLE_SOLUTION_EVALUATION_EVENT, "", solution);
	}

	private void evaluateMP(ISolutionSet<T> solutionSet) throws Exception {
		if (this.workers == null) {
			workers = new ArrayList<ComputeUnit>();
			for (int i = 0; i < this.n_threads; i++) {
				IEvaluationFunction<T> evaluator = this.deepCopy();
				if (evaluator == null) {
					this.mp = false;
					this.workers = null;
					throw new Exception("DeepCopy Interface needs to be implemented to use multiprocessing");
				}
				workers.add(new ComputeUnit(evaluator));
			}
			System.out.println("Using " + this.n_threads + " threads.");
		}

		ExecutorService exec = Executors.newFixedThreadPool(n_threads);
		CountDownLatch latch = new CountDownLatch(n_threads);

		List<ISolution<T>> list = solutionSet.getListOfSolutions();

		int n = solutionSet.getNumberOfSolutions() / n_threads;
		int t = solutionSet.getNumberOfSolutions() % n_threads;
		int from = 0;
		for (int i = 0; i < n_threads; i++) {
			int to = from + n;
			if (t > 0) {
				to += 1;
				t--;
			}
			List<ISolution<T>> sub = list.subList(from, to);
			workers.get(i).setList(sub);
			from = to;
		}

		for (ComputeUnit r : workers) {
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

	public boolean isMaximization() {
		return isMaximization;
	}

	public int getNumberOfObjectives() {
		return 1;
	}

	/**
	 * Evaluate.
	 * 
	 * @param solutionRepresentation
	 *            the solution representation
	 * 
	 * @return the list< double>
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public abstract double evaluate(T solutionRepresentation) throws Exception;

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
		if (this.listeners == null)
			listeners = new ArrayList<IEvaluationFunctionListener<T>>();

		listeners.add(listener);
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