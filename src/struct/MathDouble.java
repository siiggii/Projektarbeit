package struct;

import core.CALC;
import exception.UnsupportedException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @see BigDecimal
 *
 */
public class MathDouble implements MathObject, Serializable{

    private BigDecimal value;
    private boolean isPositiveInfinity = false;
    private boolean isNegativeInfinity = false;
    private boolean isNaN = false;

    public MathDouble() {
        value = new BigDecimal(0.0d);
    }

    public MathDouble(BigDecimal bigDecimalIn) {
        //value = bigDecimalIn;
        if (CALC.fix_rounding_errors) {
            BigDecimal temp = bigDecimalIn;
            temp.setScale(0, RoundingMode.HALF_UP);
            //BigDecimal temp = new BigDecimal(bigDecimalIn, new MathContext(core.CALC.mathcontext.getPrecision(), RoundingMode.HALF_UP));
            int intVal = temp.intValue();
            double diff = Math.abs(intVal - bigDecimalIn.doubleValue());
            //System.out.println("DIFF: " + diff);
            if (diff < 0.00001) {
                //System.out.println("LETS MAKE " + bigDecimalIn.doubleValue() + " an int with value " + intVal);
                value = new BigDecimal(intVal, CALC.mathcontext);
            } else {
                value = new BigDecimal(bigDecimalIn.doubleValue(), CALC.mathcontext);
            }
        } else {
            value = new BigDecimal(bigDecimalIn.doubleValue(), CALC.mathcontext);
        }
    }

    public MathDouble(double doubleIn) {
        if (!java.lang.Double.isNaN(doubleIn) && !java.lang.Double.isInfinite(doubleIn)) {
            if (CALC.fix_rounding_errors) {
                BigDecimal temp = new BigDecimal(doubleIn, CALC.mathcontext);
                temp.setScale(0, RoundingMode.HALF_UP);
                //BigDecimal temp = new BigDecimal(bigDecimalIn, new MathContext(core.CALC.mathcontext.getPrecision(), RoundingMode.HALF_UP));
                int intVal = temp.intValue();
                double diff = Math.abs(intVal - doubleIn);
                //System.out.println("DIFF: " + diff);
                if (diff < 0.00001) {
                    //System.out.println("LETS MAKE " + doubleIn + " an int with value " + intVal);
                    value = new BigDecimal(intVal, CALC.mathcontext);
                } else {
                    value = new BigDecimal(doubleIn, CALC.mathcontext);
                }
            } else {
                value = new BigDecimal(doubleIn, CALC.mathcontext);
            }
        } else { //mathcontext does not apply for infinitesimal values
            value = null;
            if (doubleIn == java.lang.Double.POSITIVE_INFINITY) {
                isPositiveInfinity = true;
            }
            if (doubleIn == java.lang.Double.NEGATIVE_INFINITY) {
                isNegativeInfinity = true;
            }
            if (java.lang.Double.isNaN(doubleIn)) {
                isNaN = true;
            }
        }
    }

    public MathDouble(String stringIn) {
        value = new BigDecimal(stringIn, CALC.mathcontext);
    }

    public MathDouble(MathInteger calcMathIntegerIn) {
        value = new BigDecimal(calcMathIntegerIn.bigIntegerValue());
    }

    public double doubleValue() {
        if (isPositiveInfinity) {
            return java.lang.Double.POSITIVE_INFINITY;
        } else if (isNegativeInfinity) {
            return java.lang.Double.NEGATIVE_INFINITY;
        } else if (isNaN) {
            return java.lang.Double.NaN;
        } else {
            return value.doubleValue();
        }
    }

    public BigDecimal bigDecimalValue() {
        return value;
    }

