package jecoli.algorithm.components.operator.reproduction.hybridSet.mutation;

import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentation;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentationFactory;

public class HybridSetGrowthMutation<G,H> extends AbstractHybridSetMutationOperator<G,H>{
	
	private static final long serialVersionUID = 1841479786610557527L;

	/** Number of tries */
	private static int MAXTRIES = 20;
	
	/** Number of genes to add */ 
	protected int numberGenesToAdd;
	
	public HybridSetGrowthMutation()
	{
		numberGenesToAdd = 1;
	}
	
	public HybridSetGrowthMutation(int numberOfGenesToAdd)
	{
		numberGenesToAdd = numberOfGenesToAdd;
	}
	
	@Override
	public void mutateGenome(IHybridSetRepresentation<G,H> childGenome,IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomNumberGenerator) {
		int genesToAdd = 1;
		if(this.numberGenesToAdd>1) genesToAdd = this.numberGenesToAdd;
		
		int N = (int) (randomNumberGenerator.nextDouble()*genesToAdd+1);
		
		for(int i=0;i<N;i++)
			addNewElement(childGenome,solutionFactory,randomNumberGenerator);
	}

	protected void addNewElement(IHybridSetRepresentation<G,H> childGenome,IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomGenerator)
	{
		int maxSize = solutionFactory.getMaxSetSize();
		G newElementSetValue;
		
		if(childGenome.getNumberOfElements()<maxSize)
		{
			int tries = 0;
			do{
				newElementSetValue = solutionFactory.generateSetValue();
				tries++;
			} while(childGenome.containsElement(newElementSetValue) && tries<MAXTRIES);
			if(tries<MAXTRIES) childGenome.addElement(newElementSetValue, solutionFactory.generateListValue());
		}
	}
	
	@Override
	public IReproductionOperator<IHybridSetRepresentation<G,H>, IHybridSetRepresentationFactory<G,H>> deepCopy() throws Exception {
		return new HybridSetGrowthMutation<G,H>(numberGenesToAdd);
	}
}
