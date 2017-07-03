package evaluator;

import com.sun.org.apache.regexp.internal.RE;
import core.CALC;
import core.Engine;
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

		//first step is to check if input contains plusminus
		//ersetze plusminus
		if(containsPLUSMINUS(input)){
			solveForPlusMinus(input);
			return Engine.solutionset;
		}
		else{


            //bring everything to right side
            Relationship equation = (Relationship)input.get(0);
            Function leftSideOfEquation = new Function(CALC.ADD, equation.get(0), new Function(CALC.MULTIPLY,CALC.D_NEG_ONE,equation.get(1)));
            //simplify and expand
            Function simAndExpand = new Function(CALC.EXPAND,leftSideOfEquation);
            MathObject expanded = CALC.SYM_EVAL(simAndExpand);

			if(containsVariable(expanded,"y")){
				variableToSolveFor = "y";
			}
			else {
				variableToSolveFor = "x";
			}

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


				solutionset = Engine.solutionset;
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

			return Engine.solutionset;
		}
	}
	private void substitution(){
		int substitutionFactor = addendsList.get(1).getPotenzDesSummanden();
		Function function1 = new Function(CALC.POWER,new Symbol("x"),new Integer(substitutionFactor));
		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol("y"), function1);

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
		quadraticEquation();

  		int size = Engine.solutionset.getParameters().size();

		for (int i = 0; i<size;i++) {

			Function function5 = new Function(CALC.POWER,new Integer(substitutionFactor),CALC.D_NEG_ONE);
			Function function4 = new Function(CALC.POWER,((Relationship)Engine.solutionset.getParameters().get(i)).get(1),function5);
			Relationship result3 = new Relationship(CALC.EQUAL,new Symbol("x"), function4);
			Function function = new Function(CALC.SOLVEFORVARIABEL,result3);
			MathObject resultMathObject1 = CALC.SYM_EVAL(function);

		}
		solutionset.add(result1);
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
				if(containsPLUSMINUS(relationship)){
					Function function = new Function(CALC.SOLVEFORVARIABEL,relationship);
					CALC.SYM_EVAL(function);
				}
				else{
					solutionset.add(relationship);
				}

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

        MathObject m8 = CALC.SYM_EVAL(m6);
        MathObject m9 = CALC.SYM_EVAL(m5);

        Function m10 = new Function(CALC.ADD, m8,m9);
        MathObject m11 = CALC.SYM_EVAL(m10);
        MathObject m13 = CALC.SYM_EVAL(m7);
        Function m12 = new Function(CALC.MULTIPLY,m10,CALC.SYM_EVAL(m7));



		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol(variableToSolveFor), m12);
		Function function = new Function(CALC.SOLVEFORVARIABEL, result1);
		CALC.SYM_EVAL(function);

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




	public void plusminus(Function input){

	}

	public MathObject findPLUSMINUSParent(MathObject mathObject){
		if(mathObject.getHeader().equals(CALC.PLUSMINUS)){
			return mathObject;
		}
		else if(mathObject instanceof Function){
			for(int i = 0; i<((Function) mathObject).size(); i++) {
				MathObject mathObject1 = ((Function) mathObject).get(i);
				if(mathObject1 instanceof  Function){
					if(((Function) mathObject1).getHeader().equals(CALC.PLUSMINUS)){
						return mathObject;
					}
				}
			}
			for(int i = 0; i<((Function) mathObject).size(); i++) {

				if (containsPLUSMINUS(((Function) mathObject).get(i))) {
					return findPLUSMINUSParent(((Function) mathObject).get(i));
				}
			}
		}
		else if(mathObject instanceof Relationship){
			for(int i = 0; i<((Relationship) mathObject).size(); i++) {
				MathObject mathObject1 = ((Relationship) mathObject).get(i);
				if(mathObject1 instanceof  Function){
					if(((Function) mathObject1).getHeader().equals(CALC.PLUSMINUS)){
						return mathObject;
					}
				}
			}
			for(int i = 0; i<((Relationship) mathObject).size(); i++) {
				if (containsPLUSMINUS(((Relationship) mathObject).get(i))) {
					return findPLUSMINUSParent(((Relationship) mathObject).get(i));
				}
			}
		}
		return null;
	}

	public boolean containsPLUSMINUS(MathObject mathObject){
		if(mathObject instanceof Function){
			if(mathObject.getHeader().equals(CALC.PLUSMINUS)){
				return true;
			}
			for(int i = 0; i<((Function) mathObject).size(); i++) {
				if (containsPLUSMINUS(((Function) mathObject).get(i))) {
					return true;
				}
			}
		}
		else if(mathObject instanceof Relationship){
			for(int i = 0; i<((Relationship) mathObject).size(); i++) {
				if (containsPLUSMINUS(((Relationship) mathObject).get(i))) {
					return true;
				}
			}
		}
		return false;
	}

	public void solveForPlusMinus(Function input){
		MathObject inuptplus = input.cloneMathObject();
		MathObject plus = findPLUSMINUSParent(inuptplus);
		if(plus instanceof Function){
			for (int i = 0; i< ((Function)plus).size();i++ ) {
				if(containsPLUSMINUS(((Function)plus).get(i))){
					((Function)plus).set(i,((Function)((Function)plus).get(i)).get(0));
				}
			}
		}
		else if(plus instanceof Relationship){
			for (int i = 0; i< ((Relationship)plus).size();i++ ) {
				if(containsPLUSMINUS(((Relationship)plus).get(i))){
					((Relationship)plus).set(i,((Function)((Relationship)plus).get(i)).get(0));
				}
			}
		}
		//Function equal1 = new Function(CALC.EQUAL,inuptplus,CALC.D_ZERO);
		//Function functionPlus = new Function(CALC.SOLVEFORVARIABEL,equal1);
		//CALC.SYM_EVAL(functionPlus);
		CALC.SYM_EVAL(inuptplus);

		MathObject inputminus = input.cloneMathObject();
		MathObject minus = findPLUSMINUSParent(inputminus);
		if(minus instanceof Function){
			for (int i = 0; i< ((Function)minus).size();i++ ) {
				if(containsPLUSMINUS(((Function)minus).get(i))){

					((Function)minus).set(i,CALC.MULTIPLY.createFunction(CALC.NEG_ONE, ((Function)((Function)minus).get(i)).get(0)));
				}
			}
		}
		else if(minus instanceof Relationship){
			for (int i = 0; i< ((Relationship)minus).size();i++ ) {
				if(containsPLUSMINUS(((Relationship)minus).get(i))){
					((Relationship)minus).set(i,CALC.MULTIPLY.createFunction(CALC.NEG_ONE, ((Function)((Relationship)minus).get(i)).get(0)));
				}
			}
		}
		CALC.SYM_EVAL(inputminus);
		//Function equal2 = new Function(CALC.EQUAL,inputminus,CALC.D_ZERO);
		//Function fuunctionMinus = new Function(CALC.SOLVEFORVARIABEL,equal2);
        //CALC.SYM_EVAL(fuunctionMinus);
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