    @Override
    public MathObject evaluate() {
        if (CALC.fix_rounding_errors) {
            //System.out.println("STARTA");
            //System.out.println(value);
            BigDecimal temp = value;
            temp = temp.setScale(0, RoundingMode.HALF_UP);
            //System.out.println(value);
            //System.out.println(temp);
            //System.out.println("FINNA");
            //BigDecimal temp = new BigDecimal(bigDecimalIn, new MathContext(core.CALC.mathcontext.getPrecision(), RoundingMode.HALF_UP));
            int intVal = temp.intValue();
            //System.out.println("intval:" + intVal);
            //System.out.println("doubleval:" + value.doubleValue());
            double diff = Math.abs(intVal - value.doubleValue());
            //System.out.println("DIFF: " + diff);
            if (diff < 0.00001) {
                //System.out.println("LETS MAKE " + value.doubleValue() + " an int with value " + intVal);
                value = new BigDecimal(intVal, CALC.mathcontext);
            } else {
                value = new BigDecimal(value.doubleValue(), CALC.mathcontext);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        if (isPositiveInfinity) {
            return "INFINITY";
        } else if (isNegativeInfinity) {
            return "-INFINITY";
        } else if (isNaN) {
            return "NaN";
        } else {
            return value.toString();
        }
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof MathDouble) {
            if (isPositiveInfinity) {
                return ((MathDouble) obj).isPositiveInfinity();
            }
            if (isNegativeInfinity) {
                return ((MathDouble) obj).isNegativeInfinity();
            }
            if (isNaN) {
                return ((MathDouble) obj).isNaN();
            }
            return value.doubleValue() == (((MathDouble) obj).doubleValue());
        } else if (obj instanceof MathInteger) {
            if (value == null) {
                return false;
            } else {
                return value.doubleValue() == (double) ((MathInteger) obj).intValue();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isNumber() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEven() {
        return mod(CALC.D_TWO).equals(CALC.D_ZERO);
    }

    public boolean isPositiveInfinity() {
        return isPositiveInfinity;
    }

    public boolean isNegativeInfinity() {
        return isNegativeInfinity;
    }

    public boolean isNaN() {
        return isNaN;
    }

    @Override
    public Symbol getHeader() {
        return CALC.DOUBLE;
    }

    public MathDouble add(MathDouble input) {
        if (isNaN || input.isNaN()) {
            return new MathDouble(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            return input.isNegativeInfinity() ? new MathDouble(0.0D) : new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return input.isPositiveInfinity() ? new MathDouble(0.0D) : new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        } else if (input.isPositiveInfinity()) {
            return isNegativeInfinity() ? new MathDouble(0.0D) : new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        } else if (input.isNegativeInfinity()) {
            return isPositiveInfinity() ? new MathDouble(0.0D) : new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        } else {
            return new MathDouble(value.add(input.bigDecimalValue()));
        }
    }

    public MathDouble multiply(MathDouble input) {
        if (isNaN || input.isNaN()) {
            return new MathDouble(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            return input.isNegative() ? new MathDouble(java.lang.Double.NEGATIVE_INFINITY) : new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return input.isNegative() ? new MathDouble(java.lang.Double.POSITIVE_INFINITY) : new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        } else if (input.isPositiveInfinity()) {
            return isNegative() ? new MathDouble(java.lang.Double.NEGATIVE_INFINITY) : new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        } else if (input.isNegativeInfinity()) {
            return isNegative() ? new MathDouble(java.lang.Double.POSITIVE_INFINITY) : new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        } else {
            return new MathDouble(value.multiply(input.bigDecimalValue()));
        }
    }

    public MathDouble divide(MathDouble input) {
        if (isNaN || input.isNaN()) {
            return new MathDouble(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            if (input.isPositiveInfinity()) {
                return new MathDouble(1.0D);
            } else if (input.isNegativeInfinity()) {
                return new MathDouble(-1.0D);
            } else {
                return new MathDouble(java.lang.Double.POSITIVE_INFINITY);
            }
        } else if (isNegativeInfinity) {
            if (input.isPositiveInfinity()) {
                return new MathDouble(-1.0D);
            } else if (input.isNegativeInfinity()) {
                return new MathDouble(1.0D);
            } else {
                return new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
            }
        }
        return new MathDouble(value.divide(input.bigDecimalValue(), CALC.mathcontext));
    }

    //I DONT LIKE THIS... USING DOUBLES
    public MathDouble power(MathDouble input) {
        if (isNaN || input.isNaN()) {
            return new MathDouble(java.lang.Double.NaN);
        } else {
            return new MathDouble(Math.pow(doubleValue(), input.doubleValue()));
        }
        //value.pow(input.doubleValue(), core.CALC.mathcontext);
    }

    /*public CalcDouble power(CalcInteger input) {
     if (isNaN) {
     return new CalcDouble(Double.NaN);
     } else {
     return new CalcDouble(value.pow(input.intValue(), core.CALC.mathcontext));
     }

     }*/
    public MathDouble mod(MathDouble input) {
        return new MathDouble(value.remainder(input.bigDecimalValue()));
    }

    public boolean isDivisibleBy(MathDouble input) {
        return mod(input).equals(CALC.D_ZERO);
    }

    public boolean isNegative() {
        if (value == null) {
            return isNegativeInfinity;
        } else {
            return compareTo(CALC.D_ZERO) < 0;
        }
    }

    public MathDouble negate() {
        if (isPositiveInfinity) {
            return new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNaN) {
            return new MathDouble(java.lang.Double.NaN);
        } else {
            return new MathDouble(value.negate());
        }
    }

    public boolean isInteger() {
        if (isNaN || isPositiveInfinity || isNegativeInfinity) {
            return false;
        }
        return (mod(CALC.D_ONE).equals(CALC.D_ZERO));
    }

    public boolean isInfinity() {
        return isPositiveInfinity || isNegativeInfinity;
    }

    @Override
    public int compareTo(MathObject obj) {
        if (isNaN) {
            return 0;
        }

        if (obj.isNumber()) {
            if (obj instanceof MathInteger) {
                if (isPositiveInfinity) {
                    return 1;
                } else if (isNegativeInfinity) {
                    return -1;
                } else if (value.doubleValue() < (double) ((MathInteger) obj).intValue()) {
                    return -1;
                } else if (value.doubleValue() > (double) ((MathInteger) obj).intValue()) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (obj instanceof MathDouble) {
                if (isPositiveInfinity) {
                    if (((MathDouble) obj).isPositiveInfinity()) {
                        return 0;
                    } else if (((MathDouble) obj).isNegativeInfinity()) {
                        return 1;
                    } else {
                        return 1;
                    }
                } else if (isNegativeInfinity) {
                    if (((MathDouble) obj).isPositiveInfinity()) {
                        return -1;
                    } else if (((MathDouble) obj).isNegativeInfinity()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else if (isNaN || ((MathDouble) obj).isNaN()) {
                    return 0;
                } else {
                    return value.compareTo(((MathDouble) obj).bigDecimalValue());
                }
            } else {
                throw new UnsupportedException(obj.toString());
            }
        }
        if (getHierarchy() > obj.getHierarchy()) {
            return 1;
        } else if (getHierarchy() < obj.getHierarchy()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int getHierarchy() {
        return DOUBLE;
    }

    @Override
    public int getPrecedence() {
        return 9999999; //it's over NINE MILLION AGAIN!!!!
    }


    @Override
    public MathObject cloneMathObject() {
        MathDouble clone = new MathDouble(value);

        return clone;
    }
}
