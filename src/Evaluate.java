import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import typechecking.Data;
import typechecking.TypeException;
import typechecking.bool;
import typechecking.floatdata;
import typechecking.integer;
import typechecking.string;
import AddSubtract.Add;
import AddSubtract.Subtract;
import BinaryComp.Equal;
import BinaryComp.Greater;
import BinaryComp.GreaterEqual;
import BinaryComp.Less;
import BinaryComp.LessEqual;
import LogicalOperation.And;
import LogicalOperation.Or;
import MulDiv.Divide;
import MulDiv.Multiply;


public class Evaluate {
	
	static State s;
	static Scope sc;
	
    public Evaluate(){
    	s= new State(new HashMap<String, Stack<Data>>());
    	sc = new Scope(new Stack<HashSet< String>>(), s);
    }
	public void evaluateProgram(Program p) throws TypeException{
		for(Func f : p.functions)
			evaluateFunc(f);
	}
	
	public void evaluateFunc(Func f) throws TypeException{
		sc.enterNewScope();
		if(f.params != null){
			for(Param p: f.params){
				if(p.type instanceof INTTYPE )
					sc.addIdentInScope(p.ident.identifier, new integer(0));
				if(p.type instanceof BOOLTYPE )
					sc.addIdentInScope(p.ident.identifier, new bool(false));
				if(p.type instanceof FLOATTYPE )
					sc.addIdentInScope(p.ident.identifier, new floatdata(0.0));
				if(p.type instanceof STRINGTYPE )
					sc.addIdentInScope(p.ident.identifier, new string(""));
			}
		}
		for(Stm stm : f.sb.bs)
			evaluateStm(stm);
		//sc.leaveScope();
	}
	
