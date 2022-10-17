import java.util.ArrayList;


public class SFor extends Stm {
	SAssign init;
	SAssign increment;
	Exp0 exp;
	//ArrayList<Stm> blockStms;
	Stm stm;
	
	public SFor(SAssign s1, Exp0 e, SAssign s2,Stm s ){
		init = s1;
		exp = e;
		increment = s2;
		//blockStms = bs;
		stm = s;
	}
}
