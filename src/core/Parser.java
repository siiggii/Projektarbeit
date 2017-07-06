package core;

import exception.SyntaxException;
import exception.UnsupportedException;
import struct.*;
import struct.MathDouble;
import struct.MathInteger;
import struct.MathObject;

/**
 * parses a mathematical expression string into a CalcObject.
 */
public final class Parser {
	
	/**
	 * static constants define the values that token can take based on what currentChar is, This keeps track of the TYPE of
	 * currentChar.
	 */
	private final static int
	CALC_NULL = 0,				//end of line/file or unsupported token
	CALC_POWER = 1,				//exponentiation
	CALC_MULTIPLY = 2,			//multiply
	CALC_DIVIDE = 3,			//divide
	CALC_SUBTRACT = 4,			//subtract
	CALC_ADD = 5,				//add
	CALC_PARENTHESISOPEN = 6,	//open parenthesis
	CALC_PARENTHESISCLOSE = 7,	//close parenthesis
	CALC_IDENTIFIER = 10,		//variable names
	CALC_DIGIT = 11,			//numbers
	CALC_COMMA = 12,			//commas (mostly used in function argument list)
	CALC_DEFINE = 13,			//variable assignment (i.e. x=10, f(x)=x+4, etc)
	CALC_EQUAL = 16, 	 		//EQUATION assignment (i.e. x=10, f(x)=x+4, etc)
	CALC_PLUSMINUS = 17;		//plus minus

	private String inputString;
	private char currentChar;
	private int currentCharIndex;
	private int token;
	

	public Parser(String StringIn) {
		inputString = StringIn;
	}
	

	public Parser() {
		inputString = null;
	}
	
	public MathObject parse(String input) throws SyntaxException {
		inputString = input;
		currentChar = ' ';
		currentCharIndex = 0;
		token = CALC_NULL;
		return parse();
	}
	
	public MathObject parse() throws SyntaxException {
		parseNextToken(); //initialize the token for the parser
		
		MathObject returnVal = parseDefine(); //step through the precedence levels, starting with highest -> define
		
		return returnVal;
	}
	
	/**
	 * identifies the next sequence of characters by a unique int stored in global variable
	 */
	private void parseNextToken() throws SyntaxException {
		while (inputString.length() > currentCharIndex) {
			currentChar = inputString.charAt(currentCharIndex++);
			token = CALC_NULL;
		
			if (currentChar != '\n' && currentChar != '\t'
				&& currentChar != '\r' && currentChar != ' ') { //make sure the char is not terminating or whitespace
				if ((currentChar >= 'a' && currentChar <= 'z') //is the char a letter (identifier)?
						|| (currentChar >= 'A' && currentChar <= 'Z')) {

					token = CALC_IDENTIFIER;
					return;
				}

				if (currentChar >= '0' && currentChar <= '9') { //is the char a number?
					token = CALC_DIGIT;
					return;
				}

				switch (currentChar) { //brute force identify the char and store the identification in token
					case '(':
						token = CALC_PARENTHESISOPEN;
						break;
					case ')':
						token = CALC_PARENTHESISCLOSE;
						break;
					case '[':
						token = CALC_PARENTHESISOPEN;
						break;
					case ']':
						token = CALC_PARENTHESISCLOSE;
						break;
					case ',':
						token = CALC_COMMA;
						break;
					case '^':
						token = CALC_POWER;
						break;
					case '+':
						token = CALC_ADD;
						break;
					case '-':
						token = CALC_SUBTRACT;
						break;
					case '±':
						token = CALC_PLUSMINUS;
						break;
					case '*':
						token = CALC_MULTIPLY;
						break;
					case '·':
						token = CALC_MULTIPLY;
						break;
					case '×':
						token = CALC_MULTIPLY;
						break;
					case '/':
						token = CALC_DIVIDE;
						break;
					case '=':
						token = CALC_EQUAL;
						break;
					case ':':
						token = CALC_DEFINE;
						break;
					default:
						throw new SyntaxException("Unidentified character: " + currentChar);
				}
				
				if (token == CALC_NULL) {
					throw new SyntaxException("No token identified");
				}
				
				return;
			}
		}
		
		//end of loop variable reset
		currentCharIndex = inputString.length() + 1;
		currentChar = ' ';
		token = CALC_NULL;
			
	}
	
