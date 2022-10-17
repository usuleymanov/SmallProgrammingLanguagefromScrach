import java.util.ArrayList;
import java.util.Iterator;

import AddSubtract.Add;
import AddSubtract.AddSubtract;
import AddSubtract.Subtract;
import BinaryComp.BinaryComparison;
import BinaryComp.Equal;
import BinaryComp.Greater;
import BinaryComp.GreaterEqual;
import BinaryComp.Less;
import BinaryComp.LessEqual;
import LogicalOperation.And;
import LogicalOperation.Logical;
import LogicalOperation.Or;
import MulDiv.Divide;
import MulDiv.MulDiv;
import MulDiv.Multiply;

//I have made several changes in bnf rules and therefore in parser:
//I changed the structure of "if" (new bnf rule for irest, namely irest can be null or stm)
//I created separate class "SBlock" (Block Statement) which extends Stm. Thus,  every stm can
//be block statement. This allows  bnf rules to be more compact and complete. 
//Changes in the bnf rules:
//1)Func ::= Type Ident "(" [Params] ")" SBlock
/*2) SBlock. stm ::= "{" Stms "}"
 *3) SIf. stm ::= "if" "(" Exp ")" Stm IRest
 *4) IRest ::= "else" Stm | null
 *5) SWhile ::= "while" "(" Exp ")" Stm
 * 
 */
public class Parser {
	//list of tokens
	ArrayList<Token> tokens; // = Lexer.lex("fg + oi");
	Iterator<Token> itr;
	Token next;
	Token before;
	public Parser(ArrayList<Token> t){
		tokens = t;
		itr = tokens.iterator();
		next = (Token)itr.next();
		before = next;
	}
	
	Program parsePrg() throws SyntaxErrorException{
		ArrayList<Func> funcs = parseFuncs();
		return new Program(funcs);
	}
	
	ArrayList<Func> parseFuncs() throws SyntaxErrorException{
		Func f = parseFunc();
		ArrayList<Func> funcs = new ArrayList<Func>();
		funcs.add(f);
		while(f != null && itr.hasNext()){
			f = parseFunc();
			funcs.add(f);
		}
		
		return funcs;
	}
	
	void ignore(String s) throws SyntaxErrorException{
		if(next.getData().equals(s)){
			//System.out.println("Ignored " + next.getData());
			if(itr.hasNext()){
				before = next;
				next = itr.next();
			}
		}
		else{
			//exception
			//System.out.println("Syntax ignore error in " + next.getPosition() + " token " + next.getData() + " is incoorrect");
			String msg = "Syntax ignore error in " + next.getPosition() + ". Expected token is " + s;
			throw new SyntaxErrorException(next, before , msg);
			//next = itr.next();
		}
	}
	
