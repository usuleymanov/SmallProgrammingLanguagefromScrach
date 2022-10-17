import java.util.ArrayList;


public class SWhile extends Stm{
	Exp0 exp;
	//ArrayList<Stm> BlockStms;
	Stm stm;
	public SWhile(Exp0 e, Stm s){
		exp = e;
		//BlockStms = bs;
		stm = s;
	}
}
