/**
* Copyright 2009,
* CCTC - Computer Science and Technology Center
* IBB-CEB - Institute for Biotechnology and  Bioengineering - Centre of Biological Engineering
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* Created inside the SysBio Research Group <http://sysbio.di.uminho.pt/>
* University of Minho
*/
package jecoli.algorithm.components.algorithm;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jecoli.algorithm.components.IDeepCopy;
import jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import jecoli.algorithm.components.configuration.IConfiguration;
import jecoli.algorithm.components.configuration.InvalidConfigurationException;
import jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import jecoli.algorithm.components.representation.IRepresentation;
import jecoli.algorithm.components.solution.ISolutionSet;
import jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;


/**
 * The Class AbstractAlgorithm.
 */
public abstract class AbstractAlgorithm<T extends IRepresentation,S extends IConfiguration<T>> implements Serializable,IAlgorithm<T>, IDeepCopy {
	
	private static final long serialVersionUID = 345261894136880451L;
	
	/** The Constant ITERATION_INCREMENT_EVENT. */
	public static final String ITERATION_INCREMENT_EVENT = "iterationIncrement";
	public static final String EVALUATION_FUNCTION_INCREMENT = "evaluationFunctionIncrement";
    
    /** The configuration. */
    protected S configuration;
    
    /** The listener list. */
    protected List<IAlgorithmStateListener> listenerList;
    
    protected AlgorithmState<T> algorithmState;
    
    protected boolean cancel = false;

    /**
     * Instantiates a new abstract algorithm.
     * 
     * @param configuration the configuration
     * 
     * @throws InvalidConfigurationException the invalid configuration exception
     * @throws InvalidEvaluationFunctionInputDataException 
     */
    public AbstractAlgorithm(S configuration) throws InvalidConfigurationException, InvalidEvaluationFunctionInputDataException {
    	configuration.verifyConfiguration();
        this.configuration = configuration;
        listenerList = new ArrayList<IAlgorithmStateListener>();
        algorithmState = null;
    }
    
    public AbstractAlgorithm(String filePath,boolean resetListenerList) throws InvalidConfigurationException, Exception {
    	File algorithmStateFileDescriptor =  new File(filePath);
    	FileInputStream fileInputStream = new FileInputStream(algorithmStateFileDescriptor);
    	ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    	algorithmState = (AlgorithmState<T>) objectInputStream.readObject();
    	configuration = (S) objectInputStream.readObject();
    	configuration.verifyConfiguration();
    	if(!resetListenerList)
    		listenerList = (List<IAlgorithmStateListener>) objectInputStream.readObject();
    	else
    		listenerList = new ArrayList<IAlgorithmStateListener>();
    	objectInputStream.close();
    }
    
    protected void executeAlgorithmMainCycle() throws Exception{
    	ITerminationCriteria terminationCriteria = configuration.getTerminationCriteria();
        int saveStateIterationInterval = configuration.getSaveAlgorithmIterationInterval();
        ISolutionSet<T> solutionSet = algorithmState.getSolutionSet();

        assert solutionSet != null;	   
        
    	
        while (!terminationCriteria.verifyAlgorithmTermination(this, algorithmState) && !cancel) {
            ISolutionSet<T> newSolutionSet = iteration(algorithmState, solutionSet);
            updateState(algorithmState, newSolutionSet);
            solutionSet = newSolutionSet;
            int currentNumberOfIterations = algorithmState.getCurrentIteration();
           
            if((saveStateIterationInterval > 0)&&(currentNumberOfIterations % saveStateIterationInterval == 0))
            	saveCurrentState();
        }    
    }
    
    public IAlgorithmResult<T> run() throws Exception, InvalidConfigurationException {
    	configuration.verifyConfiguration();
    	
    	if(algorithmState == null){
    		algorithmState = new AlgorithmState<T>(this);
        	runInitialization();
    	} 
    	
    	
        executeAlgorithmMainCycle();
        IAlgorithmResult<T> algorithmResult = algorithmState.getAlgorithmResult();
        processResult(algorithmResult);
        return algorithmResult;
    }
    
