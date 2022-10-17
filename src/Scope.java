import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import typechecking.Data;
import typechecking.TypeException;


public class Scope {
	Stack<HashSet< String>> scope = new Stack<HashSet<String>>();
	State state;
	
	public Scope (Stack<HashSet< String>> s, State st){
		scope = s;
		state = st;
	}
	//we entered new scope
	public void enterNewScope(){
		scope.push(new HashSet< String>());
	}
	
	//adds new identifier to the scope
	public void addIdentInScope(String ident, Data v) throws TypeException {
		if(scope.peek().contains(ident)){
			System.out.println("The identifier is already initialized in the current scope. Error.");
			throw new TypeException("The identifier is already initialized in the current scope.");
		}
		else {
			scope.peek().add(ident);
			if(state.state.containsKey(ident)){
				state.pushValue(ident, v);
			}
			else{
				state.addIdent(ident, v);
			}
		}
	}
	//we update value of predefined ident
	public void updateValueInScope(String ident, Data v)  throws TypeException{
		state.updateValue(ident, v);
		
	}
	//we leave scope
	public void leaveScope() throws TypeException{
		for(String name : scope.peek()){
			state.popValue(name);
		}
		scope.pop();
	}
	
	public Data get(String ident)throws TypeException{
		return state.get(ident);
	}
	
}
