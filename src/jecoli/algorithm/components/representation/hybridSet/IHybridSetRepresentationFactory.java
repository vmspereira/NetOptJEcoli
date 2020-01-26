/**
 * 
 */
package jecoli.algorithm.components.representation.hybridSet;

import java.util.Set;

import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.solution.ISolution;
import jecoli.algorithm.components.solution.ISolutionFactory;

/**
 * @author pmaia
 *
 * Jan 23, 2013
 */
public interface IHybridSetRepresentationFactory<G,H> extends ISolutionFactory<IHybridSetRepresentation<G,H>> {
	
	ISolution<IHybridSetRepresentation<G,H>> generateSolution(int size, IRandomNumberGenerator randomGenerator);
	
	int getMaxSetSize();
	int getMinSetSize();
	
	G generateSetValue();
	Set<G> generateSetValues(int numValuesToGenerate);
	
	H generateListValue();
		
	G increaseSetValue(G setValue);
	G decreaseSetValue(G setValue);
	
	H increaseListValue(H listValue);
	H decreaseListValue(H listValue);

	G getMaxSetValue();
	void setMaxSetValue(G maxSetValue);

	H getMaxListValue();
	void setMaxListValue(H maxListValue);

	H getMinListValue();
	void setMinListValue(H minListValue);
	
	int compareSetValues(G value1, G value2);
	int compareListValues(H value1, H value2);

	IHybridSetRepresentationFactory<G,H> deepCopy();

}
