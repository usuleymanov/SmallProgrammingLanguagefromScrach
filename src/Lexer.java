import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

	public static ArrayList<Token> lex(String input) {
		// The tokens to return
		ArrayList<Token> tokens = new ArrayList<Token>();

		// Lexer logic begins here
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for (TokenType tokenType : TokenType.values())
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

		// Begin matching tokens
		Matcher matcher = tokenPatterns.matcher(input);
		while (matcher.find()) {
			if (matcher.group(TokenType.NUMBER.name()) != null) {
				tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name()), matcher.start()));
				continue;
			}
//			} else if (matcher.group(TokenType.BINARYOP.name()) != null) {
//				tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
//				continue;
//			} 
			 else if (matcher.group(TokenType.FLOATVALUE.name()) != null) {
				tokens.add(new Token(TokenType.FLOATVALUE, matcher.group(TokenType.FLOATVALUE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.MULTILINECOMMENT.name()) != null) {
				tokens.add(new Token(TokenType.MULTILINECOMMENT, matcher.group(TokenType.MULTILINECOMMENT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.PLUS.name()) != null) {
				tokens.add(new Token(TokenType.PLUS, matcher.group(TokenType.PLUS.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.MINUS.name()) != null) {
				tokens.add(new Token(TokenType.MINUS, matcher.group(TokenType.MINUS.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.DIVIDE.name()) != null) {
				tokens.add(new Token(TokenType.DIVIDE, matcher.group(TokenType.DIVIDE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.SEMICOLON.name()) != null) {
					tokens.add(new Token(TokenType.SEMICOLON, matcher.group(TokenType.SEMICOLON.name()), matcher.start()));
					continue;
				} 
			 else if (matcher.group(TokenType.MOD.name()) != null) {
				tokens.add(new Token(TokenType.MOD, matcher.group(TokenType.MOD.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.ASSIGN.name()) != null) {
				tokens.add(new Token(TokenType.ASSIGN, matcher.group(TokenType.ASSIGN.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.BINARYAND.name()) != null) {
				tokens.add(new Token(TokenType.BINARYAND, matcher.group(TokenType.BINARYAND.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.BINARYOR.name()) != null) {
				tokens.add(new Token(TokenType.BINARYOR, matcher.group(TokenType.BINARYOR.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.ELSEIF.name()) != null) {
				tokens.add(new Token(TokenType.ELSEIF, matcher.group(TokenType.ELSEIF.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.INCREMENT.name()) != null) {
				tokens.add(new Token(TokenType.INCREMENT, matcher.group(TokenType.INCREMENT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.DECREMENT.name()) != null) {
				tokens.add(new Token(TokenType.DECREMENT, matcher.group(TokenType.DECREMENT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.GREATER.name()) != null) {
				tokens.add(new Token(TokenType.GREATER, matcher.group(TokenType.GREATER.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.LESS.name()) != null) {
				tokens.add(new Token(TokenType.LESS, matcher.group(TokenType.LESS.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.GREATEREQUAL.name()) != null) {
				tokens.add(new Token(TokenType.GREATEREQUAL, matcher.group(TokenType.GREATEREQUAL.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.LESSEQUAL.name()) != null) {
				tokens.add(new Token(TokenType.LESSEQUAL, matcher.group(TokenType.LESSEQUAL.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.EQUAL.name()) != null) {
				tokens.add(new Token(TokenType.EQUAL, matcher.group(TokenType.EQUAL.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.NOT.name()) != null) {
				tokens.add(new Token(TokenType.NOT, matcher.group(TokenType.NOT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.AND.name()) != null) {
				tokens.add(new Token(TokenType.AND, matcher.group(TokenType.AND.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.OR.name()) != null) {
				tokens.add(new Token(TokenType.OR, matcher.group(TokenType.OR.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.LEFTBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.LEFTBRACKET, matcher.group(TokenType.LEFTBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.RIGHTBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.RIGHTBRACKET, matcher.group(TokenType.RIGHTBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.LEFTCURLYBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.LEFTCURLYBRACKET, matcher.group(TokenType.LEFTCURLYBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.RIGHTCURLYBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.RIGHTCURLYBRACKET, matcher.group(TokenType.RIGHTCURLYBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.LEFTARRBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.LEFTARRBRACKET, matcher.group(TokenType.LEFTARRBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.RIGHTARRBRACKET.name()) != null) {
				tokens.add(new Token(TokenType.RIGHTARRBRACKET, matcher.group(TokenType.RIGHTARRBRACKET.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.IF.name()) != null) {
				tokens.add(new Token(TokenType.IF, matcher.group(TokenType.IF.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.ELSE.name()) != null) {
				tokens.add(new Token(TokenType.ELSE, matcher.group(TokenType.ELSE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.WHILE.name()) != null) {
				tokens.add(new Token(TokenType.WHILE, matcher.group(TokenType.WHILE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.FOR.name()) != null) {
				tokens.add(new Token(TokenType.FOR, matcher.group(TokenType.FOR.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.BREAKSIGN.name()) != null) {
				tokens.add(new Token(TokenType.BREAKSIGN, matcher.group(TokenType.BREAKSIGN.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.CONTINUE.name()) != null) {
				tokens.add(new Token(TokenType.CONTINUE, matcher.group(TokenType.CONTINUE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.RETURN.name()) != null) {
				tokens.add(new Token(TokenType.RETURN, matcher.group(TokenType.RETURN.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.TYPEINT.name()) != null) {
				tokens.add(new Token(TokenType.TYPEINT, matcher.group(TokenType.TYPEINT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.TYPEBOOL.name()) != null) {
				tokens.add(new Token(TokenType.TYPEBOOL, matcher.group(TokenType.TYPEBOOL.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.TYPEFLOAT.name()) != null) {
				tokens.add(new Token(TokenType.TYPEFLOAT, matcher.group(TokenType.TYPEFLOAT.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.TYPECHAR.name()) != null) {
				tokens.add(new Token(TokenType.TYPECHAR, matcher.group(TokenType.TYPECHAR.name()), matcher.start()));
				continue;
			} 			
			 else if (matcher.group(TokenType.TYPESTRING.name()) != null) {
				tokens.add(new Token(TokenType.TYPESTRING, matcher.group(TokenType.TYPESTRING.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.TYPEVOID.name()) != null) {
				tokens.add(new Token(TokenType.TYPEVOID, matcher.group(TokenType.TYPEVOID.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.BOOLVALUE.name()) != null) {
				tokens.add(new Token(TokenType.BOOLVALUE, matcher.group(TokenType.BOOLVALUE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.CHARVALUE.name()) != null) {
				tokens.add(new Token(TokenType.CHARVALUE, matcher.group(TokenType.CHARVALUE.name()), matcher.start()));
				continue;
			} 
			 else if (matcher.group(TokenType.IDENT.name()) != null) {
				tokens.add(new Token(TokenType.IDENT, matcher.group(TokenType.IDENT.name()), matcher.start()));
				continue;
			}
			 else if (matcher.group(TokenType.STRINGVALUE.name()) != null) {
				tokens.add(new Token(TokenType.STRINGVALUE, matcher.group(TokenType.STRINGVALUE.name()), matcher.start()));
				continue;
			}
			 else if (matcher.group(TokenType.ONELINECOMMENT.name()) != null) {
				tokens.add(new Token(TokenType.ONELINECOMMENT, matcher.group(TokenType.ONELINECOMMENT.name()), matcher.start()));
				continue;
			}
			 else if (matcher.group(TokenType.COMMA.name()) != null) {
					tokens.add(new Token(TokenType.COMMA, matcher.group(TokenType.COMMA.name()), matcher.start()));
					continue;
				}
			 else if (matcher.group(TokenType.MULTIPLY.name()) != null) {
					tokens.add(new Token(TokenType.MULTIPLY, matcher.group(TokenType.MULTIPLY.name()), matcher.start()));
					continue;
				}
			else if (matcher.group(TokenType.WHITESPACE.name()) != null)
				continue;
		}

		return tokens;
	}

//	public static void main(String[] args) {
//		String input = "int g (int z){"
//				+ "int b = 9;"
//				+ "int a =  89;{"
//				+ "int b;"
//				+ "int c = 9;"
//				+ "a = 8;"
//				+ "int h;}"
//				+ "string s = 0;"
//				+ "int c = 9; "
//				+ "b = 0.5 - 2*a*s;"
//				+ "if(50 > 7)"
//				+ "a = 12;"
//				+ "else"
//				+ "a  = 13;"
//				+ "b = 0;"
//				+ "while(a < 23){"
//				+ "b = b + 1;"
//				+ "a = a + 1;}"
//				+ "}";
//		ArrayList<Token> tokens = lex(input);
////		for (Token token : tokens)
////			System.out.println(token);
//		Parser p = new Parser(tokens);
//		try {
//		Program pr = p.parsePrg();
//		
//			//Expression e = p.parseExp();
//		Evaluate ev = new Evaluate();
//			//State s =  new State(new HashMap<String, Double>());
//		 ev.evaluateProgram(pr);
//			System.out.println(ev.sc.get("a"));
//			System.out.println(ev.sc.get("b"));
//
//		
//		}
//		catch(SyntaxErrorException e){
//			//System.out.println(e.msg + " " + e.position);
//			System.out.println("kkkk");
//
//		}
//	}
	
}

