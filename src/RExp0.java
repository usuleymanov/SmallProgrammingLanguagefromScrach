import BinaryComp.BinaryComparison;
import LogicalOperation.Logical;


public class RExp0{
	Exp exp;
	RExp0 rexp0;
	Logical lc;
	
	public RExp0(Logical l, Exp e, RExp0 r){
		exp = e;
		rexp0 = r;
		lc = l;
	}
}
