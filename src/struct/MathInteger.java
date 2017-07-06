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
public class MathInteger implements MathObject, Serializable {
	private BigInteger value;
	
	public MathInteger(int in) {
		value = BigInteger.valueOf(in);
	}
	
	/**
	 * Constructor
	 */
	public MathInteger(String intString, int radix) {
		value = new BigInteger(intString, radix);
	}
	
	public MathInteger(String intString) {
		value = new BigInteger(intString, 10);
	}
	
	public MathInteger(byte[] byteArrayIn) {
		value = new BigInteger(byteArrayIn);
	}
	
	public MathInteger(BigInteger bigIntegerIn) {
		value = bigIntegerIn;
	}

	public int intValue() {
		return value.intValue();
	}
	
	public BigInteger bigIntegerValue() {
		return value;
	}
	
	/**

	 */
	public boolean isZero() {
		return equals(CALC.ZERO);
	}
	
	/**
	 */
	public boolean isNegative() {
		return (value.compareTo(CALC.ZERO.bigIntegerValue()) < 0);
	}
	
	/**
	 */
	public boolean isEven() {
		return (value.mod(CALC.TWO.bigIntegerValue()).equals(CALC.ZERO.bigIntegerValue()));
	}
	
	public MathInteger negate() {
		return new MathInteger(value.negate());
	}
	
	public MathInteger add(MathInteger input) {
		return new MathInteger(value.add(input.bigIntegerValue()));
	}
	
	public MathInteger multiply(MathInteger input) {
		return new MathInteger(value.multiply(input.bigIntegerValue()));
	}
	
	public MathInteger divide(MathInteger input) {
		return new MathInteger(value.divide(input.bigIntegerValue()));
	}
	
	/**
	 *
	 */
	public MathInteger power(int n) {
		return new MathInteger(value.pow(n));
	}
	
	public MathInteger mod(MathInteger input) {
		return new MathInteger(value.mod(input.bigIntegerValue()));
	}
	
	/**
	 *
	 */
	public MathInteger root(int n) {
		if (isNegative() && ((n % 2) == 0)) throw new ArithmeticException("Even root of a negative number.");
		
		int temp = n - 1;
		MathInteger Temp = new MathInteger(temp);
		MathInteger N = new MathInteger(n);
		
		MathInteger A = this;
		MathInteger B = this.add(Temp).divide(N);
		
		while (B.compareTo(A) < 0) {
			A = B;
			B = add(Temp.multiply(A.power(temp)).divide(N.multiply(A.power(n))));
		}
		
		return A;
	}
	
	public boolean equals(java.lang.Object obj) {
		if (obj instanceof MathInteger) {
			return value.equals(((MathInteger)obj).bigIntegerValue());
		}
		else if (obj instanceof MathDouble) {
			if (value == null || !((MathDouble)obj).isInteger()) return false;
			else return value.intValue() == (int)((MathDouble)obj).doubleValue();
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
			if (obj instanceof MathInteger) {
				return value.compareTo(((MathInteger)obj).bigIntegerValue());
			}
			else if (obj instanceof MathDouble) {
				if ((double)value.intValue() < ((MathDouble)obj).doubleValue()) {
					return -1;
				}
				else if ((double)value.intValue() > ((MathDouble)obj).doubleValue()) {
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
	public MathObject cloneMathObject() {
		MathInteger clone = new MathInteger(value);

		return clone;
	}
}
