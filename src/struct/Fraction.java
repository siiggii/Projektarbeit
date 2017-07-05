package struct;

import core.CALC;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 *  
 *
 */
public class Fraction implements MathObject, Serializable {

	private BigInteger numerator;
	private BigInteger denominator;
	
	public Fraction(BigInteger nume, BigInteger deno) {
		if (deno.compareTo(CALC.ZERO.bigIntegerValue()) == 0) { //division by zero => throw exception
			throw new ArithmeticException();
		}
		
		//if denominator is negative, make the numerator negative instead.
		//Makes life easier when simplifying.
		if (deno.compareTo(CALC.ZERO.bigIntegerValue()) == -1) {
			nume = nume.negate();
			deno = deno.negate();
		}
		
		BigInteger commonfactor = deno.gcd(nume);
		
		if (commonfactor.compareTo(CALC.ONE.bigIntegerValue()) != 0) {
			nume = nume.divide(commonfactor);
			deno = deno.divide(commonfactor);
		}
		
		numerator = nume;
		denominator = deno;		
	}
	
	public Fraction(Integer a, Integer b) {
		this(a.bigIntegerValue(), b.bigIntegerValue());
	}
	
	public MathObject add(Fraction input) {
		
		BigInteger  nume = (numerator.multiply(input.getDenominator())).add(denominator.multiply(input.getNumerator()));
		BigInteger  deno = denominator.multiply(input.getDenominator());

		if (deno.compareTo(CALC.ZERO.bigIntegerValue()) < 0) {
			deno = deno.negate();
			nume = nume.negate();
		}
		
		BigInteger  commonfactor = nume.gcd(deno);

		return new Fraction(nume.divide(commonfactor), deno.divide(commonfactor));
	}
	
	public MathObject multiply(Integer input) {
		BigInteger nume = numerator.multiply(input.bigIntegerValue());
		BigInteger commonfactor = nume.gcd(denominator);

		return new Fraction(nume.divide(commonfactor), denominator.divide(commonfactor));
	}
	
	public MathObject multiply(Fraction input) {
		BigInteger nume = numerator.multiply(input.getNumerator());
		BigInteger deno = denominator.multiply(input.getDenominator());
		
		BigInteger commonfactor = nume.gcd(deno);
		
		return new Fraction(nume.divide(commonfactor), deno.divide(commonfactor));
	}
	

	public MathObject power(int n) {
		if (n < 0) {
			n *= -1;
			return new Fraction(denominator.pow(n), numerator.pow(n));
		}
		
		return new Fraction(numerator.pow(n), denominator.pow(n));
	}
	
	public BigInteger getNumerator() {
		return numerator;
	}
	
	public BigInteger getDenominator() {
		return denominator;
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
	
	public boolean isNegative() {
		return !((numerator.compareTo(BigInteger.ZERO) < 0 
					&& denominator.compareTo(BigInteger.ZERO) < 0)
				|| (numerator.compareTo(BigInteger.ZERO) > 0 
					&& denominator.compareTo(BigInteger.ZERO) > 0));
	}
	
	public void negate() {
		numerator.negate();
	}

	@Override
	public Symbol getHeader() {
		return CALC.FRACTION;
	}
	
	@Override
	public MathObject evaluate() throws Exception {
		return new Double((new BigDecimal(numerator)).divide(new BigDecimal(denominator), CALC.mathcontext));
	}

	public String toString() {
		StringBuffer returnVal = new StringBuffer();
		if (denominator.compareTo(CALC.ONE.bigIntegerValue()) == 0) {
			returnVal.append(numerator.toString()); //if denominator is 1, then just append numerator
		}
		else {
			returnVal.append(numerator.toString()); //else append the entire fraction expression
			returnVal.append('/');
			returnVal.append(denominator.toString());
		}
		return returnVal.toString();
	}

	@Override
	public int compareTo(MathObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		return 0;
	}

	@Override
	public int getHierarchy() {
		return FRACTION;
	}

	@Override
	public int getPrecedence() {
		if (numerator.compareTo(BigInteger.ZERO) < 0) {
			return 100;
		}
		else return 9999999;
	}


	@Override
	public MathObject cloneMathObject() {
		Fraction clone = new Fraction(numerator,denominator);

		return clone;
	}
}
