/**
 * 
 */
package struct;

/**
 * The root interface for the expression hierarchy data structure. All 
 * expression entities must implement this to be evaluable.
 *  
 *
 */

public interface MathObject extends Comparable<MathObject> {
	/**
	 * Constants that identify a hierarchy (used as part of Comparable)
	 */
	public final static int DOUBLE 		= 0x01;
	public final static int INTEGER 	= 0x02;
	public final static int FRACTION 	= 0x04;
	public final static int SYMBOL	 	= 0x08;
	public final static int VECTOR 		= 0x10;	
	public final static int MATRIX 		= 0x20;	
	public final static int FUNCTION 	= 0x40;

	/**
	 * Evaluate the object if possible. If not, throw general exception
	 * @return evaluation result as another CalcObject
	 * @throws Exception throws an Exception
	 */
	public MathObject evaluate() throws Exception;
	
	/**
	 * Convert the object into a string. Overrides the toString method in java.lang.Object
	 * @return the string
	 */
	public String toString();
	
	@Override
	public boolean equals(java.lang.Object obj);
	
	@Override
	public int compareTo(MathObject obj);
	
	/**
	 * 
	 * @return true if the object represents a numeric. False otherwise.
	 */
	public boolean isNumber();
	
	/**
	 * 
	 * @return the header symbol of this object (the object's "name")
	 */
	public Symbol getHeader();
	
	/**
	 * 
	 * @return the object's hierarchy constant. Used in compareTo(CalcObject obj)
	 * @see Comparable 
	 */
	public int getHierarchy();
	
	/**
	 * 
	 * @return the precedence of the object (used at evaluation time)
	 * The higher the return value, the higher the precedence. There is no
	 * set range on these values. A simple smaller bigger equals check will be used.
	 */
	public int getPrecedence();

	public boolean isSameSolution(MathObject obj);
	
}
