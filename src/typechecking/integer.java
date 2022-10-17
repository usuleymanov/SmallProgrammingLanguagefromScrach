package typechecking;

public class integer extends Data{
	public int i;
	public integer(int in){
		i = in;
	}
	public  String getType(){
		return "int";
	}
	
	public Object getValue(){
		return i;
	}
}
