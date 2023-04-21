package homework4;

import java.util.ArrayList;

public class Rda {
	//tokens holds the arraylist of all tokens
	static ArrayList<String> tokens;
	//nextTokens holds the current token
	static String nextTokens;
	//i keeps track of the arraylist index
	static int i = 0;
	
	public static void stmt() throws Exception {
		//<STMT> --> <IF_STMT> | <WHILE_STMT> | <BLOCK> | <ASSIGN> | <DECLARE> | <EXPR>
		//System.out.println(nextTokens + " enter stmt");
		if (i == 0) {
			getNextToken(tokens, i);
		}
		if (nextTokens == "IF_STMT") {
			ifStmt();
		}
		else if (nextTokens == "OBRACKET") {
			block();
		}
		else if (nextTokens == "WHILE_STMT") {
			whileLoop();
		}
		else if (nextTokens == "ASSIGN") {
			getNextToken(tokens, i);
			assign();
		}
		else if (nextTokens == "DECLARE") {
			getNextToken(tokens, i);
			if (nextTokens != "DATATYPE") {
				throw new Exception("Missing datatype declaration");
			}
			else {
				declare();
			}
		}
		else if (nextTokens == "EOF") {
			return;
		}
		else {
			expr();
		}
	

		//System.out.println(nextTokens + " exit stmt");
	}
	
	public static void getNextToken(ArrayList<String> s, int i) {
		//Used to iterate through the list of tokens
		if (Rda.i < s.size()) {
		Rda.i++;
		Rda.nextTokens = s.get(i).toString();
		}
	}
	
	public static void stmtList() throws Exception {
		//<STMTLIST> --> { STMT ';' }
		//System.out.println(nextTokens + " enter stmtlist");
		getNextToken(tokens, i);
		stmt();
		if (nextTokens == "EOF") {
			return;
		}
		else if (nextTokens != "SEMICOLON") {
			throw new Exception("Missing a semicolon");
		}
		else {
			if (tokens.get(i) != "CBRACKET") {
				stmtList();
			}
			else {
				getNextToken(tokens, i);
				return;
			}
		}
		
		//System.out.println(nextTokens + " exit stmtlist");
	}
	
	public static void whileLoop() throws Exception {
		//<WHILE_LOOP> --> `while` `(` <BOOL_EXPR> `)` ( <BLOCK> )
		//System.out.println(nextTokens + " enter while");
		getNextToken(tokens, Rda.i);
		if (nextTokens != "OPAREN") {
			throw new Exception("Not in the language");
		}
		else {
			getNextToken(tokens, Rda.i);
			boolExpr();
			if (nextTokens != "CPAREN") {
				throw new Exception("closing");
			}
			else {
				getNextToken(tokens, Rda.i);
				if(nextTokens != "OBRACKET") {
					throw new Exception("Missing block bracket");
				}
				else {
					block();
					getNextToken(tokens, i);
				}
			}
		}
		//System.out.println("exit while");
	}
	
	public static void ifStmt() throws Exception {
		//<IF_STMT> --> `if` `(` <BOOL_EXPR> `)` ( <BLOCK> ) [ `else` ( <BLOCK> )] 
		//System.out.println(nextTokens + " enter ifstmt");
		getNextToken(tokens, Rda.i);
		if (nextTokens != "OPAREN") {
			System.out.println(nextTokens);
			throw new Exception("Missing open parenthesis");
		}
		else {
			getNextToken(tokens, Rda.i);
			boolExpr();
			if (nextTokens != "CPAREN") {
				throw new Exception("Missing closing parenthesis");
			}
			else {
				getNextToken(tokens, Rda.i);
				if(nextTokens != "OBRACKET") {
					throw new Exception("missing block start");
				}
				else {
					block();
					getNextToken(tokens, Rda.i);
					if (nextTokens == "ELSE") {
						getNextToken(tokens, Rda.i);
						if(nextTokens != "OBRACKET") {
							throw new Exception("missing block statement start");
						}
						else
							block();
					}	
				}
			}
		}
			//System.out.println("exit ifstmt");
	}
	
	public static void block() throws Exception {
		//<BLOCK> --> `{` <STMT_LIST> `}`
		//System.out.println(nextTokens + " enter block");
		if (nextTokens != "OBRACKET") {
			throw new Exception("missing opening bracket");
		}
		else {
			stmtList();
			if (nextTokens == "EOF") {
				return;
			}
			else {
				if (nextTokens != "CBRACKET") {
					System.out.print(nextTokens);
					throw new Exception("missing closing bracket");
					//getNextToken(tokens, Rda.i);
					//stmtList();	
				}
				else if (nextTokens == "EOF") {
					return;
				}
				else {
					getNextToken(tokens, i);
				}
			}
		}
		//System.out.println("exit block");
	}
	