	private MathObject parseDefine() throws SyntaxException {
		MathObject returnVal = parseEqual(); //get next precendence level: expression
		int tempToken;
		
		while (token == CALC_DEFINE) {
			tempToken = token;
			parseNextToken();
			
			if (tempToken == CALC_DEFINE) {
				returnVal = CALC.DEFINE.createFunction(returnVal, parseExpression());
			}
		}
		return returnVal;
	}

	private MathObject parseEqual() throws SyntaxException {
		MathObject returnVal = parseExpression();

		if(token == CALC_EQUAL){
			Relationship equal = CALC.EQUAL.createRelationship();
			equal.add(returnVal);
			parseNextToken();
			equal.add(parseExpression());

			//todo implement this right
			Function calcSolveForVariable = CALC.SOLVEFORVARIABEL.createFunction(equal);


			return  calcSolveForVariable;
		}
		return returnVal;
	}
	
	private MathObject parseExpression() throws SyntaxException {
		
		int tempToken;
		MathObject returnVal;
		
		if (token == CALC_ADD || token == CALC_SUBTRACT||token == CALC_PLUSMINUS) {
			tempToken = token;
			parseNextToken();
			
			if (tempToken == CALC_SUBTRACT) {
				returnVal = parseMultiplication(true);
			}
			else {
				returnVal = parseMultiplication(false);
			}
		}
		else {
			returnVal = parseMultiplication(false);
		}
		
		if (token == CALC_ADD || token == CALC_SUBTRACT || token == CALC_PLUSMINUS) {


			Function returnFunction = CALC.ADD.createFunction(returnVal);
			
			while (token == CALC_ADD || token == CALC_SUBTRACT|| token == CALC_PLUSMINUS) {
				if (token == CALC_ADD) {
					parseNextToken();
					returnFunction.add(parseMultiplication(false));
				}
				else if(token == CALC_PLUSMINUS){
					parseNextToken();
					Function plusMinusFunction = CALC.PLUSMINUS.createFunction(parseMultiplication(false));
					returnFunction.add(plusMinusFunction);
				}
				else {
					parseNextToken();
					returnFunction.add(parseMultiplication(true));
				}
			}
			
			return returnFunction;
		}
		else return returnVal;
	}
	
	private MathObject parseMultiplication(boolean isNegative) throws SyntaxException {
		MathObject returnVal = parseDivision();
		
		if (isNegative) { //handle negated case
			if (returnVal instanceof MathInteger) {
				returnVal = ((MathInteger)returnVal).multiply(CALC.NEG_ONE);
			}
			else if (returnVal instanceof Fraction) {
				returnVal = ((Fraction)returnVal).multiply(CALC.NEG_ONE);
			}
			else returnVal = CALC.MULTIPLY.createFunction(CALC.NEG_ONE, returnVal);
		}
		
		if (token != CALC_MULTIPLY) {
			return returnVal;
		}
		
		Function returnFunction = new Function(CALC.MULTIPLY, returnVal);
	
		while (token == CALC_MULTIPLY) {
			parseNextToken();
			returnFunction.add(parseDivision());
		}
		
		return returnFunction;
		
	}
	

	private MathObject parseDivision() throws SyntaxException {
		MathObject numerator = parsePower();
		MathObject denominator;
		
		if (token != CALC_DIVIDE) {
			return numerator;
		}
		
		parseNextToken();
		
		denominator = parsePower();
		
		if (token != CALC_DIVIDE) {
			if (numerator instanceof MathInteger && denominator instanceof MathInteger) {
				if (denominator == CALC.ZERO) {
					throw new SyntaxException("Division by zero.");
				}
				return new Fraction((MathInteger)numerator, (MathInteger)denominator);
			}
			else {
				Function reciprocal = CALC.POWER.createFunction(denominator, CALC.NEG_ONE);
				return new Function(CALC.MULTIPLY, numerator, reciprocal);
			}
		}
		
		Function reciprocal = CALC.POWER.createFunction(denominator, CALC.NEG_ONE);
		Function function = new Function(CALC.MULTIPLY, numerator, reciprocal);
		
		while (token == CALC_DIVIDE) { //handle continued fraction expressions
			parseNextToken();
			function.add(CALC.POWER.createFunction(parsePower(), CALC.NEG_ONE));
		}
		
		return function;
	}
	
	private MathObject parsePower() throws SyntaxException {
		MathObject returnVal = parseTerm();
		
		if (token != CALC_POWER) {
			return returnVal;
		}
		
		while (token == CALC_POWER) {
			parseNextToken();
			Function function = new Function(CALC.POWER, returnVal);
			function.add(parseTerm());
			returnVal = function;
		}
		
		return returnVal;
	}

	

	

