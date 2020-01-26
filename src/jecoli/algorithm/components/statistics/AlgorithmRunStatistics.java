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
package jecoli.algorithm.components.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import jecoli.algorithm.components.algorithm.AlgorithmState;
import jecoli.algorithm.components.algorithm.IAlgorithm;
import jecoli.algorithm.components.algorithm.IAlgorithmStatistics;
import jecoli.algorithm.components.configuration.IConfiguration;
import jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionContainer;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.solution.SolutionContainer;
import jecoli.algorithm.components.solution.SolutionSet;

// TODO: Auto-generated Javadoc
/**
 * The Class AlgorithmRunStatistics.
 */
public class AlgorithmRunStatistics<T extends IRepresentation> implements IAlgorithmStatistics<T>, Serializable {

	private static final long serialVersionUID = 1L;

	/** The statistics list. */
	protected List<IAlgorithmIterationStatisticCell<T>> statisticsList;

	/** The solution container. */
	protected ISolutionContainer<T> solutionContainer;

	/** The total number of function evaluations. */
	protected int totalNumberOfFunctionEvaluations;

	/** The total execution time. */
	protected long totalExecutionTime;

	protected ArrayList<Writer> writers;


	
	
	
	/**
	 * Instantiates a new algorithm run statistics.
	 */
	public AlgorithmRunStatistics() {
		
		statisticsList = new ArrayList<IAlgorithmIterationStatisticCell<T>>();
		solutionContainer = new SolutionContainer<T>(1);
		writers=new ArrayList<Writer>();
		PrintWriter writer = new PrintWriter(System.out);
		writers.add(writer);
	}
	
	
	/**
	 * Instantiates a new algorithm run statistics.
	 * 
	 * @param numberOfSolutionsToKeep
	 *            the number of solutions to keep
	 */
	public AlgorithmRunStatistics(int numberOfSolutionsToKeep) {
		statisticsList = new ArrayList<IAlgorithmIterationStatisticCell<T>>();
		solutionContainer = new SolutionContainer<T>(numberOfSolutionsToKeep);
		writers=new ArrayList<Writer>();
		PrintWriter out = new PrintWriter(System.out);
		out.write("It;NFE;Time;MAX;MIN;MEAN;STD\n");
		writers.add(out);
	}

	
	
