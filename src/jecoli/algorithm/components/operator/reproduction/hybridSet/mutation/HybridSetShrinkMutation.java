package jecoli.algorithm.components.operator.reproduction.hybridSet.mutation;

import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentation;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentationFactory;

public class HybridSetShrinkMutation<G,H> extends AbstractHybridSetMutationOperator<G,H>{

	
	private static final long serialVersionUID = -6331533494232077137L;
	
	/** Number of genes to remove */
	protected int numberGenesToRemove;
	
	public HybridSetShrinkMutation()
	{
		numberGenesToRemove = 1;
	}
	
	public HybridSetShrinkMutation(int numberOfGenesToRemove)
	{
		numberGenesToRemove = numberOfGenesToRemove;
	}

	@Override
	public void mutateGenome(IHybridSetRepresentation<G,H> childGenome,IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomNumberGenerator) 
	{
		int numberOfGenes = childGenome.getNumberOfElements();
		int genesToRemove = 1;
	 	if(this.numberGenesToRemove > 1) genesToRemove = this.numberGenesToRemove;
	 	
	 	int N = (int) (randomNumberGenerator.nextDouble()*genesToRemove+1);
	 	int minSize = solutionFactory.getMinSetSize();
 
	 	genesToRemove = Math.min(N, numberOfGenes-1);
	 	
	 	for(int k=0; k<genesToRemove; k++)
 		{
 			if (childGenome.getNumberOfElements() > minSize)
 			{
 				G element = childGenome.getRandomElement(randomNumberGenerator);
 				childGenome.removeElement(element);
 			}
 		}
	}

	@Override
	public IReproductionOperator<IHybridSetRepresentation<G,H>, IHybridSetRepresentationFactory<G,H>> deepCopy() throws Exception {
		return new HybridSetShrinkMutation<G,H>(numberGenesToRemove);
	}
}