    protected void processResult(IAlgorithmResult<T> algorithmResult) throws Exception {
		List<IAlgorithmResultWriter<T>> algorithmResultWriterList = configuration.getAlgorithmResultWriterList();

		if(algorithmResultWriterList!=null && !algorithmResultWriterList.isEmpty()){
			String uid = configuration.getUID();
			for(IAlgorithmResultWriter<T> algorithmResultWriter:algorithmResultWriterList)
				algorithmResultWriter.writeResult(algorithmResult,uid);
		}
	}

	/**
     * Run initialization.
     * 
     * @return the i solution set
     * 
     * @throws Exception the exception
     */
    protected ISolutionSet<T> runInitialization() throws Exception{
    	algorithmState.initializeState();
         ISolutionSet<T> solutionSet = initialize();
         algorithmState.setSolutionSet(solutionSet);
         algorithmState.updateState(solutionSet);
		return solutionSet;
    }
    
    public void saveCurrentState() throws IOException{
    	String UID = configuration.getUID();
    	String baseDirectory = configuration.getProblemBaseDirectory();
    	String saveStateFileDirectory = baseDirectory+"/"+configuration.getSaveAlgorithmStateDirectoyPath();
    	String saveStateFileName = configuration.getSaveAlgorithmStateFile();
    	File dirPathFileDescriptor =  new File(saveStateFileDirectory);
    	dirPathFileDescriptor.mkdirs();
    	String statefilePath = saveStateFileDirectory+"/"+saveStateFileName+UID;
    	File stateFile = new File(statefilePath);
    	stateFile.delete();
		FileOutputStream fileOutputStream = new FileOutputStream(stateFile);
		ObjectOutputStream objectOutStream = new ObjectOutputStream(fileOutputStream);
		objectOutStream.writeObject(algorithmState);
		objectOutStream.writeObject(configuration);
		objectOutStream.writeObject(listenerList);//TODO ver se faz sentido
		objectOutStream.close();
	}
    
	/**
	 * Iteration.
	 * @param algorithmState TODO
	 * @param solutionSet the solution set
	 * 
	 * @return the i solution set
	 * 
	 * @throws Exception the exception
	 */
	protected abstract ISolutionSet<T> iteration(AlgorithmState<T> algorithmState, ISolutionSet<T> solutionSet)throws Exception;

	
    public void updateState(AlgorithmState<T> algorithmState,ISolutionSet<T> solutionSet) {
    	algorithmState.updateState(solutionSet);
    }
    
  
    public IConfiguration<T> getConfiguration(){
    	return configuration;
    }
    
  
    public void addAlgorithmStateListener(IAlgorithmStateListener algorithmStateListener){
    	if(!listenerList.contains(algorithmStateListener))
    		listenerList.add(algorithmStateListener);
    }
    
    public void notifyAlgorithmStateListeners(String id,String message){
    	AlgorithmStateEvent algorithmStateEvent = new AlgorithmStateEvent(this,id,message);
    	
    	for(IAlgorithmStateListener listenerObject:listenerList)
    		listenerObject.processAlgorithmStateEvent(algorithmStateEvent);
    } 

    public Collection<?> getSolutionSetView(){
     return Collections.unmodifiableCollection(algorithmState.solutionSet.getListOfSolutions());
    }

	public List<IAlgorithmStateListener> getListenerList() {
		return listenerList;
	}

	public AlgorithmState<T> getAlgorithmState() {
		return algorithmState;
	}

	public void setAlgorithmState(AlgorithmState<T> algorithmState) {
		this.algorithmState = algorithmState;
	}
	
	public synchronized IAlgorithmResult<T> cancel() throws Exception{
		this.cancel=true;
		IAlgorithmResult<T> algorithmResult = algorithmState.getAlgorithmResult();
        processResult(algorithmResult);
        return algorithmResult;
	}
}

