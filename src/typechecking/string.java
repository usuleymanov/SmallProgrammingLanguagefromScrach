package typechecking;

public class string extends Data{
	String s;
	public string(String st){
		s = st;
	}
	
	public  String getType(){
		return "String";
	}
	
	public Object getValue(){
		return s;
	}
}