	public void evaluateStm(Stm stm) throws TypeException{
		if(stm instanceof SDec){
			if(((SDec)stm).type instanceof INTTYPE ){
				sc.addIdentInScope(((SDec) stm).ident.identifier, new integer(0));
				if(((SDec) stm).exp != null){
					Data v = evalExp(((SDec) stm).exp);
					if(v.getType().equals("int"))
						sc.updateValueInScope(((SDec) stm).ident.identifier, v);
					else
						throw new TypeException("You cannot assign  " + (v.getType()) + " to integer ");
				}	
			}
			if(((SDec)stm).type instanceof FLOATTYPE ){
				sc.addIdentInScope(((SDec) stm).ident.identifier, new floatdata(0.0));
				if(((SDec) stm).exp != null){
					Data v = evalExp(((SDec) stm).exp);
					if(v.getType().equals("float"))
						sc.updateValueInScope(((SDec) stm).ident.identifier, v);
					else
						throw new TypeException("You cannot assign  " + (v.getType()) + " to float.");
				}	
			}
			if(((SDec)stm).type instanceof STRINGTYPE ){
				sc.addIdentInScope(((SDec) stm).ident.identifier, new string(""));
				if(((SDec) stm).exp != null){
					Data v = evalExp(((SDec) stm).exp);
					if(v.getType().equals("String"))
						sc.updateValueInScope(((SDec) stm).ident.identifier, v);
					else
						throw new TypeException("You cannot assign  " + (v.getType()) + " to float.");
				}	
			}
			if(((SDec)stm).type instanceof BOOLTYPE ){
				sc.addIdentInScope(((SDec) stm).ident.identifier, new bool(false));
				if(((SDec) stm).exp != null){
					Data v = evalExp(((SDec) stm).exp);
					if(v.getType().equals("bool"))
						sc.updateValueInScope(((SDec) stm).ident.identifier, v);
					else
						throw new TypeException("You cannot assign  " + (v.getType()) + " to float.");
				}	
			}
		}
		if(stm instanceof SAssign){
			Data v = evalExp(((SAssign) stm).exp);
			//System.out.println("Evaluating assignment");
			if(s.get(((SAssign) stm).ident.identifier).getType().equals(v.getType()))
				sc.updateValueInScope(((SAssign) stm).ident.identifier, v);
			else
				throw new TypeException("You cannot assign  " + (v.getType()) + " to " 
			+ s.get(((SAssign) stm).ident.identifier).getType() );

		}
		if(stm instanceof SBlock){
			sc.enterNewScope();
			for(Stm st : ((SBlock) stm).bs)
				evaluateStm(st);
			sc.leaveScope();
		}
		if(stm instanceof SIf){
			Data d = evalExp(((SIf) stm).exp);
			if(d.getType().equals("bool")){
				if((boolean) (d.getValue())){
					evaluateStm(((SIf) stm).st);
				}
				else if( ! (boolean) (d.getValue()) && ((SIf) stm).ifrest != null){
					evaluateStm(((SIf) stm).ifrest.stm);
				}
			}
			else
				throw new TypeException("Expected boolean in if" );
		}
		
		if(stm instanceof SWhile){
			Data d = evalExp(((SWhile) stm).exp);
			if(d.getType().equals("bool")){
				while((boolean)d.getValue()){
					evaluateStm(((SWhile) stm).stm);
					d = evalExp(((SWhile) stm).exp);
				}
			}
			else
				throw new TypeException("Expected boolean in while" );
		}
		
		if(stm instanceof SFor){
			evaluateStm(((SFor) stm).init);
			Data d = evalExp(((SFor) stm).exp);
			if(d.getType().equals("bool")){
				while((boolean)d.getValue()){
					evaluateStm(((SFor) stm).stm);
					evaluateStm(((SFor) stm).increment);
					d = evalExp(((SFor) stm).exp);
				}
			}
			else
				throw new TypeException("Expected boolean in for" );
		}
		if(stm instanceof SExp){
			Data d = evalExp(((SExp) stm).exp);
		}
		
	}
	Data evalExp(Expression e) throws TypeException{
		
		if(e instanceof Exp0){
			Data firstpart = evalExp(((Exp0) e).exp);
			if(((Exp0) e).rexp0 == null){
				return firstpart; 
			}
			Exp0 se = new Exp0(((Exp0) e).rexp0.exp, ((Exp0) e).rexp0.rexp0);
			Data secondpart = evalExp(se);
			if(((Exp0) e).rexp0.lc instanceof And){
				//if firstpart and secondpart have different types we raise typeexception
				if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("bool")){
					if(  ( Boolean) firstpart.getValue() && (Boolean) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}
			if(((Exp0) e).rexp0.lc instanceof Or){
				//if firstpart and secondpart have different types we raise typeexception
				if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("bool")){
					if(  ( Boolean) firstpart.getValue() || (Boolean) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}

			}
		if(e instanceof Exp){
			Data firstpart = evalExp(((Exp) e).exp1);
			if(((Exp) e).rexp == null){
				return firstpart; 
			}
			Exp se = new Exp(((Exp) e).rexp.exp1, ((Exp) e).rexp.rexp);
			Data secondpart = evalExp(se);
			if(((Exp) e).rexp.bc instanceof Equal){
				//if firstpart and secondpart have different types we raise typeexception
				if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
						 )){
					if( ( Integer) firstpart.getValue() == (Integer) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("float")){
					if( ( Double) firstpart.getValue() == (Double) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("String")){
					if( ( boolean) firstpart.getValue().equals(secondpart.getValue()))
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("bool")){
					if( ( boolean) firstpart.getValue().equals(secondpart.getValue()))
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}
			if(((Exp) e).rexp.bc instanceof Greater){
				if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
						 )){
					if( ( Integer) firstpart.getValue() > (Integer) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("float")){
					if( ( Double) firstpart.getValue() > (Double) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}
			if(((Exp) e).rexp.bc instanceof GreaterEqual){
				if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
						 )){
					if( ( Integer) firstpart.getValue() >= (Integer) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("float")){
					if( ( Double) firstpart.getValue() >= (Double) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}
			if(((Exp) e).rexp.bc instanceof Less){
				if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
						 )){
					if( ( Integer) firstpart.getValue() < (Integer) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("float")){
					if( ( Double) firstpart.getValue() < (Double) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}
			if(((Exp) e).rexp.bc instanceof LessEqual){
				if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
						 )){
					if( ( Integer) firstpart.getValue() <= (Integer) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else if( firstpart.getType().equals(secondpart.getType()) && firstpart.getType().equals("float")){
					if( ( Double) firstpart.getValue() <= (Double) secondpart.getValue())
						return new bool(true);
					return new bool(false);
				}
				else{
					throw new TypeException("You cannot compare " + firstpart.getType() + " and " 
							+ secondpart.getType());
				}
			}	
		}
		
		if(e instanceof Exp1){
			Data firstpart = evalExp(((Exp1) e).exp2);
			if(((Exp1) e).rexp1 == null){
				return firstpart; 
			}
			Exp1 se = new Exp1(((Exp1) e).rexp1.exp2, ((Exp1) e).rexp1.rexp1);
			Data secondpart = evalExp(se);
			
			if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
					 || firstpart.getType().equals("float"))){
				if(((Exp1) e).rexp1.a instanceof Add){
					if(firstpart.getType().equals("int"))
						return new integer((int)firstpart.getValue() + (int)secondpart.getValue());
					return new floatdata((double)firstpart.getValue() + (double)secondpart.getValue());
				}
				if(((Exp1) e).rexp1.a instanceof Subtract){
					if(firstpart.getType().equals("int"))
						return new integer((int)firstpart.getValue() - (int)secondpart.getValue());
					return new floatdata((double)firstpart.getValue() - (double)secondpart.getValue());
				}
			}
			else
				throw new TypeException("You cannot add " + firstpart.getType() + " and " 
						+ secondpart.getType());
		}
		
		if(e instanceof Exp2){
			Data firstpart = evalExp(((Exp2) e).exp3);
			if(((Exp2) e).rexp2 == null){
				return firstpart; 
			}
			Exp2 se = new Exp2(((Exp2) e).rexp2.exp3, ((Exp2) e).rexp2.rexp2);
			Data secondpart = evalExp(se);
			if(firstpart.getType().equals(secondpart.getType()) && (firstpart.getType().equals("int")
					 || firstpart.getType().equals("float"))){
				if(((Exp2) e).rexp2.md instanceof Divide){
					if(firstpart.getType().equals("int"))
						return new integer((int)firstpart.getValue() / (int)secondpart.getValue());
					return new floatdata((double)firstpart.getValue() / (double)secondpart.getValue());
			
				}
				if(((Exp2) e).rexp2.md instanceof Multiply){
					if(firstpart.getType().equals("int"))
						return new integer((int)firstpart.getValue() * (int)secondpart.getValue());
					return new floatdata((double)firstpart.getValue() * (double)secondpart.getValue());
				}
			}
			else
				throw new TypeException("You cannot multiply " + firstpart.getType() + " and " 
						+ secondpart.getType());
		}
		if(e instanceof Exp3){
			if(e instanceof BExp3)
				return evalExp(((BExp3) e).exp);
			if(e instanceof NExp3){
				if(((NExp3) e).nt instanceof INT)
					return new integer((int)((NExp3) e).nt.getValue());
				return new floatdata(((NExp3) e).nt.getValue());
			}
			if(e instanceof IdExp3)
				return s.get(((IdExp3) e).id.identifier);
			if(e instanceof BOOLEAN)
				return new bool(((BOOLEAN) e).b);
			if(e instanceof STRING)
				return new string(((STRING) e).s);
		}
		return new bool(false);
	}
   public String printVlaues() throws TypeException{
	   String res = "";
	   for(String ident : s.state.keySet()){
		   System.out.println("Value of " + ident + " is " + sc.get(ident).getValue());
		   res+= "Value of " + ident + " is " + sc.get(ident).getValue()+"\n";
	   }
	   return res;
   }
	

}
