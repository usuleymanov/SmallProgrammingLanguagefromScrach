import java.util.ArrayList;


public class SBlock extends Stm{
	ArrayList<Stm> bs;
	public SBlock(ArrayList<Stm> s){
		bs = s;
	}
}