	public static void declare() throws Exception {
		//<DECLARE> --> DATATYPE ID {, ID}
		//System.out.println(nextTokens + " enter declare");
		getNextToken(tokens, i);
		if (nextTokens != "ID") {
			throw new Exception("Missing ID");
		}
		else {
			getNextToken(tokens, i);
			if (nextTokens == "COMMA") {
				declare();
			}
		}
		//System.out.println("exit delcare");
	}
	
	public static void assign() throws Exception {
		//<ASSIGN> --> ID "=" <EXPR>
		//System.out.println(nextTokens + " Enter Assign");
		if (nextTokens != "ID") {
			throw new Exception("Missing identifier on assign");
		}
		else {
			getNextToken(tokens, i);
			//System.out.print(nextTokens);
			if (nextTokens != "ASSIGNMENT_OP") {
				throw new Exception("missing assign");
			}
			else {
				getNextToken(tokens, i);
				expr();
			}
		}
		//System.out.println("Exit Assign");
	}
	
	public static void expr() throws Exception {
		//<EXPR> --> <TERM> {(`+`|`-`) <TERM>}
		//System.out.println(nextTokens + " enter expr");
		term();
		while(nextTokens == "ADD_OP" || nextTokens == "SUB_OP") {
			getNextToken(tokens, Rda.i);
			term();
		}
		//System.out.println("exit expr");
	}
	
	public static void term() throws Exception {
		//<TERM> --> <FACT> {(`*`|`/`|`%`) <FACT>}
		//System.out.println(nextTokens + " enter term");
		factor();
		while(nextTokens == "MUL_OP" || nextTokens == "DIV_OP" || nextTokens == "MOD_OP") {
			getNextToken(tokens, Rda.i);
			factor();
		}
		//System.out.println("exit term");
	}
	
	public static void factor() throws Exception {
		//<FACT> --> ID | INT_LIT | FLOAT_LIT | `(` <EXPR> `)`
		//System.out.println(nextTokens + " enter factor");
		if (nextTokens == "ID" || nextTokens == "INT" || nextTokens == "FLOAT") {
			getNextToken(tokens, Rda.i);
		}
		else {
			if (nextTokens == "OPAREN" ) {
				getNextToken(tokens, Rda.i);
				expr();
				if (nextTokens == "CPAREN" ) 
					getNextToken(tokens, Rda.i);
				else if (nextTokens == "NOTEQUAL_OP" || nextTokens == "EQUAL_OP") {
					getNextToken(tokens, Rda.i);
					expr();
				}
				else {
					System.out.println(nextTokens + " " + i);
					throw new Exception("missing closing parenthesis (factor)");
				}
			}
			else 
				System.out.println(nextTokens);
				throw new Exception("not an int, ID, float, or cparen ");
		}
		//System.out.println("exit factor");
	}
	
	public static void boolExpr() throws Exception {
		//<BOOL_EXPR> --> <BTERM> {(`>`|`<`|`>=`|`<=`) <BTERM>}
		//System.out.println(nextTokens + " enter boolexpr");
		bTerm();
		while (nextTokens == "GREATER_OP" || nextTokens == "LESS_OP" || nextTokens == "GREATEROREQUAL_OP" || nextTokens == "LESSOREQUAL_OP") {
			getNextToken(tokens, Rda.i);
			bTerm();
		}
		//System.out.println(nextTokens + " exit boolexpr");

	}
	
	public static void bTerm() throws Exception {
		//<BTERM> --> <BAND> {(`==`|`!=`) <BAND>}
		//System.out.println(nextTokens+" enter bTerm");
		bAnd();
		while (nextTokens == "EQUAL_OP" || nextTokens == "NOTEQUAL_OP") {
			getNextToken(tokens, Rda.i);
			bAnd();
		}
		//System.out.println("exit bTerm");
	}
	
	public static void bAnd() throws Exception {
		//<BAND> --> <BOR> {`&&` <BOR>}
		//System.out.println(nextTokens+" enter bAnd");
		bOr();
		while (nextTokens == "AND_OP") {
			getNextToken(tokens, Rda.i);
			bOr();
		}
		//System.out.println("exit bAnd");
	}
	
	public static void bOr() throws Exception {
		//<BOR> --> <EXPR> {`&&` <EXPR>}
		//System.out.println(nextTokens+" enter bOr");
		expr();
		while (nextTokens == "OR_OP") {
			getNextToken(tokens, Rda.i);
			expr();
		}
		//System.out.println("exit bOr");
	}
}
