package evaluator;

import com.sun.org.apache.regexp.internal.RE;
import core.CALC;
import evaluator.extend.*;
import exception.WrongParametersException;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SOLVEFORVARIABEL implements FunctionEvaluator {

	/**
 *
 */
public SOLVEFORVARIABEL() {}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(CalcFunction)
	 */

	List<Addends> addendsList;
	Solutionset solutionset;
    String variableToSolveFor = "x";

	@Override
	public MathObject evaluate(Function input) {

		if(containsVariable(input,"y")){
			variableToSolveFor = "y";
		}

		//first step is to bring all terms on one side of equation and to simplify and expand them

		//bring everything to right side
		Relationship equation = (Relationship)input.get(0);
		Function leftSideOfEquation = new Function(CALC.ADD, equation.get(0), new Function(CALC.MULTIPLY,CALC.D_NEG_ONE,equation.get(1)));
		//simplify and expand
		Function simAndExpand = new Function(CALC.EXPAND,leftSideOfEquation);
		MathObject expanded = CALC.SYM_EVAL(simAndExpand);

		//check if the expanded Term is addition
		if(expanded.getHeader().equals(CALC.ADD)){
			Function addendsTerm =  (Function) expanded;

			addendsList = new ArrayList<>();
			// add addends to List
			for (int i = 0; i < addendsTerm.size();i++){
				addendsList.add(new Addends(addendsTerm.get(i)));
			}
			// sort list for variable exponent
			Collections.sort(addendsList);
			//all variables who's exponent is 0 are added together
			Function addExponent0 = new Function(CALC.ADD);
			int listSize = addendsList.size();
			for(int i=0; i<listSize;i++){
				if( addendsList.get(i).getPotenzDesSummanden()==0){
					addExponent0.add( addendsList.get(i).getAbc());
					addendsList.remove( addendsList.get(i));
					listSize = listSize-1;
					i--;
				}
			}
			addendsList.add(new Addends(addExponent0,0));


			solutionset = new Solutionset(CALC.SOLUTIONSET);
			//check term for elementary algebra
			if(addendsList.size() == 2){
				if(addendsList.get(0).getPotenzDesSummanden()!=0&&addendsList.get(1).getPotenzDesSummanden()==0){
					simpleEquation();
				}
			}
			//check term for polynomial equation
			else if(addendsList.size() == 3){
				if(addendsList.get(0).getPotenzDesSummanden()==2
						&& addendsList.get(1).getPotenzDesSummanden()==1
						&& addendsList.get(2).getPotenzDesSummanden()==0){
					//midnightformula?
					Solutionset solutionsetN = quadraticEquation();
					solutionset.addAll(solutionsetN.getParameters());
				}
				else{
					if(addendsList.get(0).getPotenzDesSummanden() / addendsList.get(1).getPotenzDesSummanden() == 2 && addendsList.get(2).getPotenzDesSummanden()==0){
						substitution();
					}
				}
				/*
					&& addendsList.get(0).getPotenzDesSummanden()==4
						&& addendsList.get(1).getPotenzDesSummanden()==2
						&& addendsList.get(2).getPotenzDesSummanden()==0){
					//todo substitution();
				*/

			}
			//can't solve yet
			else{

			}



		}
		else{
			throw new WrongParametersException("SOLVEFORVARIABLE -> wrong number of parameters");
		}

		return solutionset;
	}
	private void substitution(){
		int substitutionFactor = addendsList.get(1).getPotenzDesSummanden();
		Function function1 = new Function(CALC.POWER,new Symbol("x"),new Integer(substitutionFactor));
		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol("y"), function1);
		solutionset.add(result1);
		Function function2 = new Function(CALC.ADD);
		for (Addends addens:addendsList) {
			function2.add(addens.summand);
		}
		Relationship result2 = new Relationship(CALC.EQUAL,function2, CALC.D_ZERO);
		//solutionset.add(result2);
		Function function3 = new Function(CALC.ADD);
		for (Addends addens:addendsList) {
			addens.substitute(substitutionFactor);
			function3.add(addens.summand);
		}
		variableToSolveFor = "y";
		Solutionset solutionsetN = quadraticEquation();
		solutionset.addAll(solutionsetN.getParameters());


		for (MathObject mathObject:solutionsetN.getParameters()) {

			Function function5 = new Function(CALC.POWER,new Integer(substitutionFactor),CALC.D_NEG_ONE);
			Function function4 = new Function(CALC.POWER,((Relationship)mathObject).get(1),function5);
			MathObject resultMathObject2 = CALC.SYM_EVAL(function4);
			Relationship result3 = new Relationship(CALC.EQUAL,new Symbol("x"), function4);
			MathObject resultMathObject1 = CALC.SYM_EVAL(result3);
			solutionset.add(resultMathObject1);
		}
	}

	private void simpleEquation(){
		MathObject leftSide = addendsList.get(0).getSummand();
		MathObject rightSide = new Function(CALC.ADD, new Function(CALC.MULTIPLY,CALC.D_NEG_ONE,addendsList.get(1).getSummand()));

		while(1==1) {
			if (leftSide instanceof Function) {
				Function left = (Function) leftSide;


				if (left.getHeader().equals(CALC.MULTIPLY)) {
					Function calcFunction = new Function(CALC.MULTIPLY);
					int sizeleft = left.size();
					for (int i = 0; i < sizeleft; i++) {
						if (!containsVariable(((Function) left).get(i),variableToSolveFor)) {
							calcFunction.add(((Function) left).get(i));
							 left.remove(i);
							i--;
							sizeleft--;
						}
					}
					leftSide = left.get(0);
					if (calcFunction.size() != 1) {
						rightSide = new Function(CALC.MULTIPLY, rightSide, new Function(CALC.POWER, calcFunction, CALC.D_NEG_ONE));
					} else {
						rightSide = new Function(CALC.MULTIPLY, rightSide, new Function(CALC.POWER, calcFunction.get(0), CALC.D_NEG_ONE));
					}
				} else if (left.getHeader().equals(CALC.POWER)) {
					MathObject newLeftSide = ((Function) leftSide).get(0);
					MathObject newRightSide = new Function(CALC.POWER, rightSide, new Function(CALC.POWER,((Function) leftSide).get(1), CALC.D_NEG_ONE));
					leftSide = newLeftSide;
					rightSide = newRightSide;
				}
			} else {
				MathObject mathObject = CALC.SYM_EVAL(rightSide);

				Relationship relationship = new Relationship(CALC.EQUAL, leftSide, mathObject);
				solutionset.add(relationship);
				break;
			}
		}
		//addendsList.get(0)
	}

	private Solutionset quadraticEquation(){
		//2*a
		Function m1 = new Function(CALC.MULTIPLY,new Integer(2), addendsList.get(0).getAbc());
		//(2*a)^-1
		Function m7 = new Function(CALC.POWER,m1,CALC.D_NEG_ONE);
		//b^2
		Function m2 = new Function(CALC.POWER, addendsList.get(1).getAbc(),new Integer(2));
		//-1*4*a*c
		Function m3 = new Function(CALC.MULTIPLY);
		m3.add(CALC.D_NEG_ONE);
		m3.add(new Integer(4));
		m3.add(addendsList.get(0).getAbc());
		m3.add(addendsList.get(2).getAbc());
		//b^2-1*4*a*c
		Function m4 = new Function(CALC.ADD,m2,m3);
		//(b^2-1*4*a*c)^0.5
		Function m5 = new Function(CALC.POWER,m4,new Double(0.5));
		//-1*b
		Function m6 = new Function(CALC.MULTIPLY, CALC.D_NEG_ONE, addendsList.get(1).getAbc());
		//-1*b+(b^2-1*4*a*c)^0.5
		Function resultplus = new Function(CALC.ADD, m6,m5);
		//-(b^2-1*4*a*c)^0.5
		Function minus = new Function(CALC.MULTIPLY, CALC.D_NEG_ONE,m5);
		Function resultminus = new Function(CALC.ADD, m6,minus);



		Function result11 = new Function(CALC.MULTIPLY,resultplus,m7);
		Function result12 = new Function(CALC.MULTIPLY,resultminus,m7);
		MathObject resultMathObject1 = CALC.SYM_EVAL(result11);
		MathObject resultMathObject2 = CALC.SYM_EVAL(result12);
		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol(variableToSolveFor), resultMathObject1);
		Relationship result2 = new Relationship(CALC.EQUAL,new Symbol(variableToSolveFor), resultMathObject2);
		Solutionset solutionset = new Solutionset(CALC.SOLUTIONSET);
		solutionset.add(result1);
		solutionset.add(result2);
		return solutionset;
	}

	private boolean containsVariable(MathObject mathObject, String variableToSolveFor){
		if(mathObject instanceof Function){
			for(int i = 0; i<((Function) mathObject).size(); i++) {
				if (containsVariable(((Function) mathObject).get(i),variableToSolveFor)) {
					return true;
				}
			}
		}
		else if(mathObject instanceof Symbol){
			if(((Symbol) mathObject).getName().equals( variableToSolveFor)){
				return true;
			}
		}
		return false;
	}



	class Addends implements Comparable<Addends>{
		MathObject summand;
		int potenzDesSummanden;
		MathObject abc;


		public MathObject getAbc(){
			return abc;
		}

		public Addends(MathObject summand) {

			this.summand = summand;
			if(summand instanceof Function) {
				if (((Function) summand).getHeader().equals(CALC.ADD)) {
					abc = summand;
				}
				else{
					defineSum();
				}
			}
			else {
				defineSum();
			}
		}

		public Addends(MathObject summand, int potenzDesSummanden){
			this.summand =summand;
			abc = summand;
			potenzDesSummanden = potenzDesSummanden;
		}

		public MathObject getSummand(){
			return summand;
		}

		public void defineSum(){
			if(containsVariable(summand,variableToSolveFor)){
				// Summand = a*x, a*b*x, a*x^b, a*b*x^c
				if(summand.getHeader().equals(CALC.MULTIPLY)){
					Function calcMulitply = (Function)summand;
					List<MathObject> multiplikanden = new ArrayList<>();
					for(int i = 0; i<calcMulitply.size();i++){

						if(containsVariable(calcMulitply.get(i),variableToSolveFor)){
							if(calcMulitply.get(i) instanceof Symbol){
								potenzDesSummanden=1;
							}
							else if(calcMulitply.get(i).getHeader().equals(CALC.POWER)){
								Function powerFunction = (Function)calcMulitply.get(i);
								//to do: nur Zahlen implementiert
								Integer integer1 = (Integer)powerFunction.get(1);
								potenzDesSummanden=integer1.intValue();
							}
						}
						else{
							multiplikanden.add(calcMulitply.get(i));
						}

					}
					Function multiplikand = new Function(CALC.MULTIPLY,CALC.D_ONE);
					for (MathObject multiplikandMathObject : multiplikanden) {
						multiplikand.add(multiplikandMathObject);
					}
					abc = multiplikand;
				}
				// Summand = x^a
				else if(summand.getHeader().equals(CALC.POWER)){
					Function powerFunction = (Function)summand;
					//to do: nur Zahlen implementiert
					Integer integer1 = (Integer)powerFunction.get(1);
					potenzDesSummanden = integer1.intValue();
					abc = new Integer(1);
				}
				// Summand = x
				else if (summand instanceof Symbol){
					potenzDesSummanden = 1;
					abc = new Integer(1);
				}
			}
			else{
				potenzDesSummanden = 0;
				abc = summand;
			}
		}



		public int getPotenzDesSummanden(){
			return  potenzDesSummanden;
		}

		@Override
		public int compareTo(Addends o) {
			return o.potenzDesSummanden - potenzDesSummanden;
		}

		@Override
		public String toString() {
			return abc.toString()+
					"*"+variableToSolveFor+"^" + potenzDesSummanden +
					"///=" + summand;
		}

		public void substitute(int substitutionFactor){
			if(potenzDesSummanden != 0) {
				potenzDesSummanden = potenzDesSummanden/substitutionFactor;
				Function function = new Function(CALC.POWER, new Symbol("y"), new Integer(potenzDesSummanden));
				summand = new Function(CALC.MULTIPLY, abc, function);
			}
		}
	}


}
