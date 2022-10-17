package typechecking;

public class floatdata extends Data{

	public double f;
	public floatdata(double fl){
		f = fl;
	}
	
	public  String getType(){
		return "float";
	}
	
	public Object getValue(){
		return f;
	}
}
