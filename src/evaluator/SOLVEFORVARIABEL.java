package evaluator;

import core.CALC;
import evaluator.extend.*;
import exception.WrongParametersException;
import struct.*;
import struct.MathDouble;
import struct.MathInteger;
import struct.MathObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solves a variable for x or for y
 * y is for substitution
 */
//todo rework this class but implement set first

public class SOLVEFORVARIABEL implements FunctionEvaluator {


public SOLVEFORVARIABEL() {}



	List<Addends> addendsList;
	//Set set = new Set(CALC.SET);
    String variableToSolveFor = "x";



	@Override
	public MathObject evaluate(Function input) {

		//todo x=0

			if(input.get(0).getHeader().equals(CALC.SET)){
				return new MathSet(CALC.SET,CALC.EVALUATE(CALC.SOLVEFORVARIABEL.createFunction(((MathSet)input.get(0)).get(0))),CALC.EVALUATE(CALC.SOLVEFORVARIABEL.createFunction(((MathSet)input.get(0)).get(1))));
			}
            //bring everything to right side
            Relationship equation = (Relationship)input.get(0);
            Function leftSideOfEquation = new Function(CALC.ADD, equation.get(0), new Function(CALC.MULTIPLY,CALC.D_NEG_ONE,equation.get(1)));
            //simplify and expand
            Function simAndExpand = new Function(CALC.EXPAND,leftSideOfEquation);
            MathObject expanded = CALC.EVALUATE(simAndExpand);

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



				//check term for elementary algebra
				if(addendsList.size() == 2){
					if(addendsList.get(0).getPotenzDesSummanden()!=0&&addendsList.get(1).getPotenzDesSummanden()==0){
						return simpleEquation();
					}
				}
				//check term for polynomial equation
				else if(addendsList.size() == 3){
					if(addendsList.get(0).getPotenzDesSummanden()==2
							&& addendsList.get(1).getPotenzDesSummanden()==1
							&& addendsList.get(2).getPotenzDesSummanden()==0){
						//midnightformula?
						return   quadraticEquation();
						//set.addAll(setN.getParameters());
					}
					else{
						if(addendsList.get(0).getPotenzDesSummanden() / addendsList.get(1).getPotenzDesSummanden() == 2 && addendsList.get(2).getPotenzDesSummanden()==0){
							return substitution();
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
					return null;
				}



			}
			else{
				throw new WrongParametersException("SOLVEFORVARIABLE -> wrong number of parameters");

			}
			return null;
			//return set;
		//}
	}






	//todo use DEFINE class
	private MathSet substitution(){

		MathSet mathSetSubstitution = new MathSet(CALC.SET);
		int substitutionFactor = addendsList.get(1).getPotenzDesSummanden();
		Function function1 = new Function(CALC.POWER,new Symbol("x"),new MathInteger(substitutionFactor));
		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol("y"), function1);

		Function function2 = new Function(CALC.ADD);
		for (Addends addens:addendsList) {
			function2.add(addens.summand);
		}
		Relationship result2 = new Relationship(CALC.EQUAL,function2, CALC.D_ZERO);
		Function function3 = new Function(CALC.ADD);
		for (Addends addens:addendsList) {
			addens.substitute(substitutionFactor);
			function3.add(addens.summand);
		}
		variableToSolveFor = "y";
		mathSetSubstitution = quadraticEquation();

  		int size = mathSetSubstitution.getParameters().size();

		for (int i = 0; i<size;i++) {
			Function function5 = new Function(CALC.POWER,new MathInteger(substitutionFactor),CALC.D_NEG_ONE);
			Function function4 = new Function(CALC.POWER,((Relationship) mathSetSubstitution.getParameters().get(i)).get(1),function5);
			MathObject mhja = CALC.EVALUATE(function4);
			Relationship result3 = new Relationship(CALC.EQUAL,new Symbol("x"), mhja);
			CALC.ambiguityEvaluated = true;
			MathObject mhja2 = CALC.EVALUATE(result3);
			CALC.ambiguityEvaluated = false;
			if(!(mhja2 instanceof MathSet)){
				mhja2 = new MathSet(CALC.SET,mhja2);
			}

			mathSetSubstitution.addAll(((MathSet)mhja2).getParameters());
		}
		mathSetSubstitution.add(result1);
		return mathSetSubstitution;
	}

