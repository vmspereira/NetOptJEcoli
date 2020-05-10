package jecoli.algorithm.multiobjective.archive.components;

public enum InsertionStrategy {
	
	//FILTERS
	ADD_ALL,
	ADD_NO_REPEAT,
	ADD_ALL_ORDERED,
	ADD_NO_REPEAT_ORDERED,
	ADD_SMART,
	
	//EVENTS
	ADD_ON_SINGLE_EVALUATION_FUNCTION_EVENT,
	ADD_ON_SOLUTIONSET_EVALUATION_FUNCTION_EVENT;	

}
