
public class Token {
		public TokenType type;
		public String data;
		public int position;

		public Token(TokenType type, String data, int position) {
			this.type = type;
			this.data = data;
			this.position = position;
		}

		@Override
		public String toString() {
			return String.format("(%s %s %d)", type.name(), data, position);
		}
		
		public String getData(){
			return data;
		}
		
		public TokenType getType(){
			return type;
		}
		
		public int getPosition(){
			return position;
		}
}
