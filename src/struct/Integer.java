package struct;

import core.CALC;
import exception.ArithmeticException;
import exception.UnsupportedException;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Hierarchially encapsulates the java integer. BigInteger is used as the medium
 * because of the lack of size restrictions.
 *  
 *
 */
public class Integer implements MathObject, Serializable {
	private BigInteger value;
	
	public Integer(int in) {
		value = BigInteger.valueOf(in);
	}
	
	/**
	 * Constructor
	 * @param intString input string that represents an integer
	 * @param radix the radix (base) of the integer
	 */
	public Integer(String intString, int radix) {
		value = new BigInteger(intString, radix);
	}
	
	public Integer(String intString) {
		value = new BigInteger(intString, 10);
	}
	
	public Integer(byte[] byteArrayIn) {
		value = new BigInteger(byteArrayIn);
	}
	
	public Integer(BigInteger bigIntegerIn) {
		value = bigIntegerIn;
	}

	public int intValue() {
		return value.intValue();
	}
	
	public BigInteger bigIntegerValue() {
		return value;
	}
	
	/**
	 * 
	 * @return whether this integer is zero
	 */
	public boolean isZero() {
		return equals(CALC.ZERO);
	}
	
	/**
	 * 
	 * @return whether this integer is negative
	 */
	public boolean isNegative() {
		return (value.compareTo(CALC.ZERO.bigIntegerValue()) < 0);
	}
	
	/**
	 * 
	 * @return whether this integer is even
	 */
	public boolean isEven() {
		return (value.mod(CALC.TWO.bigIntegerValue()).equals(CALC.ZERO.bigIntegerValue()));
	}
	
	public Integer negate() {
		return new Integer(value.negate());
	}
	
	public Integer add(Integer input) {
		return new Integer(value.add(input.bigIntegerValue()));
	}
	
	public Integer multiply(Integer input) {
		return new Integer(value.multiply(input.bigIntegerValue()));
	}
	
	public Integer divide(Integer input) {
		return new Integer(value.divide(input.bigIntegerValue()));
	}
	
	/**
	 * 
	 * @param n
	 * @return the nth power of this integer
	 */
	public Integer power(int n) {
		return new Integer(value.pow(n));
	}
	
	public Integer mod(Integer input) {
		return new Integer(value.mod(input.bigIntegerValue()));
	}
	
	/**
	 * 
	 * @param n
	 * @return the nth root of this integer
	 */
	public Integer root(int n) {
		if (isNegative() && ((n % 2) == 0)) throw new ArithmeticException("Even root of a negative number.");
		
		int temp = n - 1;
		Integer Temp = new Integer(temp);
		Integer N = new Integer(n);
		
		Integer A = this;
		Integer B = this.add(Temp).divide(N);
		
		while (B.compareTo(A) < 0) {
			A = B;
			B = add(Temp.multiply(A.power(temp)).divide(N.multiply(A.power(n))));
		}
		
		return A;
	}
	
	public boolean equals(java.lang.Object obj) {
		if (obj instanceof Integer) {
			return value.equals(((Integer)obj).bigIntegerValue());
		}
		else if (obj instanceof Double) {
			if (value == null || !((Double)obj).isInteger()) return false;
			else return value.intValue() == (int)((Double)obj).doubleValue();
		}
		else return false;
	}
	
	@Override
	public boolean isNumber() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Symbol getHeader() {
		return CALC.INTEGER;
	}
	
	@Override
	public MathObject evaluate() {
		return this;
	}
	
	public String toString() {
		return value.toString();
	}

	@Override
	public int compareTo(MathObject obj) {
		if (obj.isNumber()) {
			if (obj instanceof Integer) {
				return value.compareTo(((Integer)obj).bigIntegerValue());
			}
			else if (obj instanceof Double) {
				if ((double)value.intValue() < ((Double)obj).doubleValue()) {
					return -1;
				}
				else if ((double)value.intValue() > ((Double)obj).doubleValue()) {
					return 1;
				}
				else return 0;
			}
			else throw new UnsupportedException(obj.toString());
		}
		else if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}

		else return 0;
	}

	@Override
	public int getHierarchy() {
		return INTEGER;
	}

	@Override
	public int getPrecedence() {
		if (value.compareTo(BigInteger.ZERO) < 0) {
			//return 100;
		}
		return 9999999;
	}

	@Override
	public boolean isSameSolution(MathObject obj) {
		//todo if(functionHeader)
		if(!(obj instanceof Integer)){
			return false;
		}

		Integer int2 = (Integer) obj;
		if(value != int2.value) return false;
		return true;
	}
}
