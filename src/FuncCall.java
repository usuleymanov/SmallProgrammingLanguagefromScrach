import java.util.ArrayList;


public class FuncCall {
	Ident ident;
	ArrayList<Arg> args;
	
	public FuncCall(Ident i, ArrayList<Arg> a){
		ident = i;
		args = a;
	}
}
