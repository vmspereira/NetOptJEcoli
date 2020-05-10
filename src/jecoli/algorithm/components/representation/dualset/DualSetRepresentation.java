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
package jecoli.algorithm.components.representation.dualset;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.IComparableRepresentation;
import jecoli.algorithm.components.representation.IRepresentation;


// TODO: Auto-generated Javadoc
/**
 * The Class SetRepresentation.
 */
public class DualSetRepresentation implements IRepresentation,IComparableRepresentation<DualSetRepresentation>, Serializable {
	
	private static final long serialVersionUID = 1L;

	/** The genome. */
	protected TreeSet<Integer> genomeKO; // genome of Knockouts
	protected TreeSet<Integer> genomeKI; // genome of Knockins
	
	/**
	 * Instantiates a new sets the representation.
	 * 
	 * @param genome the genome
	 */
	public DualSetRepresentation (TreeSet<Integer> genomeKnockouts, TreeSet<Integer> genomeAddReactions){
		this.genomeKO = genomeKnockouts;
		this.genomeKI = genomeAddReactions;
	}
	
    /**
     * Gets the number of elements.
     * 
     * @return the number of elements
     */
    public int getNumberOfElements() {
        return genomeKO.size() + genomeKI.size();
    }
    
    public int getNumberOfElements(boolean isKnockout) {
    	if (isKnockout)
    		return genomeKO.size();
        return genomeKI.size();
    }
    
    public Integer getElementAtIndex(int index, boolean isKnockout) {
    	Iterator<Integer> it = isKnockout?genomeKO.iterator():genomeKI.iterator();
    	Integer element = null;
    	for (int i = 0; i <= index; i++)
    		element = it.next();

    	return element;
    }

    /**
     * Gets the random element.
     * 
     * @return the random element
     */
    public Integer getRandomElement(IRandomNumberGenerator randomGenerator, boolean isKnockout) {
    	int size = isKnockout?genomeKO.size():genomeKI.size();
    	int index = (int) (randomGenerator.nextDouble()*( size - 1));
    	return getElementAtIndex(index, isKnockout);
    }
    
    
    /**
     * Adds the element.
     * 
     * @param element the element
     */
    public void addElement(Integer element, boolean isKnockout) {
    	if(isKnockout)
    		genomeKO.add(element);
    	else
    		genomeKI.add(element);
    }
    
    
    /**
     * Removes the element.
     * 
     * @param element the element
     */
    public void removeElement(Integer element, boolean isKnockout){
    	if (isKnockout)
    		genomeKO.remove(element);
    	else
    		genomeKI.remove(element);
    }
    
    /**
     * Contains element.
     * 
     * @param element the element
     * @param isKnochout identify the genome part
     * @return true, if successful
     */
    public boolean containsElement(Integer element, boolean isKnochout){
    	if(isKnochout)
    		return genomeKO.contains(element);
    	return genomeKI.contains(element);
    }

	/**
	 * Gets the genome of Knockouts.
	 * 
	 * @return the genome
	 */
	public TreeSet<Integer> getGenomeKnockout() {
		return genomeKO;
	}
	
	/**
	 * Gets the genome of new reactions.
	 * 
	 * @return the genome
	 */
	public TreeSet<Integer> getGenomeAddReactions() {
		return genomeKI;
	}

	/**
	 * Sets the genome.
	 * 
	 * @param genome the new genome
	 */
	public void setGenome(TreeSet<Integer> genomeKnockouts, TreeSet<Integer> genomeAddReactions) {
		this.genomeKO = genomeKnockouts;
		this.genomeKI = genomeAddReactions;
	}

	@Override
	public String stringRepresentation() {
		String genomeStringRepresentation = "";
		Iterator<Integer> genomeIterator = genomeKO.iterator();
		while(genomeIterator.hasNext()){
			Integer geneValue = genomeIterator.next();
			genomeStringRepresentation += " "+geneValue;
		}
		genomeIterator = genomeKI.iterator();		
		while(genomeIterator.hasNext()){
			Integer geneValue = genomeIterator.next();
			genomeStringRepresentation += " "+geneValue;
		}
		return genomeStringRepresentation;
	}

	//TODO: IMPLEMENT THIS!!
	@Override
	public boolean equals(DualSetRepresentation representation) {
		// TODO Auto-generated method stub
		return false;
	}

}
