package jecoli.algorithm.components.algorithm;

import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.IConfiguration;
import jecoli.algorithm.components.representation.IRepresentation;

public abstract class AbstractAlgorithmResultWriter<T extends IRepresentation,S extends IConfiguration<T>> implements IAlgorithmResultWriter<T> {
	protected S configuration;
	
	public AbstractAlgorithmResultWriter(S configuration){
		this.configuration = configuration;
	}
}
