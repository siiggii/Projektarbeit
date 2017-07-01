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
public class Double implements MathObject, Serializable{

    private BigDecimal value;
    private boolean isPositiveInfinity = false;
    private boolean isNegativeInfinity = false;
    private boolean isNaN = false;

    public Double() {
        value = new BigDecimal(0.0d);
    }

    public Double(BigDecimal bigDecimalIn) {
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

    public Double(double doubleIn) {
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

    public Double(String stringIn) {
        value = new BigDecimal(stringIn, CALC.mathcontext);
    }

    public Double(Integer calcIntegerIn) {
        value = new BigDecimal(calcIntegerIn.bigIntegerValue());
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
        if (obj instanceof Double) {
            if (isPositiveInfinity) {
                return ((Double) obj).isPositiveInfinity();
            }
            if (isNegativeInfinity) {
                return ((Double) obj).isNegativeInfinity();
            }
            if (isNaN) {
                return ((Double) obj).isNaN();
            }
            return value.doubleValue() == (((Double) obj).doubleValue());
        } else if (obj instanceof Integer) {
            if (value == null) {
                return false;
            } else {
                return value.doubleValue() == (double) ((Integer) obj).intValue();
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

    public Double add(Double input) {
        if (isNaN || input.isNaN()) {
            return new Double(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            return input.isNegativeInfinity() ? new Double(0.0D) : new Double(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return input.isPositiveInfinity() ? new Double(0.0D) : new Double(java.lang.Double.NEGATIVE_INFINITY);
        } else if (input.isPositiveInfinity()) {
            return isNegativeInfinity() ? new Double(0.0D) : new Double(java.lang.Double.POSITIVE_INFINITY);
        } else if (input.isNegativeInfinity()) {
            return isPositiveInfinity() ? new Double(0.0D) : new Double(java.lang.Double.NEGATIVE_INFINITY);
        } else {
            return new Double(value.add(input.bigDecimalValue()));
        }
    }

    public Double multiply(Double input) {
        if (isNaN || input.isNaN()) {
            return new Double(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            return input.isNegative() ? new Double(java.lang.Double.NEGATIVE_INFINITY) : new Double(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return input.isNegative() ? new Double(java.lang.Double.POSITIVE_INFINITY) : new Double(java.lang.Double.NEGATIVE_INFINITY);
        } else if (input.isPositiveInfinity()) {
            return isNegative() ? new Double(java.lang.Double.NEGATIVE_INFINITY) : new Double(java.lang.Double.POSITIVE_INFINITY);
        } else if (input.isNegativeInfinity()) {
            return isNegative() ? new Double(java.lang.Double.POSITIVE_INFINITY) : new Double(java.lang.Double.NEGATIVE_INFINITY);
        } else {
            return new Double(value.multiply(input.bigDecimalValue()));
        }
    }

    public Double divide(Double input) {
        if (isNaN || input.isNaN()) {
            return new Double(java.lang.Double.NaN);
        } else if (isPositiveInfinity) {
            if (input.isPositiveInfinity()) {
                return new Double(1.0D);
            } else if (input.isNegativeInfinity()) {
                return new Double(-1.0D);
            } else {
                return new Double(java.lang.Double.POSITIVE_INFINITY);
            }
        } else if (isNegativeInfinity) {
            if (input.isPositiveInfinity()) {
                return new Double(-1.0D);
            } else if (input.isNegativeInfinity()) {
                return new Double(1.0D);
            } else {
                return new Double(java.lang.Double.NEGATIVE_INFINITY);
            }
        }
        return new Double(value.divide(input.bigDecimalValue(), CALC.mathcontext));
    }

    //I DONT LIKE THIS... USING DOUBLES
    public Double power(Double input) {
        if (isNaN || input.isNaN()) {
            return new Double(java.lang.Double.NaN);
        } else {
            return new Double(Math.pow(doubleValue(), input.doubleValue()));
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
    public Double mod(Double input) {
        return new Double(value.remainder(input.bigDecimalValue()));
    }

    public boolean isDivisibleBy(Double input) {
        return mod(input).equals(CALC.D_ZERO);
    }

    public boolean isNegative() {
        if (value == null) {
            return isNegativeInfinity;
        } else {
            return compareTo(CALC.D_ZERO) < 0;
        }
    }

    public Double negate() {
        if (isPositiveInfinity) {
            return new Double(java.lang.Double.NEGATIVE_INFINITY);
        } else if (isNegativeInfinity) {
            return new Double(java.lang.Double.POSITIVE_INFINITY);
        } else if (isNaN) {
            return new Double(java.lang.Double.NaN);
        } else {
            return new Double(value.negate());
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
            if (obj instanceof Integer) {
                if (isPositiveInfinity) {
                    return 1;
                } else if (isNegativeInfinity) {
                    return -1;
                } else if (value.doubleValue() < (double) ((Integer) obj).intValue()) {
                    return -1;
                } else if (value.doubleValue() > (double) ((Integer) obj).intValue()) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (obj instanceof Double) {
                if (isPositiveInfinity) {
                    if (((Double) obj).isPositiveInfinity()) {
                        return 0;
                    } else if (((Double) obj).isNegativeInfinity()) {
                        return 1;
                    } else {
                        return 1;
                    }
                } else if (isNegativeInfinity) {
                    if (((Double) obj).isPositiveInfinity()) {
                        return -1;
                    } else if (((Double) obj).isNegativeInfinity()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else if (isNaN || ((Double) obj).isNaN()) {
                    return 0;
                } else {
                    return value.compareTo(((Double) obj).bigDecimalValue());
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
    public boolean isSameSolution(MathObject obj) {

        if(!(obj instanceof Double)){
            return false;
        }
        Double doub2 = (Double) obj;

        if(!(value.compareTo(doub2.value)==0)) return false;

        return true;
    }
}