	private MathSet simpleEquation(){

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
				//MathObject mathObject = CALC.EVALUATE(rightSide);

				Relationship relationship = new Relationship(CALC.EQUAL, leftSide, rightSide);
				MathObject mathObject1 = CALC.EVALUATE(relationship);
				CALC.ambiguityEvaluated = true;
				MathObject mathObject2 = CALC.EVALUATE(mathObject1);
				CALC.ambiguityEvaluated = false;
				if(mathObject2 instanceof MathSet){
					return (MathSet)mathObject2;
				}
				else {
					return new MathSet(CALC.SET,mathObject2);
				}

				//simpleEq = (MathSet) mathObject2;


				//break;
			}
		}
		//return simpleEq;
		//addendsList.get(0)
	}

	private MathSet quadraticEquation(){
		MathSet quadraticEq = new MathSet(CALC.SET);
		//2*a
		Function m1 = new Function(CALC.MULTIPLY,new MathInteger(2), addendsList.get(0).getAbc());
		//(2*a)^-1
		Function m7 = new Function(CALC.POWER,m1,CALC.D_NEG_ONE);
		//b^2
		Function m2 = new Function(CALC.POWER, addendsList.get(1).getAbc(),new MathInteger(2));
		//-1*4*a*c
		Function m3 = new Function(CALC.MULTIPLY);
		m3.add(CALC.D_NEG_ONE);
		m3.add(new MathInteger(4));
		m3.add(addendsList.get(0).getAbc());
		m3.add(addendsList.get(2).getAbc());
		//b^2-1*4*a*c
		Function m4 = new Function(CALC.ADD,m2,m3);
		//(b^2-1*4*a*c)^0.5
		Function m5 = new Function(CALC.POWER,m4,new MathDouble(0.5));
		//-1*b
		Function m6 = new Function(CALC.MULTIPLY, CALC.D_NEG_ONE, addendsList.get(1).getAbc());

        //MathObject m8 = CALC.EVALUATE(m6);
        //MathObject m9 = CALC.EVALUATE(m5);
		//CALC.ambiguityEvaluated = true;
		//MathObject temp1 = CALC.EVALUATE(m9);
		//CALC.ambiguityEvaluated = false;
        Function m10 = new Function(CALC.ADD, m6,m5);
        //MathObject m11 = CALC.EVALUATE(m10);
        //MathObject m13 = CALC.EVALUATE(m7);
        Function m12 = new Function(CALC.MULTIPLY,m10,m7);


		//MathObject mathObject = CALC.EVALUATE(m12);
		Relationship result1 = new Relationship(CALC.EQUAL,new Symbol(variableToSolveFor), m12);
		MathObject mhhhhh = CALC.EVALUATE(result1);
		CALC.ambiguityEvaluated = true;
		MathObject temp1 = CALC.EVALUATE(mhhhhh);
		CALC.ambiguityEvaluated = false;
		if(temp1 instanceof MathSet) {
			return (MathSet) temp1;
		}
		else {
			return new MathSet(CALC.SET,temp1);
		}

		//Function function = new Function(CALC.SOLVEFORVARIABEL, result1);
		//CALC.EVALUATE(function);
		//quadraticEq.addAll(((MathSet)   (MathObject)function.get(0)).getParameters());
		//return quadraticEq;
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



	/**
	 *  nested Class that helps to split equation into its Addends
	 */
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
								MathInteger mathInteger1 = (MathInteger)powerFunction.get(1);
								potenzDesSummanden= mathInteger1.intValue();
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
					MathInteger mathInteger1 = (MathInteger)powerFunction.get(1);
					potenzDesSummanden = mathInteger1.intValue();
					abc = new MathInteger(1);
				}
				// Summand = x
				else if (summand instanceof Symbol){
					potenzDesSummanden = 1;
					abc = new MathInteger(1);
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
				Function function = new Function(CALC.POWER, new Symbol("y"), new MathInteger(potenzDesSummanden));
				summand = new Function(CALC.MULTIPLY, abc, function);
			}
		}
	}


}
