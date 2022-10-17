import BinaryComp.BinaryComparison;


public class RExp{
	Exp1 exp1;
	RExp rexp;
	BinaryComparison bc;
	
	public RExp(BinaryComparison b, Exp1 e, RExp r){
		exp1 = e;
		rexp = r;
		bc = b;
	}
}
