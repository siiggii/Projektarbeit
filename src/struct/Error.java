package struct;

import core.CALC;

import java.io.Serializable;

/**
 * Hierarchially encapsulates the java integer. BigInteger is used as the medium
 * because of the lack of size restrictions.
 *
 *
 */
public class Error implements MathObject, Serializable{

    public Error() {
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof Error) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isNumber() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Symbol getHeader() {
        return CALC.ERROR;
    }

    @Override
    public MathObject evaluate() {
        return this;
    }

    @Override
    public String toString() {
        return "core.CALC ERROR";
    }

    @Override
    public int compareTo(MathObject obj) {
        return -1;
    }

    @Override
    public int getHierarchy() {
        return SYMBOL;
    }

    @Override
    public int getPrecedence() {
        return -1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }



    @Override
    public MathObject cloneMathObject() {
        return null;
    }
}