	private MathObject parseTerm() throws SyntaxException {
		MathObject returnVal;
		
		if (token == CALC_SUBTRACT) {
			parseNextToken();
			return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, parseTerm());
		}
		if (token == CALC_IDENTIFIER) {
			Symbol id = parseIdentifier();
			
			if (token == CALC_PARENTHESISOPEN) {
				return parseFunction(id);
			}
			else if (CALC.hasDefinedVariable(id)) {
				return CALC.getDefinedVariable(id);
			}
			else return id;
		}
		if (token == CALC_DIGIT) {
			return parseNumber();
		}
		if (token == CALC_PARENTHESISOPEN) {
			
			parseNextToken();
			
			returnVal = parseExpression(); //return all the way to root recursion
			
			if (token != CALC_PARENTHESISCLOSE) {
				throw new SyntaxException("Missing close parenthesis");
			}
			
			parseNextToken();
			
			return returnVal;
		}
		
		switch (token) {
			case CALC_PARENTHESISCLOSE:
				throw new SyntaxException("Extra closing parenthesis");
		}
		
		throw new SyntaxException("Unable to parse term: " + currentChar);
	}
	
	private MathObject parseFunction(Symbol symbol) throws SyntaxException {
		Function function = new Function(symbol);
		parseNextToken();
		
		if (token == CALC_PARENTHESISCLOSE) {
			parseNextToken();
			return function;
		}
		
		parseParameters(function);
		
		if (token == CALC_PARENTHESISCLOSE) {
			parseNextToken();
			return function;
		}
		
		throw new SyntaxException("Expecting '('");
	}

	private void parseParameters(Function inputFunction) throws SyntaxException {
		while (true) {	//this scares the SHIT out of me but I have to
			inputFunction.add(parseExpression());
			
			if (token != CALC_COMMA) break; //no more parameters
			
			parseNextToken();
		}
	}
	
	private Symbol parseIdentifier() throws SyntaxException {
		StringBuffer identifier = new StringBuffer();
		
		identifier.append(currentChar);
		
		parseNextChar();
		
		while ((currentChar >= 'a' && currentChar <= 'z') 
				|| (currentChar >= 'A' && currentChar <= 'Z') 
				|| (currentChar >= '0' && currentChar <= '9')) {
			identifier.append(currentChar);
			parseNextChar();
		}
		
		currentCharIndex--;
		
		parseNextToken();
		
		Symbol symbol;
		
		if (CALC.isUpperCase(identifier.toString())) { //if the symbol is all upper case, it must be a built in function
			Symbol temp;
			if ((temp = CALC.getSymbol(identifier.toString())) != null)
				symbol = temp;
			else throw new UnsupportedException(identifier.toString());
		}
		else {
			symbol = new Symbol(identifier);

		}
		
		return symbol;
	}

	private MathObject parseNumber() throws SyntaxException {
		StringBuffer numberString = new StringBuffer();
		boolean IsFloating = false;
		
		numberString.append(currentChar); //append first digit
		
		parseNextChar();
		//append any digits beyond the first, including decimal place
		while ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') {
			if (currentChar == '.') {
				if (IsFloating) break;
				IsFloating = true;
				numberString.append(currentChar);
				parseNextChar();	
			}
			else {
				numberString.append(currentChar);
				parseNextChar();
			}
		}
	
		currentCharIndex--;
		parseNextToken();
		
		/*if (!(currentChar >= '0' && currentChar <= '9') && currentChar != ',' && currentChar != ']') {		
			if (IsFloating) {
				return core.CALC.MULTIPLY.createFunction(new CalcDouble(numberString.toString()), parseTerm());
			}
			else {
				return core.CALC.MULTIPLY.createFunction(new CalcInteger(numberString.toString()), parseTerm());
			}
		}*/
	
		if (IsFloating) {
			return new MathDouble(numberString.toString());
		}
		else {
			return new MathInteger(numberString.toString());
		}
	}

	private void parseNextChar() {
		if (inputString.length() > currentCharIndex) {
			currentChar = inputString.charAt(currentCharIndex++);
			return;
		}
		
		currentCharIndex = inputString.length() + 1;
		currentChar = ' ';
		token = CALC_NULL;
	}
	
	@Override
	public String toString() {
		return inputString;
	}
}