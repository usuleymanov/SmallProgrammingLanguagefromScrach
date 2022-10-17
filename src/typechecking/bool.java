package typechecking;

public class bool extends Data{
	public boolean b;
	public bool(boolean bo){
		b = bo;
	}
	public  String getType(){
		return "bool";
	}
	
	public Object getValue(){
		return b;
	}
}
