package core;

import evaluator.*;
    import evaluator.extend.ConstantEvaluator;
import struct.*;
import struct.MathDouble;
import struct.MathInteger;
import struct.MathObject;

import java.math.MathContext;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.Set;
    import java.util.logging.Logger;

    /**
     * This class contains a lot of global static constant and mutable values,
     * functions, and instances.
     *
     */
    public final class CALC {
    	
    	private static final Logger log = Logger.getLogger( CALC.class.getName() );
    	

        public static MathContext mathcontext = MathContext.DECIMAL32;

        public static boolean operator_notation = false;
        public static int max_recursion_depth = 3;
        public static boolean full_integrate_mode = true;
        public static boolean fix_rounding_errors = true;

        public static final Symbol EQUAL = new Symbol("EQUAL", new EQUAL());

        public static final Symbol ADD = new Symbol("ADD", new ADD(),
                Symbol.OPERATOR | Symbol.COMMUTATIVE | Symbol.ASSOCIATIVE | Symbol.UNIPARAM_IDENTITY);
        public static final Symbol SUBTRACT = new Symbol("SUBTRACT",
                Symbol.OPERATOR | Symbol.UNIPARAM_IDENTITY);
        public static final Symbol MULTIPLY = new Symbol("MULTIPLY", new MULTIPLY(),
                Symbol.OPERATOR | Symbol.COMMUTATIVE | Symbol.ASSOCIATIVE | Symbol.UNIPARAM_IDENTITY);
        public static final Symbol DIVIDE = new Symbol("DIVIDE",
                Symbol.OPERATOR | Symbol.UNIPARAM_IDENTITY);
        public static final Symbol POWER = new Symbol("POWER", new POWER(),
                Symbol.OPERATOR | Symbol.UNIPARAM_IDENTITY);


        public static final Symbol EXPAND = new Symbol("EXPAND", new EXPAND(),
                Symbol.OPERATOR);
        public static final Symbol DEPTH = new Symbol("DEPTH", new DEPTH(),
                Symbol.OPERATOR);
        public static final Symbol SIMPLIFY = new Symbol("SIMPLIFY", new SIMPLIFY(),
                Symbol.OPERATOR);


        public static final Symbol SOLVEFORVARIABEL = new Symbol("SOLVEFORVARIABEL",
                new SOLVEFORVARIABEL());
        public static final Symbol PLUSMINUS = new Symbol("PLUSMINUS",
                new PLUSMINUS(), Symbol.FAST_EVAL);


        

        public static final Symbol LN = new Symbol("LN", new LN(),
                Symbol.NO_PROPERTY);



        public static final Symbol DEFINE = new Symbol("DEFINE", new DEFINE(),
                Symbol.OPERATOR | Symbol.FAST_EVAL);




        /**
         * Useful numerical constants
         */
        private static final byte[] IntegerZero = {0};
        public static final MathInteger ZERO = new MathInteger(IntegerZero);
        public static final MathDouble D_ZERO = new MathDouble(ZERO);
        private static final byte[] IntegerOne = {1};
        public static final MathInteger ONE = new MathInteger(IntegerOne);
        public static final MathDouble D_ONE = new MathDouble(ONE);
        private static final byte[] IntegerNegOne = {-1};
        public static final MathInteger NEG_ONE = new MathInteger(IntegerNegOne);
        public static final MathDouble D_NEG_ONE = new MathDouble(NEG_ONE);
        private static final byte[] IntegerTwo = {2};
        public static final MathInteger TWO = new MathInteger(IntegerTwo);
        private static final byte[] IntegerNegTwo = {-2};
        public static final MathInteger NEG_TWO = new MathInteger(IntegerNegTwo);
        public static final MathDouble D_TWO = new MathDouble(TWO);
        private static final byte[] IntegerFour = {4};
        public static final MathInteger FOUR = new MathInteger(IntegerFour);
        public static final MathDouble D_FOUR = new MathDouble(FOUR);
        public static final Fraction HALF = new Fraction(ONE, TWO);
        public static final Fraction NEG_HALF = new Fraction(NEG_ONE, TWO);
        public static final MathDouble D_NEG_QUARTER = new MathDouble("-0.25");
        public static final MathDouble D_QUARTER = new MathDouble("0.25");
        public static final MathDouble D_HALF = new MathDouble("0.5");
        public static final MathDouble D_THREE_HALF = new MathDouble("1.5");
        public static final MathDouble INFINITY = new MathDouble(java.lang.Double.POSITIVE_INFINITY);
        public static final MathDouble NEG_INFINITY = new MathDouble(java.lang.Double.NEGATIVE_INFINITY);
        /**
         * Header definitions for certain structs
         */
        public static final Symbol INTEGER = new Symbol("Integer");
        public static final Symbol DOUBLE = new Symbol("Double");
        public static final Symbol FRACTION = new Symbol("Fraction");
        public static final Symbol SYMBOL = new Symbol("Symbol");
        public static final Symbol ERROR = new Symbol("Error");
        public static final Symbol SET = new Symbol("SET");
        /**
         * Symbols for built-in constants
         */
        public static final Symbol PI = new Symbol("PI",
                new ConstantEvaluator(new MathDouble(Math.PI)), Symbol.CONSTANT);
        public static final Symbol E = new Symbol("E",
                new ConstantEvaluator(new MathDouble(Math.E)), Symbol.CONSTANT);
        /**
         * HashMap that stores user defined local variables using the header symbol
         * as key
         */
        public static HashMap<Symbol, MathObject> defined = new HashMap<Symbol, MathObject>();


        public static boolean hasDefinedVariable(Symbol variable) {
            //TODO optimize this process
            if (defined.isEmpty()) {
                return false;
            }

            Set<Symbol> keySet = defined.keySet();
            Iterator<Symbol> iter = keySet.iterator();

            while (iter.hasNext()) {
                if (variable.equals(iter.next())) {
                    return true;
                }
            }

            return false;
        }
        public static MathObject getDefinedVariable(Symbol variable) {
            if (!hasDefinedVariable(variable)) {
                return null;
            }

            Set<Symbol> keySet = defined.keySet();
            Iterator<Symbol> iter = keySet.iterator();

            while (iter.hasNext()) {
                Symbol next = iter.next();
                if (variable.equals(next)) {
                    return defined.get(next);
                }
            }

            return null;
        }

        public static Symbol getDefinedVariableKey(MathObject value) {
            if (!defined.containsValue(value)) {
                return null;
            }

            Set<Symbol> keySet = defined.keySet();
            Iterator<Symbol> iter = keySet.iterator();

            while (iter.hasNext()) {
                Symbol next = iter.next();
                if (value.equals(defined.get(next))) {
                    return next;
                }
            }

            return null;
        }


        public static void setDefinedVariable(Symbol key, MathObject value) {
            if (hasDefinedVariable(key)) {
                if (!getDefinedVariable(key).equals(value)) {
                    defined.put(key, value);
                }
            } else {
                defined.put(key, value);
            }
        }


        public static final boolean isUpperCase(String input) {
            for (int ii = 0; ii < input.length(); ii++) {
                if (Character.isLowerCase(input.charAt(ii))) {
                    return false;
                }
            }
            return true;
        }

        /**
         * evaluate MathObject recursivly
         */
        public static MathObject EVALUATE(MathObject input) {
        	
        	
        	log.info("input = " + input.toString() );  /////
        	
        	
            MathObject returnVal = null;
            try {
                returnVal = input.evaluate();
                log.info("returnVal = "+ returnVal.toString() );  /////
            } catch (Exception e1) {
                //e1.printStackTrace();
            }

            if (returnVal == null) {
                return null;
            }
            int i = 0;
            i++;


            MathObject temp = null;

            try {
                temp = returnVal.evaluate();
                log.info("temp1  = "+ temp.toString() );  /////
            } catch (Exception e1) {
                //e1.printStackTrace();
            }

            //if 2nd evaluation still produced different result, do it again
            //until the result cannot be further evaluated into a different form
            while (temp != null && !returnVal.equals(temp)) {
                returnVal = temp;
                try {
                    temp = returnVal.evaluate();
                    log.info("temp2  = "+ temp.toString() );  /////
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            return (returnVal == null) ? input : returnVal;
        }

        /**
         *

         */
        public static Symbol getSymbol(String identifier) {
            if (identifier.equals("LN")) {
                return (Symbol) LN.clone();
            } else if (identifier.equals("EXPAND")) {
                return (Symbol) EXPAND.clone();
            } else if (identifier.equals("DEPTH")) {
                return (Symbol) DEPTH.clone();
            } else if (identifier.equals("SIMPLIFY")) {
                return (Symbol) SIMPLIFY.clone();
            } else if (identifier.equals("DEFINE")) {
                return (Symbol) DEFINE.clone();
            } else if (identifier.equals("PI")) {
                return PI;
            } else if (identifier.equals("E")) {
                return E;
            } else if(identifier.equals("SOLVEFORVARIABEL")){
            	return (Symbol) SOLVEFORVARIABEL.clone();
            	
            }else
            return null;
        }

        public static void setMathContext(int precision) {
            mathcontext = new MathContext(precision);
        }

        public static void toggleOperatorNotation() {
            operator_notation = !operator_notation;
        }
    }