	protected void finalize() throws Throwable{
		for(Writer out: writers){
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.finalize();
	}
	
	public void addWriter(Writer writer){
		this.writers.add(writer);
	}
	

	public void saveToFile(){
		
		File statisticsFile = new File("./statistics/algorithm.Time." + System.currentTimeMillis());
		try {
			FileWriter fstream = new FileWriter(statisticsFile);
			BufferedWriter out = new BufferedWriter(fstream);
			writers.add(out);
			out.write("It;NFE;Time;MAX;MIN;MEAN;STD\n");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int getNumberOfIterations() {
		return statisticsList.size() - 1;
	}

	@Override
	public IAlgorithmIterationStatisticCell<T> getAlgorithmIterationStatisticCell(int i) {
		return statisticsList.get(i);
	}

	@Override
	public void calculateStatistics(AlgorithmState<T> algorithmState) {
		ISolutionSet<T> solutionSet = algorithmState.getSolutionSet();
		int currentIteration = algorithmState.getCurrentIteration();
		int numberOfFunctionEvaluations = algorithmState.getCurrentIterationNumberOfFunctionEvaluations();
		long executionTime = algorithmState.getLastIterationTime();
		IAlgorithm<T> algorithm = algorithmState.getAlgorithm();
		IConfiguration<T> configuration = algorithm.getConfiguration();
		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		StatisticsConfiguration statisticsConfiguration = configuration.getStatisticConfiguration();
		int statisticsPrintInterval = statisticsConfiguration.getScreenIterationInterval();

		ObjectiveStatisticCell scalarFitnessStatisticCell = null;

		// ????????
		List<ObjectiveStatisticCell> objectiveStatistiCellList = StatisticUtils
				.calculateObjectiveStatisticCell(solutionSet, currentIteration);
		int numObjectives = configuration.getNumberOfObjectives();
		// List<ObjectiveStatisticCell> objectiveStatistiCellList = new
		// ArrayList<ObjectiveStatisticCell>();

		if (numObjectives == 1)
			scalarFitnessStatisticCell = StatisticUtils.processScalarFitness(solutionSet);
		else
			objectiveStatistiCellList = StatisticUtils.calculateObjectiveStatisticCell(solutionSet, currentIteration);

		ISolutionSet<T> bestSolutionSet = calculateBestSolutionSet(algorithmState);

		IAlgorithmIterationStatisticCell<T> cell = new AlgorithmIterationStatiscticCell<T>(currentIteration,
				numberOfFunctionEvaluations, executionTime, objectiveStatistiCellList, scalarFitnessStatisticCell,
				bestSolutionSet);

		totalExecutionTime += executionTime;
		totalNumberOfFunctionEvaluations += numberOfFunctionEvaluations;

		if (statisticsPrintInterval > 0 && currentIteration % statisticsPrintInterval == 0)
			if (numObjectives == 1)
				printStatistics(currentIteration, executionTime, scalarFitnessStatisticCell, statisticsConfiguration);
			else
				printStatistics(currentIteration, executionTime, objectiveStatistiCellList, statisticsConfiguration);

		boolean isMaximization = evaluationFunction.isMaximization();

		if (numObjectives == 1)
			solutionContainer.addSpecificSolutions(solutionSet, currentIteration, isMaximization);

		statisticsList.add(cell);
	}

	/**
	 * Prints the statistics.SOEA
	 * 
	 * @param currentIteration
	 *            the current iteration
	 * @param executionTime
	 *            the execution time
	 * @param objectiveStatisticCell
	 *            the objective statistic cell
	 * @param statisticsConfiguration
	 *            the statistics configuration
	 */
	protected void printStatistics(int currentIteration, long executionTime,
			ObjectiveStatisticCell objectiveStatisticCell, StatisticsConfiguration statisticsConfiguration) {
		StatisticTypeMask mask = statisticsConfiguration.getScreenStatisticsMask();

		for(Writer out: writers)
		try {
			if (mask.isIteration()) {
				out.write("" + currentIteration);
			}

			if (mask.isNumberOfFunctionEvaluations()) {
				out.write(";" + totalNumberOfFunctionEvaluations);
			}
			if (mask.isTotalExecutionTime()) {
				out.write(";" + totalExecutionTime);
			}
			if (mask.isIterationExecutionTime()) {
				out.write(";" + executionTime);
			}
			if (mask.isMaxFitnessValue()) {
				out.write(";" + objectiveStatisticCell.getMaxValue());
			}
			if (mask.isMinFitnessValue()) {
				out.write(";" + objectiveStatisticCell.getMinValue());
			}
			if (mask.isMeanFitnessValue()) {
				out.write(";" + objectiveStatisticCell.getMean());
			}
			if (mask.isStdDevValue()) {
				out.write(";" + objectiveStatisticCell.getStdDev() + "\n");
			}
			out.flush();
			//out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MOEA
	 * 
	 * @param currentIteration
	 * @param executionTime
	 * @param objectiveStatisticCellList
	 * @param statisticsConfiguration
	 */
	protected void printStatistics(int currentIteration, long executionTime,
			List<ObjectiveStatisticCell> objectiveStatisticCellList, StatisticsConfiguration statisticsConfiguration) {
		StatisticTypeMask mask = statisticsConfiguration.getScreenStatisticsMask();
		
		
		for(Writer out: writers)
		try {
			if (mask.isIteration()) {
				out.write("" + currentIteration);
			}

			if (mask.isNumberOfFunctionEvaluations()) {
				out.write(";" + totalNumberOfFunctionEvaluations);
			}
			if (mask.isTotalExecutionTime()) {
				out.write(";" + totalExecutionTime);
			}
			if (mask.isIterationExecutionTime()) {
				out.write(";" + executionTime);
			}
			for (int i = 0; i < objectiveStatisticCellList.size(); i++) {

				ObjectiveStatisticCell cell = objectiveStatisticCellList.get(i);
				out.write(";" + i);

				if (mask.isMaxFitnessValue()) {
					out.write(";" + cell.getMaxValue());
				}
				if (mask.isMinFitnessValue()) {
					out.write(";" + cell.getMinValue());
				}
				if (mask.isMeanFitnessValue()) {
					out.write(";" + cell.getMean());
				}
				if (mask.isStdDevValue()) {
					out.write(";" + cell.getStdDev());
				}
			}
			out.write("\n");
			out.flush();
			//out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Calculate best solution set.
	 * 
	 * @param algorithmState
	 *            the algorithm state
	 * 
	 * @return the i solution set
	 */
	protected ISolutionSet<T> calculateBestSolutionSet(AlgorithmState<T> algorithmState) {
		ISolutionSet<T> solutionSet = algorithmState.getSolutionSet();
		IAlgorithm<T> algorithm = algorithmState.getAlgorithm();
		IConfiguration<T> configuration = algorithm.getConfiguration();
		IEvaluationFunction<T> evaluationFunction = configuration.getEvaluationFunction();
		boolean isMaximization = evaluationFunction.isMaximization();
		StatisticsConfiguration statisticsConfiguration = configuration.getStatisticConfiguration();
		int numberOfBestSolutionsToKeepPerIteration = statisticsConfiguration
				.getNumberOfBestSolutionsToKeepPerIteration();

		if (solutionSet.getNumberOfObjectives() > 1)// TODO ver se não faria
													// sentido extender no caso
													// de multiObjectivo
			return solutionSet;

		if (isMaximization) {
			List<ISolution<T>> solutionList = solutionSet
					.getHighestValuedSolutions(numberOfBestSolutionsToKeepPerIteration);
			return new SolutionSet<T>(solutionList);
		}

		List<ISolution<T>> solutionList = solutionSet.getLowestValuedSolutions(numberOfBestSolutionsToKeepPerIteration);
		return new SolutionSet<T>(solutionList);
	}

	public int getTotalNumberOfFunctionEvaluations() {
		return totalNumberOfFunctionEvaluations;
	}

	public long getTotalExecutionTime() {
		return totalExecutionTime;
	}

	public ISolutionContainer<T> getSolutionContainer() {
		return solutionContainer;
	}

	public double getRunMaxScalarFitnessValue() {
		double maxValue = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell scalarFitnessStatisticCell = iterationStatistics.getScalarFitnessCell();
			double maxFitnessValue = scalarFitnessStatisticCell.getMaxValue();
			maxValue = Math.max(maxValue, maxFitnessValue);
		}

		return maxValue;
	}

	public double getRunMinScalarFitnessValue() {
		double minValue = Double.POSITIVE_INFINITY;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell scalarFitnessStatisticCell = iterationStatistics.getScalarFitnessCell();
			double minFitnessValue = scalarFitnessStatisticCell.getMinValue();
			minValue = Math.min(minValue, minFitnessValue);
		}

		return minValue;
	}

	public double getRunMeanScalarFitnessValue() {
		double meanValue = 0;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell scalarFitnessStatisticCell = iterationStatistics.getScalarFitnessCell();
			double fitnessValue = scalarFitnessStatisticCell.getMean();
			meanValue += fitnessValue;
		}

		return meanValue / statisticsList.size();
	}

	@Override
	public int getNumberOfObjectives() {
		IAlgorithmIterationStatisticCell<T> cell = statisticsList.get(0);
		return cell.getNumberOfObjectives();
	}

	@Override
	public double getRunObjectiveMaxFitnessValue(int objectivePosition) {
		double maxValue = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell objectiveStatisticCell = iterationStatistics
					.getObjectiveStatisticCell(objectivePosition);
			double maxFitnessValue = objectiveStatisticCell.getMaxValue();
			maxValue = Math.max(maxValue, maxFitnessValue);
		}

		return maxValue;
	}

	@Override
	public double getRunObjectiveMeanFitnessValue(int objectivePosition) {
		double meanValue = 0;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell objectiveStatisticCell = iterationStatistics
					.getObjectiveStatisticCell(objectivePosition);
			double fitnessValue = objectiveStatisticCell.getMean();
			meanValue += fitnessValue;
		}

		return meanValue / statisticsList.size();
	}

	@Override
	public double getRunObjectiveMinFitnessValue(int objectivePosition) {
		double minValue = Double.POSITIVE_INFINITY;

		for (int i = 0; i < statisticsList.size(); i++) {
			IAlgorithmIterationStatisticCell<T> iterationStatistics = statisticsList.get(i);
			ObjectiveStatisticCell objectiveStatisticCell = iterationStatistics
					.getObjectiveStatisticCell(objectivePosition);
			double minFitnessValue = objectiveStatisticCell.getMinValue();
			minValue = Math.min(minValue, minFitnessValue);
		}

		return minValue;
	}

	@Override
	public void setSolutionContainer(ISolutionContainer<T> container) {
		this.solutionContainer = container;
	}

}