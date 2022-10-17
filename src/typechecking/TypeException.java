package typechecking;

public class TypeException extends Exception {

	public String msg;
	
	public TypeException(String m){
		msg = m;
	}
	
}
