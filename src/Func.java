import java.util.ArrayList;


public class Func {
    	Type type;
    	Ident ident;
    	ArrayList<Param> params;
    	//ArrayList<Stm> stms;
    	SBlock sb;
    	public Func(Type type, Ident ident, ArrayList<Param> params, SBlock sb){
    		this.type = type;
        	this.ident = ident;
        	this.params= params;
        	//this.stms = stms;
        	this.sb = sb;
    	}
    	
}
