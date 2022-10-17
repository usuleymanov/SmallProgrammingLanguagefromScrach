
public class SDec extends Stm{
   Type type;
   Ident ident;
   Exp0 exp;
   
   public SDec(Type type, Ident ident, Exp0 exp){
	   this.type = type;
	   this.ident = ident;
	   this.exp = exp;
   }
}
