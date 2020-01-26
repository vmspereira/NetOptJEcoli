package jecoli.algorithm.components.operator.reproduction.hybridSet.mutation;

import jecoli.algorithm.components.operator.IReproductionOperator;
import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentation;
import jecoli.algorithm.components.representation.hybridSet.IHybridSetRepresentationFactory;

public class HybridSetGaussianPertubationMutation<G,H> extends AbstractHybridSetMutationOperator<G,H>{

	
	private static final long serialVersionUID = 4244251866835540502L;

	/** Number of genes to change */
	protected int numberGenesToChange;
	
	/** Standard Deviation */
	protected double standardDeviation = 1;
	
	/** Mean */
	protected double mean = 0.0;
	
	
	public HybridSetGaussianPertubationMutation()
	{
		numberGenesToChange = 1;
	}
	
	public HybridSetGaussianPertubationMutation(int numberOfGenesToChange)
	{
		numberGenesToChange = numberOfGenesToChange;
	}
	
	
	@Override
	public void mutateGenome(IHybridSetRepresentation<G,H> childGenome, IHybridSetRepresentationFactory<G,H> solutionFactory, IRandomNumberGenerator randomNumberGenerator) {
		int genesToChange = 1;
		if(this.numberGenesToChange > 1) 
			genesToChange = this.numberGenesToChange;
		
		int N = (int)(randomNumberGenerator.nextDouble()*genesToChange+1);
		 
	 	for(int k=0; k<N; k++)
	 	{
	 		changeElement(childGenome,solutionFactory,randomNumberGenerator);
	 	}
	}
	

	protected void changeElement(IHybridSetRepresentation<G,H> childGenome,IHybridSetRepresentationFactory<G,H> solutionFactory,IRandomNumberGenerator randomNumberGenerator){
		G element = childGenome.getRandomElement(randomNumberGenerator);
		H elementListValue = childGenome.getListValue(element);
		childGenome.removeElement(element);
	
		
		double gaussianPerturbation = randomNumberGenerator.nextGaussian()*standardDeviation + mean;
		
		if(gaussianPerturbation >= 0)
			elementListValue = solutionFactory.increaseListValue(elementListValue);
		else
			elementListValue = solutionFactory.decreaseListValue(elementListValue);
		
		if(solutionFactory.compareListValues(elementListValue, solutionFactory.getMaxListValue())<0)
			elementListValue = solutionFactory.getMinListValue();
		
		if(solutionFactory.compareListValues(elementListValue, solutionFactory.getMaxListValue())>0)
			elementListValue = solutionFactory.getMaxListValue();		
		
		childGenome.addElement(element, elementListValue);
	}
	
	
	@Override
	public IReproductionOperator<IHybridSetRepresentation<G,H>, IHybridSetRepresentationFactory<G,H>> deepCopy() throws Exception {
		return new HybridSetGaussianPertubationMutation<G,H>(numberGenesToChange);
	}
}
