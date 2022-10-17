import java.util.HashMap;
import java.util.Stack;

import typechecking.Data;
import typechecking.TypeException;
import typechecking.bool;

	public class State {
		HashMap<String,Stack< Data>> state = new HashMap<String, Stack<Data>>();
		public State(HashMap<String, Stack< Data>> s){
			state = s;
		}
		public Data get(String identifier)  throws TypeException{
			if(state.containsKey(identifier) && ! state.get(identifier).isEmpty() )
			// TODO Auto-generated method stub
				return state.get(identifier).peek();
			System.out.println(identifier + " does not exist");
			 throw new TypeException("The identifier is not initialized in the current scope.");

			//return new bool(false);
		}
		
	   public void updateValue(String ident, Data v) throws TypeException{
		   if(state.containsKey(ident) && ! state.get(ident).isEmpty()){
			   state.get(ident).pop();
			   state.get(ident).push(v);
		   }
		   else{
			   System.out.println(ident + " not initialized");
			   throw new TypeException("The identifier is not initialized in the current scope.");

		   }
	   }
	   
	   public void addIdent(String ident, Data v) throws TypeException{
		   Stack<Data> s = new Stack<Data>();
		   //adding default value automatically
		   s.push((Data) v);
		   state.put(ident, s);
	   }
//	   public void updateState(String ident){
//		   state.get(ident).pop();
//	   }
	   
	   public void pushValue(String ident, Data v) throws TypeException{
		 //  if(state.containsKey(ident))
			   state.get(ident).push(v);
		  // else
			   //System.out.println(ident+ " not previously initialized");
	   }
	   
	   public void popValue(String ident)throws TypeException{
		   state.get(ident).pop();
	   }
	   
	  
	}