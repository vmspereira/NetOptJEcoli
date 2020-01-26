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
package jecoli.algorithm.components.solution;

import java.io.Serializable;
import java.util.Arrays;

import jecoli.algorithm.components.representation.IRepresentation;



// TODO: Auto-generated Javadoc
/**
 * The Class Solution.
 */
public class Solution<T extends IRepresentation> implements ISolution<T>, Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1822046108083320183L;
	
	/** The number of objectives */
	protected int numberOfObjectives = 0;

	/** The genome. */
    protected T genome;
    
    /** The selection value */
    protected Double selectionValue;
    
    /** The scalar fitness value. */
    protected Double scalarFitnessValue;
    
    /** The fitness values vector. */
    protected Double[] fitnessValuesArray;
    
    /** The location. */
    protected int location;
    
    /** The rank. */
    protected int rank;
    
    /** The crowding distance. */
    protected double crowdingDistance;	

    
    public Solution(T genome) {
        this.genome = genome;
        this.selectionValue = 0.0;
        this.scalarFitnessValue = 0.0;
        this.numberOfObjectives = 1;
    }
    
    public Solution(T genome,int numberObjectives) {
        this.genome = genome;
        this.numberOfObjectives = numberObjectives;
        initializeFitnessValuesArray(numberObjectives);
        selectionValue = 0.0;
        scalarFitnessValue = 0.0;
    }
    
    public Solution(T genome, Double... objectives){
    	this.genome = genome;
    	this.numberOfObjectives = objectives.length;
    	initializeFitnessValuesArray(objectives);
    	selectionValue = 0.0;
    	scalarFitnessValue = 0.0;
    }
    
    /**
	 * Initialize fitness values vector.
	 * 
	 * @param numberObjectives the number objectives
	 */
	protected void initializeFitnessValuesArray(int numberObjectives) {
        fitnessValuesArray = new Double[numberObjectives];
        for(int i=0;i<numberObjectives;i++)
        	fitnessValuesArray[i] = new Double(0.0);
    }
	
	/**
	 * Initialize fitness values vector.
	 * 
	 * @param numberObjectives the number objectives
	 */
	protected void initializeFitnessValuesArray(Double... fitnessValues) {
        fitnessValuesArray = new Double[fitnessValues.length];
        for(int i=0;i<fitnessValues.length;i++)
        	fitnessValuesArray[i] = fitnessValues[i];
    }
	
    public T getGenome() {
        return genome;
    }

    public void setFitnessValue(Double fitnessValue) {
		this.scalarFitnessValue = fitnessValue;
	}

    @Override
    public T getRepresentation() {
        return genome;
    }

    public Double getScalarFitnessValue() {
        return scalarFitnessValue;
    }

	@Override
	public int getNumberOfObjectives() {
		if(numberOfObjectives==0)
			numberOfObjectives = (fitnessValuesArray==null) ? 1 : fitnessValuesArray.length;
			
		return numberOfObjectives;
	}
	

	@Override
	public Double getFitnessValue(int objectiveIndex) {
		
		if(getNumberOfObjectives()==1)
			return this.scalarFitnessValue;
		
		if(fitnessValuesArray==null || objectiveIndex > getNumberOfObjectives()-1)
			throw new IllegalArgumentException(fitnessValuesArray==null? "Null fitness Values Array ":"Incorrect objective index ["+objectiveIndex+"]. Only "+getNumberOfObjectives()+" objectives defined.");
		return fitnessValuesArray[objectiveIndex];
	}

	@Override
	public void setFitnessValues(Double... fitnessValues) {
		for(int i=0;i<fitnessValues.length;i++){
			getFitnessValuesArray()[i]=fitnessValues[i];
		}
	}

	@Override
	public Double[] getFitnessValuesArray() {
		if(this.fitnessValuesArray==null)
			initializeFitnessValuesArray(numberOfObjectives);
		
		return fitnessValuesArray;
	}
	
	@Override
	public Double getSelectionValue(){
		return selectionValue;
	}
	
	@Override
	public void setSelectionValue(Double selectionValue){
		this.selectionValue = selectionValue;
	}

	@Override
	public int getLocation() {
		return location;
	}

	@Override
	public void setLocation(int location) {
		this.location = location;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public double getCrowdingDistance() {
		return crowdingDistance;
	}

	@Override
	public void setCrowdingDistance(double crowdingDistance) {
		this.crowdingDistance = crowdingDistance;
	}
	
	public boolean isMultiobjective(){
		return (this.numberOfObjectives > 1);
	}

	@Override
	public boolean equals(ISolution<T> solution) {
		
		return (
				(this.numberOfObjectives == solution.getNumberOfObjectives()) &&
				(this.location == solution.getLocation()) &&
				(this.crowdingDistance == solution.getCrowdingDistance()) &&
				(this.rank == solution.getRank()) &&
				(this.scalarFitnessValue == solution.getScalarFitnessValue()) &&
				(Arrays.equals(fitnessValuesArray, solution.getFitnessValuesArray())) &&
				(this.genome.equals(solution.getRepresentation()))				
		);		
	}

	@Override
	public boolean equalsRepresentation(ISolution<T> solution) {
		return this.genome.equals(solution.getRepresentation());				
	}

	@Override
	public boolean equalsRepresentationAndFitness(ISolution<T> solution) {
		if(numberOfObjectives!= solution.getNumberOfObjectives())
			return false;
		
		if(isMultiobjective())
			return (Arrays.equals(fitnessValuesArray, solution.getFitnessValuesArray()));
		else
			return this.scalarFitnessValue==solution.getScalarFitnessValue();
	}

}