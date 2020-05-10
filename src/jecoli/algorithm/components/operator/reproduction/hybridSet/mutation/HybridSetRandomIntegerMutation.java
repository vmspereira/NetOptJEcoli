package jecoli.algorithm.components.operator.reproduction.hybridSet.mutation;

import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentation;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentationFactory;

public class HybridSetRandomIntegerMutation<G,H> extends AbstractHybridSetMutationOperator<G,H>{

	
	private static final long serialVersionUID = -4102519863675711234L;

	/** Number of genes to change */
	protected int numberGenesToChange;
	
	public HybridSetRandomIntegerMutation()
	{
		numberGenesToChange = 1;
	}
	
	public HybridSetRandomIntegerMutation(int numberOfGenesToChange)
	{
		numberGenesToChange = numberOfGenesToChange;
	}
	
	@Override
	public void mutateGenome(IHybridSetRepresentation<G,H> childGenome, IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomNumberGenerator) 
	{
		int genesToChange = 1;
		if(this.numberGenesToChange > 1) genesToChange = this.numberGenesToChange;
		
		int N = (int)(randomNumberGenerator.nextDouble()*genesToChange+1);
		 
	 	for(int k=0; k<N; k++)
	 	{
	 		changeElement(childGenome,solutionFactory,randomNumberGenerator);
	 	}
	}
	
	protected void changeElement (IHybridSetRepresentation<G,H> childGenome, IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomNumberGenerator)
	{
		G element = childGenome.getRandomElement(randomNumberGenerator);
		
		childGenome.removeElement(element);
		
		childGenome.addElement(element,solutionFactory.generateListValue());
	}

	@Override
	public IReproductionOperator<IHybridSetRepresentation<G,H>, IHybridSetRepresentationFactory<G,H>> deepCopy() throws Exception {
		return new HybridSetRandomIntegerMutation<G,H>(numberGenesToChange);
	}
}