	Func parseFunc() throws SyntaxErrorException{
		Type t = parseType();
		if(t == null){
			//System.out.println("Function type not specified in" + next.getPosition());
			String msg = "Function type not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		Ident i = parseIdent();
		if( i == null){
			//exception
			//System.out.println("Function identifier not specified in" + next.getPosition());
			String msg = "Function identifier not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		ignore("(");
		ArrayList<Param> p = parseParams();
		ignore(")");
//		ignore("{");
//		ArrayList<Stm> s = parseStms();
//		ignore("}");
		SBlock bs = parseBlockStms();
		
		return new Func(t, i, p, bs);	
	}
	
	Type parseType(){
		if(next.getType() == (TokenType.TYPEINT)){
			before = next;
			if(itr.hasNext()){
				next = itr.next();
			}
			return new INTTYPE();
		}
		else if(next.getType() == (TokenType.TYPEFLOAT)){
			before = next;
			if(itr.hasNext())
				next = itr.next();
			return new FLOATTYPE();
		}
		else if(next.getType() == (TokenType.TYPESTRING)){
			before = next;
			if(itr.hasNext())
				next = itr.next();
			return new STRINGTYPE();
		}
		else if(next.getType() == (TokenType.TYPEBOOL)){
			before = next;
			if(itr.hasNext())
				next = itr.next();
			return new BOOLTYPE();
		}
		return null;
	}
	
	Ident parseIdent(){
		if(next.getType() == (TokenType.IDENT) ){
			String s = next.getData();
			//System.out.println("parsed ident " + next.getData() );
			before = next;
			if(itr.hasNext())
				next = itr.next();
			
			return new Ident(s);
		}
		return null;
	}
	
	ArrayList<Param> parseParams() throws SyntaxErrorException{
		if(next.getType() == TokenType.RIGHTBRACKET){
			//no parameters for function
			//System.out.println("no parameters for function");
			return null;
		}
		Param p = parseParam();
		ArrayList<Param> ps  = new ArrayList<Param>();
		ps.add(p);
		while( next.getType() == TokenType.COMMA){
			ignore(",");
			p = parseParam();
			if(p == null){
				//System.out.println("Syntax error in" + next.getPosition());
				String msg = "Parameter expected in " + next.getPosition();
				throw new SyntaxErrorException(next, before , msg);
			}
			ps.add(p);
		}
		return ps;
	}
	
	Param parseParam() throws SyntaxErrorException{
		Type t = parseType();
		
		Ident i = parseIdent();
		
		if(t == null && i == null ){
			return null;
		}
		if(t == null){
			//exception
			//System.out.println("Parameter type not specified" + next.getPosition());
			String msg = "Type expected in " + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		if(i == null){
			//exception
			//System.out.println("Parameter ident not specified" + next.getPosition());
			String msg = "Identifier expected in " + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		return new Param(t, i);
	}
	
//	ArrayList<Stm> parseStms() throws SyntaxErrorException{
//		ArrayList<Stm> stms = new ArrayList<Stm>();
//		Stm s = parseStm();
//		while(s != null){
//			stms.add(s);
//			s = parseStm();
//		}
//		
//		return stms;
//	}
	
	Stm parseStm() throws SyntaxErrorException{
		
		if(next.getType() == TokenType.IDENT){
			//System.out.println("Parsed aassignment");
			Ident i = parseIdent();
			ignore("=");
			Exp0 e = parseExp0();
			ignore(";");
			return new SAssign(i,e);
		}
		else if(next.getType() == TokenType.TYPESTRING 
				|| next.getType() == TokenType.TYPEFLOAT || next.getType() == TokenType.TYPEINT || next.getType() == TokenType.TYPEBOOL ){
			Type t = parseType();
			Ident i = parseIdent();
			Exp0 e = null;
			if(next.getType() == TokenType.ASSIGN){
				ignore("=");
				e = parseExp0();
			}
			ignore(";");
			return new SDec(t, i, e);
		}
		else if(next.getType() == TokenType.IF){
			ignore("if");
			ignore("(");
			Exp0 e = parseExp0();
			ignore(")");
			//ArrayList<Stm> blockStms = new ArrayList<Stm>();
			//blockStms = parseBlockStms();
			Stm s = parseStm();
			IRest ir = parseIRest();
			return new SIf(e, s, ir);
		}
		else if(next.getType() == TokenType.WHILE){
			ignore("while");
			ignore("(");
			Exp0 e = parseExp0();
			ignore(")");
//			ArrayList<Stm> blockStms = new ArrayList<Stm>();
//			blockStms = parseBlockStms();
			Stm s = parseStm();
			return new SWhile(e, s);
		}
		else if(next.getType() == TokenType.FOR){
			ignore("for");
			ignore("(");
			Ident i = parseIdent();
			if( i == null){
				String msg = "Identifier missing in" + next.getPosition();
				throw new SyntaxErrorException(next, before , msg);
			}
			Exp0 e = null;
			// assigning some value to i is optional
			if(next.getType() != TokenType.SEMICOLON){
				ignore("=");
				e = parseExp0();
			}
			SAssign sa1 = new SAssign(i, e);
			ignore(";");
			Exp0 e1 = parseExp0();
			ignore(";");
			i = null;
			e = null;
			//increment part is optional
			if(next.getType() != TokenType.RIGHTBRACKET){
				i = parseIdent();
				if(i == null){
					String msg = "Identifier missing in" + next.getPosition();
					throw new SyntaxErrorException(next, before , msg);
				}
				ignore("=");
				e = parseExp0();
				if( e == null){
				    //exception
					String msg = "Expression is expected in" + next.getPosition();
					throw new SyntaxErrorException(next, before , msg);
				}
			}
			SAssign sa2 = new SAssign(i, e);
			ignore(")");
//			ArrayList<Stm> blockStms = new ArrayList<Stm>();
//			blockStms = parseBlockStms();
			Stm s = parseStm();
			return new SFor(sa1, e1, sa2, s);
		}
		else if(next.getType() == TokenType.RETURN){
			ignore("return");
			Exp e = parseExp();
			ignore(";");
			return new SReturn(e);
		}
		else if(next.getType() == TokenType.BREAKSIGN){
			ignore("break");
			ignore(";");
			return new SBreak();
		}
		else if(next.getType() == TokenType.CONTINUE){
			ignore("contine");
			ignore(";");
			return new SContinue();
		}
		
		else if(next.getType() == TokenType.LEFTCURLYBRACKET){
			//ArrayList<Stm> bs = new ArrayList<Stm>();
			SBlock s = parseBlockStms();
			return s;
		}
		Exp0 e = parseExp0();
		
		if(e == null){
			return null;
		}
		ignore(";");
		return new SExp(e);
	}
	
	SBlock parseBlockStms() throws SyntaxErrorException{
		ArrayList<Stm> bs = new ArrayList<Stm>();
		ignore("{");
		Stm s = null;
		s = parseStm();
		bs.add(s);
//		if(itr.hasNext())
//			s = parseStm();
		while( itr.hasNext() && next.getType() != TokenType.RIGHTCURLYBRACKET && s != null ){
			s = parseStm();
			bs.add(s);
			//s = parseStm();
			//bs = parseStms();
			//System.out.println("parsed bs");
			//ignore("}");
			//return bs;
		}
		//Stm s = parseStm();
		//bs.add(s);
//		while(next.getData().equals("}") ){
//			System.out.println("} nan tapdim");
//			if(itr.hasNext())
//				next = itr.next();
//			else break;
//		}
		ignore("}");
		return new SBlock(bs);
	}
	
	IRest parseIRest() throws SyntaxErrorException{
		if(next.getType() == TokenType.ELSE){
			ignore("else");
//			ArrayList<Stm> bs = new ArrayList<Stm>();
//			bs = parseBlockStms();
			Stm s = parseStm();
			return new IRest(s);
		}
//		if(next.getType() == TokenType.ELSEIF){
//			ignore("else if");
//			ignore("(");
//			Exp e = parseExp();
//			ignore(")");
//			ArrayList<Stm> bs = new ArrayList<Stm>();
//			bs = parseBlockStms();
//			IRest ir = parseIRest();
//			return new RElseIf(bs, e, ir);
//		}
		return null;
	}
	
	Exp parseExp() throws SyntaxErrorException{
		Exp1 e1 = parseExp1();
		RExp re = parseRExp();
		if(e1 == null && re == null){
			return null;
		}
		return new Exp(e1, re);
	}
	
	Exp0 parseExp0() throws SyntaxErrorException{
		Exp e = parseExp();
		RExp0 re = parseRExp0();
		if(e == null && re == null){
			return null;
		}
		return new Exp0(e, re);
	}
	RExp0 parseRExp0() throws SyntaxErrorException{
		Logical lc = parseLogical();
		Exp e = parseExp();
		RExp0 r = null;
		if(lc != null && e == null){
			//exception
			//System.out.println("Syntax error in" + next.getPosition());
			String msg = "Expression is expected in" + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		if( lc != null){
			r = parseRExp0();
		}
		if (lc == null || e == null){
			return null;
		}
		return new RExp0(lc, e, r);
	}

	
	RExp parseRExp() throws SyntaxErrorException{
		BinaryComparison bc = parseBinaryComparison();
		Exp1 e1 = parseExp1();
		RExp r = null;
		if(bc != null && e1 == null){
			//exception
			//System.out.println("Syntax error in" + next.getPosition());
			String msg = "Expression is expected in" + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		if( bc != null){
			r = parseRExp();
		}
		if (bc == null || e1 == null){
			return null;
		}
		return new RExp(bc, e1, r);
	}
	
	BinaryComparison parseBinaryComparison() throws SyntaxErrorException{
		if(next.getType() == TokenType.GREATER){
			ignore(">");
			return new Greater();
		}
		if(next.getType() == TokenType.GREATEREQUAL){
			ignore(">=");
			return new GreaterEqual();
		}
		if(next.getType() == TokenType.LESS){
			ignore("<");
			return new Less();
		}
		if(next.getType() == TokenType.LESSEQUAL){
			ignore("<=");
			return new LessEqual();
		}
		if(next.getType() == TokenType.EQUAL){
			ignore("==");
			return new Equal();
		}
		return null;
	}
	Logical parseLogical() throws SyntaxErrorException{
		if(next.getType() == TokenType.BINARYAND){
			ignore("&&");
			return new And();
		}
		if(next.getType() == TokenType.BINARYOR){
			ignore("||");
			return new Or();
		}
		return null;
	}
	
	Exp1 parseExp1() throws SyntaxErrorException{
		Exp2 e2 = parseExp2();
		RExp1 r1 = parseRExp1();
		if(e2 == null && r1 == null){
			return null;
		}
		return new Exp1(e2, r1);
	}
	
	RExp1 parseRExp1() throws SyntaxErrorException{
		AddSubtract a = parseAddSubtract();
		Exp2 e2 = parseExp2();
		RExp1 r1 = null;
		if(a != null && e2 == null){
			//exception
			//System.out.println("Syntax error in" + next.getPosition());	
			String msg = "Expression is expected in" + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		if( a != null){
			r1 = parseRExp1();
		}
		if (a == null || e2 == null){
			return null;
		}
		return new RExp1(a, e2, r1);
	}
	
	AddSubtract parseAddSubtract() throws SyntaxErrorException{
		if(next.getType() == TokenType.PLUS){
			ignore("+");
			return new Add();
		}
		if(next.getType() == TokenType.MINUS){
			ignore("-");
			return new Subtract();
		}
		return null;
	}
	
	Exp2 parseExp2() throws SyntaxErrorException{
		Exp3 e3 = parseExp3();
		RExp2 r2 = parseRExp2();
		if(e3 == null && r2 == null){
			return null;
		}
		return new Exp2(e3, r2);
	}
	
	RExp2 parseRExp2() throws SyntaxErrorException{
		MulDiv m = parseMulDiv();
		Exp3 e3 = parseExp3();
		RExp2 r2 = null;
		if(m != null && e3 == null){
			//exception
			//System.out.println("Syntax error in" + next.getPosition());
			String msg = "Expression is expected in" + next.getPosition();
			throw new SyntaxErrorException(next, before , msg);
		}
		if( m != null){
			r2 = parseRExp2();
		}
		if (m == null || e3 == null){
			return null;
		}
		return new RExp2(m, e3, r2);
	}
	
	
	MulDiv parseMulDiv() throws SyntaxErrorException{
		if(next.getType() == TokenType.MULTIPLY){
			ignore("*");
			return new Multiply();
		}
		if(next.getType() == TokenType.DIVIDE){
			ignore("/");
			return new Divide();
		}
		return null;
	}
	
	Exp3 parseExp3() throws SyntaxErrorException{
		if(next.getType() == TokenType.FLOATVALUE || next.getType() == TokenType.NUMBER ){
			NumTypes nt = parseNumTypes();
			return new NExp3(nt);
		}
		if(next.getType() == TokenType.IDENT){
			//FuncCall f = parseFuncCall();
			Ident i = parseIdent();
			//parsing functionCall
			if(next.getType() == TokenType.LEFTBRACKET){
				ignore("(");
				ArrayList<Arg> args = new ArrayList<Arg>();
				args = parseArgs();
				ignore(")");
				FuncCall f =  new FuncCall(i, args);
				return new FExp3(f);
			}
			//return identifier within expression
			return new IdExp3(i);	
		}
		if(next.getType() == TokenType.LEFTBRACKET){
			ignore("(");
			Exp e = parseExp();
			ignore(")");
			return new BExp3(e);
		}
		if(next.getType() == TokenType.BOOLVALUE){
			if(next.getData().equals("true")){
				ignore("true");
				return new BOOLEAN(true);
			}
			ignore("false");
			return new BOOLEAN(false);
		}
		
		if(next.getType() == TokenType.STRINGVALUE){
			String s = next.getData();
			ignore(s);
			return new STRING(s);
		}

		
		return null;
	}
	
	NumTypes parseNumTypes(){
		if(next.getType() == TokenType.NUMBER){
			int i = Integer.parseInt(next.getData());
			before = next;
			next = itr.next();
			return new INT(i);
		}
		double d = Double.parseDouble(next.getData());
		before = next;
		next = itr.next();
		return new FLOAT(d);
	}
	
/*	FuncCall parseFuncCall() throws SyntaxErrorException{
		Ident i = parseIdent();
		ignore("(");
		ArrayList<Arg> args = new ArrayList<Arg>();
		args = parseArgs();
		ignore(")");
		return new FuncCall(i, args);
	}
	*/
	
	ArrayList<Arg> parseArgs() throws SyntaxErrorException{
		if(next.getType() == TokenType.RIGHTBRACKET){
			//no parameters for function
			//next = itr.next();
			return null;
		}
		Arg a = parseArg();
		ArrayList<Arg> args  = new ArrayList<Arg>();
		args.add(a);
		//System.out.println("Expecting asrg");
		while( next.getType() == TokenType.COMMA){
			ignore(",");
			//System.out.println("Expecting asrg");
			a = parseArg();
			if(a == null){
				String msg = "Argument is missing in" + next.getPosition();
				throw new SyntaxErrorException(next, before , msg);
			}
			args.add(a);
		}
		return args;
	}
	
	Arg parseArg() throws SyntaxErrorException{
		Exp e = parseExp();
		if(e == null)
			return null;
		return new Arg(e);
	}
	//Stm parseStm(){ if(next.getData().equals("if")){
//ignore("if"); ignore("("); Exp e = parseExp(); ignore(")"); Stm s = parseStm();return new SIf(e,s);}
/*	if( next is Ident){
 * Ident id = 
 * ignore(=)
 * Exp e = parseExp();
 * return new Asignment(id, e);


*/
//}
	//next = tokens.getfirst();
	
	//if ignore fails store exception
	
}
