package jecoli.algorithm.multiobjective.archive.aggregation;

import jecoli.algorithm.multiobjective.archive.components.IArchiveManagementFunction;

public interface IAggregationFunction {
	
	Double aggregate(Double[] values);

}
