package core;

import evaluator.*;
    import evaluator.extend.ConstantEvaluator;
    import evaluator.extend.FunctionEvaluator;
    import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

import java.math.MathContext;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.Set;
    import java.util.logging.Logger;

    /**
     * This class contains a lot of global static constant or mutable values,
     * functions, and instances.
     *
     *
     *
     */
    public final class CALC {
    	
    	private static final Logger log = Logger.getLogger( CALC.class.getName() );
    	

        /**
         * The math context for the engine. Controls rounding and truncations to 32
         * bit to IEEE 754R Decimal32 standards (7 digits). The precision can be
         * changed by calling <b>core.CALC.setMathContext(int precision)</b>
         */
        //public static MathContext mathcontext = MathContext.UNLIMITED;
        public static MathContext mathcontext = MathContext.DECIMAL32;
        //public static MathContext mathcontext = ;
        public static boolean operator_notation = false;
        public static int max_recursion_depth = 3;
        public static boolean full_integrate_mode = true;
        public static boolean fix_rounding_errors = true;
        /**
         * Standard unaryChar operator symbols
         */
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
        public static final Symbol FACTORIAL = new Symbol("FACTORIAL", new FACTORIAL(),
                Symbol.OPERATOR);

        public static final Symbol EXPAND = new Symbol("EXPAND", new EXPAND(),
                Symbol.OPERATOR);
        public static final Symbol DEPTH = new Symbol("DEPTH", new DEPTH(),
                Symbol.OPERATOR);
        public static final Symbol SIMPLIFY = new Symbol("SIMPLIFY", new SIMPLIFY(),
                Symbol.OPERATOR);


        public static final Symbol FACTOR = new Symbol("FACTOR", new FACTOR(),
                Symbol.OPERATOR);

        public static final Symbol SOLVEFORVARIABEL = new Symbol("SOLVEFORVARIABEL",
                new SOLVEFORVARIABEL(), Symbol.ONLY_EVAL_FIRST);



        
        /**
         * Special function symbols
         */
        //public static final CalcSymbol COT = new CalcSymbol("COT", new CalcCOT(),
        //CalcSymbol.NO_PROPERTY);
        public static final Symbol LN = new Symbol("LN", new LN(),
                Symbol.NO_PROPERTY);


        //TODO implement INT (integration). This is gonna a hell of a lot harder.
        public static final Symbol DEFINE = new Symbol("DEFINE", new DEFINE(),
                Symbol.OPERATOR | Symbol.FAST_EVAL);




        /**
         * Useful numerical constants
         */
        private static final byte[] IntegerZero = {0};
        public static final Integer ZERO = new Integer(IntegerZero);
        public static final Double D_ZERO = new Double(ZERO);
        private static final byte[] IntegerOne = {1};
        public static final Integer ONE = new Integer(IntegerOne);
        public static final Double D_ONE = new Double(ONE);
        private static final byte[] IntegerNegOne = {-1};
        public static final Integer NEG_ONE = new Integer(IntegerNegOne);
        public static final Double D_NEG_ONE = new Double(NEG_ONE);
        private static final byte[] IntegerTwo = {2};
        public static final Integer TWO = new Integer(IntegerTwo);
        private static final byte[] IntegerNegTwo = {-2};
        public static final Integer NEG_TWO = new Integer(IntegerNegTwo);
        public static final Double D_TWO = new Double(TWO);
        private static final byte[] IntegerFour = {4};
        public static final Integer FOUR = new Integer(IntegerFour);
        public static final Double D_FOUR = new Double(FOUR);
        public static final Fraction HALF = new Fraction(ONE, TWO);
        public static final Fraction NEG_HALF = new Fraction(NEG_ONE, TWO);
        public static final Double D_NEG_QUARTER = new Double("-0.25");
        public static final Double D_QUARTER = new Double("0.25");
        public static final Double D_HALF = new Double("0.5");
        public static final Double D_THREE_HALF = new Double("1.5");
        public static final Double INFINITY = new Double(java.lang.Double.POSITIVE_INFINITY);
        public static final Double NEG_INFINITY = new Double(java.lang.Double.NEGATIVE_INFINITY);
        /**
         * Header definitions for certain structs
         */
        public static final Symbol INTEGER = new Symbol("Integer");
        public static final Symbol DOUBLE = new Symbol("Double");
        public static final Symbol FRACTION = new Symbol("Fraction");
        public static final Symbol SYMBOL = new Symbol("Symbol");
        public static final Symbol MATRIX = new Symbol("Matrix");
        public static final Symbol VECTOR = new Symbol("Vector");
        public static final Symbol ERROR = new Symbol("Error");
        public static final Symbol USUB = new Symbol("USub");
        public static final Symbol CALCRELATIONSHIP = new Symbol("EQUATION");
        public static final Symbol SOLUTIONSET = new Symbol("SOLUTIONSET");
        /**
         * Symbols for built-in constants
         */
        public static final Symbol PI = new Symbol("PI",
                new ConstantEvaluator(new Double(Math.PI)), Symbol.CONSTANT);
        public static final Symbol E = new Symbol("E",
                new ConstantEvaluator(new Double(Math.E)), Symbol.CONSTANT);
        /**
         * HashMap that stores user defined local variables using the header symbol
         * as key
         */
        public static HashMap<Symbol, MathObject> defined = new HashMap<Symbol, MathObject>();

        /**
         *
         * @param variable .
         * @return true if the HashMap <b>defined</b> contains key <b>variable</b>.
         * False otherwise.
         */
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

        /**
         *
         * @param variable .
         * @return The value in HashMap <b>defined</b> corresponding to the key
         * <b>variable</b>
         */
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

        /**
         * Puts entry (<b>key</b>, <b>value</b>) in HashMap <b>defined</b>
         *
         * @param key .
         * @param value .
         */
        public static void setDefinedVariable(Symbol key, MathObject value) {
            if (hasDefinedVariable(key)) {
                if (!getDefinedVariable(key).equals(value)) {
                    defined.put(key, value);
                }
            } else {
                defined.put(key, value);
            }
        }

        /**
         *
         * @param input .
         * @return true if every char in input is upper case. False otherwise.
         */
        public static final boolean isUpperCase(String input) {
            for (int ii = 0; ii < input.length(); ii++) {
                if (Character.isLowerCase(input.charAt(ii))) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Symbolically and recursively evaluate a CalcObject using its own evaluate
         * method
         * @param input .
         * @return .
         */
        public static MathObject SYM_EVAL(MathObject input) {
        	
        	
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
         * @param identifier .
         * @return a function evaluator capable of evaluating the properties of
         * symbol
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
            } else if (identifier.equals("FACTOR")) {
                return (Symbol) FACTOR.clone();
            } else if (identifier.equals("DEFINE")) {
                return (Symbol) DEFINE.clone();
            } else if (identifier.equals("PI")) {
                return PI;
            } else if (identifier.equals("E")) {
                return E;
            } else if(identifier.equals("SOLVEFORVARIABEL")){
            	return (Symbol) SOLVEFORVARIABEL.clone();
            	
            }else {
            
                String name = "javacalculus.evaluator.Calc" + identifier;

                Class cls = null;
                FunctionEvaluator evaluator;
                Symbol symbol = new Symbol(identifier);

                try {
                    cls = Class.forName(name);
                } catch (ClassNotFoundException e) {
                }

                try {
                    evaluator = (FunctionEvaluator) cls.newInstance();
                    symbol.setEvaluator(evaluator);
                    return symbol;
                } catch (Exception e) {
                }
            }
            return null;
        }

        public static void setMathContext(int precision) {
            mathcontext = new MathContext(precision);
        }

        public static void toggleOperatorNotation() {
            operator_notation = !operator_notation;
        }
    }
