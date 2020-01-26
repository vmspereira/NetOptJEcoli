//package jecoli.algorithm.components.representation.hybridRealSet;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TreeSet;
//
//import jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
//import jecoli.algorithm.components.representation.IComparableRepresentation;
//import jecoli.algorithm.components.representation.IRepresentation;
//
//public class HybridRealSetRepresentation implements IRepresentation, IComparableRepresentation<HybridRealSetRepresentation>,Serializable{
//
//	private static final long serialVersionUID = -1084960127822745736L;
//	
//	/** Real values */
//	protected List<Double> realvalues;
//	
//	/** Set values */
//	protected TreeSet<Integer> setvalues;
//	
//	
//	/**
//	 *  Instantiates a new HybridRealSetRepresentation
//	 */
//	public HybridRealSetRepresentation()
//	{
//		this(new ArrayList<Double>(),new TreeSet<Integer>());
//	}
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentation
//	 * 
//	 * @param representation
//	 */
//	public HybridRealSetRepresentation(HybridRealSetRepresentation representation)
//	{
//		this(representation.getRealvalues(),representation.getSetvalues());
//	}
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentation
//	 * 
//	 * @param realvalues
//	 * @param setvalues
//	 */
//	public HybridRealSetRepresentation(List<Double> realvalues,TreeSet<Integer> setvalues)
//	{
//		this.realvalues = realvalues;
//		this.setvalues = setvalues;
//	}
//	
//	
//	
//	
//	public void removeElementInPosition(int index)
//	{
//		realvalues.remove(index);
//		setvalues.remove(getSetValueAtIndex(index));
//	}
//	
//	public void removeElement(int setValue)
//	{
//		int position = getSetValuePosition(setValue);
//		
//		realvalues.remove(position);
//		setvalues.remove(setValue);
//	}
//	
//	public void removeElements(TreeSet<Integer> setValues)
//	{
//		for(Integer value : setValues)
//			removeElement(value);
//	}
//	
//	public void addElement(int setValue,double realValue)
//	{
//		if(!containsSetValue(setValue))
//		{
//			setvalues.add(setValue);
//			int position = getSetValuePosition(setValue);
//			realvalues.add(position, realValue);
//		}
//	}
//	
//	public void addElement(HybridRealSetRepresentation representation,int elementIndex)
//	{
//		int setValue = representation.getSetValueAtIndex(elementIndex);
//		double realValue = representation.getRealValue(setValue);
//		
//		addElement(setValue, realValue);
//	}
//	
//	public void addAllElements(HybridRealSetRepresentation representation)
//	{
//		int size = representation.getSize();
//		
//		for(int i=0;i<size;i++)
//			addElement(representation, i);
//	}
//	
//	public HybridRealSetRepresentation getElementAtPosition(int position)
//	{
//		HybridRealSetRepresentation newRepresentation = new HybridRealSetRepresentation();
//		newRepresentation.addElement(this, position);
//		return newRepresentation;
//	}
//	
//	public boolean containsRealValue(double value)
//	{
//		int size = getSize();
//		for(int i=0; i<size;i++)
//			if(realvalues.get(i) == value)
//				return true;
//		return false;
//	}
//	
//	public boolean containsSetValue(int value)
//	{
//		return setvalues.contains(value);
//	}
//	
//	public Double getRealValueAtIndex(int index)
//	{
//		return realvalues.get(index);
//	}
//	
//	public int getSetValueAtIndex(int index)
//	{
//		int pos =0;
//		
//		for(Integer value:setvalues){
//			if(pos==index)
//				return value;
//			pos++;
//		}
//		
//		return -1;
//	}
//	
//	public int getSize()
//	{
//		return setvalues.size();
//	}
//
//	public List<Double> getRealvalues() {
//		return realvalues;
//	}
//
//	public void setRealvalues(List<Double> realvalues) {
//		this.realvalues = realvalues;
//	}
//
//	public TreeSet<Integer> getSetvalues() {
//		return setvalues;
//	}
//
//	public void setSetvalues(TreeSet<Integer> setValues)
//	{
//		setValues = new TreeSet<Integer>();
//		
//		for(Integer value : setValues)
//			setvalues.add(value);
//	}
//	
//	
//	public int getSetValuePosition(int setValue)
//	{
//		int pos = 0;
//		for(Integer value : setvalues){
//			if(value==setValue)
//				return pos;
//			pos++;
//		}
//		return -1;
//	}
//	
//	public double getRealValue(Integer setValue)
//	{
//		int position = getSetValuePosition(setValue);
//		return getRealValueAtIndex(position);
//	}
//	
//	public int getRandomSetValue(IRandomNumberGenerator randomGenerator)
//	{
//		int index = (int) (randomGenerator.nextDouble()*(getSize() - 1));
//		return getSetValueAtIndex(index);
//	}
//	
//	@Override
//	public String stringRepresentation() {
//		String stringrepresentation = "";
//		
//		int length = getSize();
//		
//		for(int i=0;i<length;i++)
//			stringrepresentation+="["+getSetValueAtIndex(i)+","+getRealValueAtIndex(i)+"];";
//		
//		
//		return stringrepresentation;
//	}
//	
//	public TreeSet<Integer> getSetValuesAsTreeSet()
//	{
//		TreeSet<Integer> list = new TreeSet<Integer>();
//		
//		int size = getSize();
//		for(int i=0;i<size;i++)
//			list.add(getSetValueAtIndex(i));
//			
//		return list;
//	}
//	
//	public List<Double> getRealValuesAsList()
//	{
//		List<Double> list = new ArrayList<Double>();
//		
//		int size = getSize();
//		for(int i=0;i<size;i++)
//			list.add(getRealValueAtIndex(i));
//		
//		return list;
//	}
//
//	
//	//TODO: IMPLEMENT THIS!!!
//	@Override
//	public boolean equals(HybridRealSetRepresentation rep) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
//	
//}
//
