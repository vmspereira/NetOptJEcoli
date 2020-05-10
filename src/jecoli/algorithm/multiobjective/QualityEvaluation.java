package jecoli.algorithm.multiobjective;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jmetal.qualityIndicator.GeneralizedSpread;
import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.Spread;


/**
 * jMetal quality indicators, managing class.
 * 
 * @author paulo maia, May 26, 2008
 *
 */
public class QualityEvaluation {
	
	private PerformanceIndex pi = PerformanceIndex.GENERATIONAL_DISTANCE;
	
	public QualityEvaluation(){
	}
	
	public QualityEvaluation(PerformanceIndex pi){
		this.pi = pi;
	}
	
	public double evaluate(PerformanceIndex pi,double[][] pareto,double[][] truepareto,int no) throws Exception{
		this.setPi(pi);
		return this.evaluate(pareto, truepareto, no);
	}
	
	public double evaluate(double[][] pareto,double[][] truepareto,int no) throws Exception{
				
		switch(pi){
			case GENERALIZED_SPREAD: {
				GeneralizedSpread qualityIndicator = new GeneralizedSpread();
				return qualityIndicator.generalizedSpread(pareto, truepareto, no);
			}
			case GENERATIONAL_DISTANCE: {
				GenerationalDistance qualityIndicator = new GenerationalDistance();
				return qualityIndicator.generationalDistance(pareto, truepareto, no);
			}
			case HYPERVOLUME: {
				Hypervolume qualityIndicator = new Hypervolume();
				return qualityIndicator.hypervolume(pareto, truepareto, no);
			}
			case INVERTED_GENERATIONAL_DISTANCE: {
				InvertedGenerationalDistance qualityIndicator = new InvertedGenerationalDistance();
				return qualityIndicator.invertedGenerationalDistance(pareto, truepareto, no);				
			}
			case SPREAD: {
				if(no>2) throw new Exception("The SPREAD quality indicator (delta index) can only be used for bi-objective problems.");
				Spread qualityIndicator = new Spread();
				return qualityIndicator.spread(pareto, truepareto, 2);
			}
			default: return 0.0;
			
		}
		
	}

	/**
	 * @return the pi
	 */
	public PerformanceIndex getPi() {
		return pi;
	}

	/**
	 * @param pi the pi to set
	 */
	public void setPi(PerformanceIndex pi) {
		this.pi = pi;
	}
	
	/**
	 * This version uses the default separator - white space
	 * 
	 * @param path - the path of the file to read
	 * @return
	 */
	public double[][] readFrontFromFile(String path){
		return this.readFrontFromFile(path,null);
	}
	
	public double[][] readFrontFromFile(String path,String separator){
		try {
			// Open the file
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			List<double[]> list = new ArrayList<double[]>();
			int numberOfObjectives = 0;
			String aux = br.readLine();
			while (aux != null) {
				StringTokenizer st = (separator==null) ? new StringTokenizer(aux) : new StringTokenizer(aux,separator);
				int i = 0;
				numberOfObjectives = st.countTokens();
				double[] vector = new double[st.countTokens()];
				while (st.hasMoreTokens()) {
					double value = (new Double(st.nextToken())).doubleValue();
					vector[i] = value;
					i++;
				}
				list.add(vector);
				aux = br.readLine();
			}

			br.close();

			double[][] front = new double[list.size()][numberOfObjectives];
			for (int i = 0; i < list.size(); i++) {
				front[i] = list.get(i);
			}
			return front;

		} catch (Exception e) {
			System.out.println("InputFacilities crashed reading for file: "	+ path);
			e.printStackTrace();
		}
		return null;	
	}	

}
