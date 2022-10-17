import java.util.ArrayList;


public class SIf extends Stm{
	Exp0 exp;
	//ArrayList<Stm> BlockStms;
	Stm st;
	IRest ifrest;
	
	public SIf(Exp0 e, Stm s, IRest r){
		exp = e;
		//BlockStms = BlockStm;
		st = s;
		ifrest = r;
	}
}
