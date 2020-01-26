package jecoli.jUnitsTests;

import java.util.ArrayList;

import jecoli.algorithm.components.operator.reproduction.linear.IntegerAddMutation;
import jecoli.algorithm.components.representation.integer.IntegerRepresentationFactory;
import jecoli.algorithm.components.representation.linear.ILinearRepresentation;
import jecoli.algorithm.components.representation.linear.ILinearRepresentationFactory;
import jecoli.algorithm.components.solution.ISolution;



public class IntegerAddMutationTest extends AbstractReproductionOperatorTest<ILinearRepresentation<Integer>, ILinearRepresentationFactory<Integer>>{

	protected Integer upperBound;
	protected Integer lowerBound;
	
	public IntegerAddMutationTest()
	{
		this.testName = "IntegerAddMutation";
		
		this.expectedSolutions = new ArrayList<ISolution<ILinearRepresentation<Integer>>>();
		this.initialPopulation = new ArrayList<ISolution<ILinearRepresentation<Integer>>>();
		
	}

	

	@Override
	protected void specificOperatorTests() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setTestValues() {
		this.solutionFactory = new IntegerRepresentationFactory(this.size, this.upperBound,this.lowerBound);
		this.operator = new IntegerAddMutation(size);
	}

	public Integer getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(Integer upperBound) {
		this.upperBound = upperBound;
	}

	public Integer getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(Integer lowerBound) {
		this.lowerBound = lowerBound;
	}

}
