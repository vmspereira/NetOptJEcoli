//package jecoli.algorithm.components.representation.hybridIntegerSet;
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
//public class HybridIntegerSetRepresentation implements IRepresentation,IComparableRepresentation<HybridIntegerSetRepresentation>, Serializable{
//
//	
//	private static final long serialVersionUID = 3633349252621565059L;
//
//	/** List of Integers **/
//	protected List<Integer> integerValues;
//	
//	/** Integer Set List **/
//	protected TreeSet<Integer> setValues;
//	
//	
//	/**
//	 *  Instantiates a new HybridRealSetRepresentation
//	 */
//	public HybridIntegerSetRepresentation()
//	{
//		this(new ArrayList<Integer>(),new TreeSet<Integer>());
//	}
//	
//	/**
//	 * Instantiates a new HybridRealSetRepresentation
//	 * 
//	 * @param representation
//	 */
//	public HybridIntegerSetRepresentation(HybridIntegerSetRepresentation representation)
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
//	public HybridIntegerSetRepresentation(List<Integer> realvalues,TreeSet<Integer> setvalues)
//	{
//		this.integerValues = realvalues;
//		this.setValues = setvalues;
//	}
//	
//	
//	
//	
//	public void removeElementInPosition(int index)
//	{
//		integerValues.remove(index);
//		setValues.remove(getSetValueAtIndex(index));
//	}
//	
//	public void removeElement(int setValue)
//	{
//		int position = getSetValuePosition(setValue);
//		
//		integerValues.remove(position);
//		setValues.remove(setValue);
//	}
//	
//	public void removeElements(TreeSet<Integer> setValues)
//	{
//		for(Integer value : setValues)
//			removeElement(value);
//	}
//	
//	public void addElement(int setValue,Integer integerValue)
//	{
//		if(!containsSetValue(setValue))
//		{
//			setValues.add(setValue);
//			int position = getSetValuePosition(setValue);
//			integerValues.add(position, integerValue);
//		}
//	}
//	
//	public void addElement(HybridIntegerSetRepresentation representation,int elementIndex)
//	{
//		int setValue = representation.getSetValueAtIndex(elementIndex);
//		int integerValue = representation.getIntegerValue(setValue);
//		
//		addElement(setValue, integerValue);
//	}
//	
//	public void addAllElements(HybridIntegerSetRepresentation representation)
//	{
//		int size = representation.getSize();
//		
//		for(int i=0;i<size;i++)
//			addElement(representation, i);
//	}
//	
//	public HybridIntegerSetRepresentation getElementAtPosition(int position)
//	{
//		HybridIntegerSetRepresentation newRepresentation = new HybridIntegerSetRepresentation();
//		newRepresentation.addElement(this, position);
//		return newRepresentation;
//	}
//	
//	public boolean containsIntegerValue(int value)
//	{
//		int size = getSize();
//		for(int i=0; i<size;i++)
//			if(integerValues.get(i) == value)
//				return true;
//		return false;
//	}
//	
//	public boolean containsSetValue(int value)
//	{
//		return setValues.contains(value);
//	}
//	
//	public Integer getIntegerValueAtIndex(int index)
//	{
//		return integerValues.get(index);
//	}
//	
//	public int getSetValueAtIndex(int index)
//	{
//		int pos =0;
//		
//		for(Integer value : setValues){
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
//		return setValues.size();
//	}
//
//	public List<Integer> getRealvalues() {
//		return integerValues;
//	}
//	
//	public TreeSet<Integer> getSetvalues() {
//		return setValues;
//	}
//
//	public void setSetvalues(TreeSet<Integer> setValues)
//	{
//		setValues = new TreeSet<Integer>();
//		
//		for(Integer value : setValues)
//			setValues.add(value);
//	}
//	
//	
//	public int getSetValuePosition(int setValue)
//	{
//		int pos = 0;
//		for(Integer value : setValues){
//			if(value==setValue)
//				return pos;
//			pos++;
//		}
//		return -1;
//	}
//	
//	public Integer getIntegerValue(Integer setValue)
//	{
//		int position = getSetValuePosition(setValue);
//		return getIntegerValueAtIndex(position);
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
//			stringrepresentation+="["+getSetValueAtIndex(i)+","+getIntegerValueAtIndex(i)+"];";
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
//	public List<Integer> getIntegerValuesAsList()
//	{
//		List<Integer> list = new ArrayList<Integer>();
//		
//		int size = getSize();
//		for(int i=0;i<size;i++)
//			list.add(getIntegerValueAtIndex(i));
//		
//		return list;
//	}
//
//	//TODO: IMPLEMENT THIS!!
//	@Override
//	public boolean equals(HybridIntegerSetRepresentation rep) {
//		
//		return false;
//	}
//	
//}
