
public enum TokenType {
		// Token types cannot have underscores
		//order matters 
		FLOATVALUE("-?[0-9]*\\.[0-9]+"),
		NUMBER("-?[0-9]+"), 
		//BINARYOP("[*|/|+|-]"),
		ONELINECOMMENT("//.*"),
		MULTILINECOMMENT("/\\*(.|\n)*\\*/"),
		WHITESPACE("[ \t\f\r\n]+"),
		INCREMENT("\\+\\+"),
		DECREMENT("\\-\\-"),
		PLUS("\\+"),
		MINUS("\\-"),
		MULTIPLY("\\*"),
		DIVIDE("\\/"),
		MOD("%"),
		EQUAL("\\=="),
		ASSIGN("\\="),
		GREATEREQUAL(">\\="),
		LESSEQUAL("<\\="),
		GREATER(">"),
		LESS("<"),
		BINARYAND("\\&\\&"),
		BINARYOR("\\|\\|"),
		NOT("\\!"),
		AND("\\&"),
		OR("\\|"),
		LEFTBRACKET("\\("),
		RIGHTBRACKET("\\)"),
		LEFTCURLYBRACKET("\\{"),
		RIGHTCURLYBRACKET("\\}"),
		LEFTARRBRACKET("\\["),
		RIGHTARRBRACKET("\\]"),
		ELSEIF("else if"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		FOR("for"),
		BREAKSIGN("break"),
		CONTINUE("continue"),
		RETURN("return"),
		TYPEINT("int"),
		TYPEBOOL("bool"),
		TYPEFLOAT("float"),
		SEMICOLON(";"),
		TYPECHAR("char"),
		COMMA(","),
		TYPESTRING("string"),
		TYPEVOID("void"),
		BOOLVALUE("true|false"),
		IDENT("[A-Za-z][A-Za-z0-9_]*"),
		STRINGVALUE("\".*\""),
		CHARVALUE("'.'"),;
		public final String pattern;
       //public int position;

		private TokenType(String pattern) {
			this.pattern = pattern;
		}
}
