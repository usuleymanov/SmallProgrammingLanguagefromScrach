# Small C-like Programming Language Built from Scrach
Small C-like Programming Language built from scratch. This programming language is capable of evaluating expressions, functions, loops and logic statements. The languege has been defined by BNF grammar and is able to evalueate resluts of experssions and functions. It is also able to highlight synatax errors and show the output of expressions. The evaluation engine has been built which is capable of evaluating the results of expressions, statements, loops and etc as shown in BNF rules. 
A minimal UI has been developed to execute the codes, show outputs and highlight the syntax errors. 

**Sample output from UI**
![image](https://user-images.githubusercontent.com/29092927/196136942-97cd4e95-cc22-468b-b7b8-8d89c028f50e.png)

**Sample Highlighting syntax error**
![image](https://user-images.githubusercontent.com/29092927/196138525-5315c5a6-62f1-4d35-a612-6ac4faeaa04f.png)


- **Lexer class** - tokenizes the given input
- **Parser class** - constructs AST
- **Evaluate class** - calculates the values of expressions and stores the results in State class  when it encounters assignment statement. Uses Scope class to push and pop values from State.
- **Scope class** - used by Evaluate class for handling scope of variables
- **State class** - stores the identifier and value of given variable. For every variable we have stack of values because of scoping. 
- **TypeException class**- thrown when program has type error.
- **SyntaxErrorException** - thrown when program has syntax error.
     
