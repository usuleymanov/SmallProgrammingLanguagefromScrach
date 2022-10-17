
public class SyntaxErrorException extends Exception{
	Token current;
	//for highlighting the previous token
	Token prev;
   String msg;
   public SyntaxErrorException(Token t1, Token t2, String m){
	   current = t1;
	   prev = t2;
	   msg = m;
   }
}
