package jecoli.algorithm.components.operator.container;

import jecoli.algorithm.components.operator.ISelectionOperator;
import jecoli.algorithm.components.representation.IRepresentation;

public class SelectionOperatorContainerCell <T extends IRepresentation> extends AbstractOperatorContainerCell<ISelectionOperator<T>> {

	public SelectionOperatorContainerCell(double probability,ISelectionOperator<T> operator) throws Exception {
		super(probability, operator);
	}

	@Override
	public SelectionOperatorContainerCell<T> deepCopy()	throws Exception {
		ISelectionOperator<T> operatorCopy = operator.deepCopy();
		return new SelectionOperatorContainerCell<T>(probability, operatorCopy);
	}

}
