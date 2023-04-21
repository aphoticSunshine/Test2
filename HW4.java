package homework4;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class HW4 {

	static ArrayList<String> lexeme = new ArrayList<String>();
	static int i = 0;
	static String nextToken = "";
	static String token, tokenClass, tokenHolder;
	static Scanner fileScanner;
	static Pattern alphaPattern = Pattern.compile("[a-zA-Z]+");
	
	public static void main(String[] args) throws Exception {
		//Instantiates two scanner inputs, one for the user to declare the file that will be iterated through and another for the file itself.
		Scanner fileNameInput = new Scanner(System.in);
		System.out.print("Please enter the name of the file we'll be checking:\n");
		String fileName = fileNameInput.nextLine();
		fileNameInput.close();
		try {
			//Prints the pathway of the file, or in an error case the pathway the program is looking for
			System.out.println(new File(fileName).getAbsolutePath());
			//trims the file name in case any extra spaces or tabs were entered and creates the file scanner
			File file = new File(fileName.trim());
			fileScanner = new Scanner(file);
			do {
				//iterates through the provided file and assigns tokenClass based on the input from the file, then calls lex to assign those tokens and add them to the lexeme arraylist
				getChar();
				nextToken = fileScanner.next();
				//System.out.println(nextToken);
				lex();
			} 
			while (fileScanner.hasNext());
			fileScanner.close();
		} 
		catch (FileNotFoundException e) {
			System.out.print("File not found ");
			e.printStackTrace();
		}
		//handles issue where unknown token is appended after datatype
		for (int x = 0; x < lexeme.size(); x ++) {
			if (lexeme.get(x).toString() == "UNKNOWN") {
				lexeme.remove(x);
			}
		}
		//Adds the ending character to designate the end of the lexeme list
		lexeme.add("EOF");
		//Assigns the lexeme list to the list tokens in the Rda class
		Rda.tokens = lexeme;
		//Sets the nextTokens String to the first lexeme from the lexeme list
		Rda.nextTokens = Rda.tokens.get(0);
		//Begins the recursive descent parser
		while (Rda.i != lexeme.size()) {
			Rda.stmt();
		}
		//Only prints if there was no grammatical issue, otherwise error gets thrown from the Rda class where the grammatical issue was
		System.out.print("This is in the language :O");
		
		
	}

	public static String lookup(String s) {
		//A lookup class made for assigning the correct operator tokens, including (,),{,},;
		switch (s) {
			case "+": {
				tokenClass = "ADD_OP";
				break;
			}
			case "-": {
				tokenClass = "SUB_OP";
				break;
			}
			case "*": {
				tokenClass = "MUL_OP";
				break;
			}
			case "/": {
				tokenClass = "DIV_OP";
				break;
			}
			case "%": {
				tokenClass = "MOD_OP";
				break;
			}
			case ">": {
				tokenClass = "GREATER_OP";
				break;
			}
			case "<": {
				tokenClass = "LESS_OP";
				break;
			}
			case ">=": {
				tokenClass = "GREATEROREQUAL_OP";
				break;
			}
			case "<=": {
				tokenClass = "LESSOREQUAL_OP";
				break;
			}
			case "=": {
				tokenClass = "ASSIGNMENT_OP";
				break;
			}
			case "==": {
				tokenClass = "EQUAL_OP";
				break;
			}
			case "!=": {
				tokenClass = "NOTEQUAL_OP";
				break;
			}
			case "&&": {
				tokenClass = "AND_OP";
				break;
			}
			case "||": {
				tokenClass = "OR_OP";
				break;
			}
			case ";": {
				tokenClass = "SEMICOLON";
				break;
			}
			case "(": {
				tokenClass = "OPAREN";
				break;
			}
			case ")": {
				tokenClass = "CPAREN";
				break;
			}
			case "{": {
				tokenClass = "OBRACKET";
				break;
			}
			case "}": {
				tokenClass = "CBRACKET";
				break;
			}
			case ",": {
				tokenClass = "COMMA";
				break;
			}
			
			default:
				tokenClass = "UNKNOWN";
				break;
		};
		
		return tokenClass;
	}
	
	public static void addChar() {
		//very straightforward, adds a token onto the arraylist of lexemes.
		//System.out.println(token);
		lexeme.add(token);
	}
	
	public static void addAssign() {
		//adds the assign token two steps behind an assignment operator for parsing
		if (lexeme.size() <= 2) {
			lexeme.add(0, "ASSIGN");
		}
		else
			lexeme.add(lexeme.lastIndexOf(token)-1, "ASSIGN");
	}
	
	public static void addDeclare() {
		//adds a declare token preceeding a datatype declaration for parsing
		lexeme.add(lexeme.lastIndexOf("DATATYPE"), "DECLARE");
	}
	
	public static void getChar() {
		if (fileScanner.hasNext("(if)")||fileScanner.hasNext("(IF)"))
			tokenClass = "IF_STMT";
		else if (fileScanner.hasNext("else") || fileScanner.hasNext("ELSE"))
			tokenClass = "ELSE";
		else if (fileScanner.hasNext("while")||fileScanner.hasNext("WHILE"))
			tokenClass = "WHILE_STMT";
		else if (fileScanner.hasNext("int") || fileScanner.hasNext("float") || fileScanner.hasNext("boolean") || fileScanner.hasNext("double") || fileScanner.hasNext("String") || fileScanner.hasNext("char") || fileScanner.hasNext("short") || fileScanner.hasNext("long") || fileScanner.hasNext("byte"))
			tokenClass = "DATATYPE";
		else if (fileScanner.hasNext(alphaPattern)) 
			tokenClass = "ID";
		else if (fileScanner.hasNext()) 
			tokenClass = "UNKNOWN";
		else 
			tokenClass = "EOF";
	}
	
	public static void lex() {
		//assigns a tokenClass, or token name, to the token variable to be pushed onto the lexeme array list.
		if (nextToken.matches("[+-]?\\d+"))
			tokenClass = "INT";
		else if (nextToken.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"))
			tokenClass = "FLOAT";
		switch (tokenClass) {
			case "INT": {
				token = tokenClass;
				addChar();
				break;
			}
			case "FLOAT": {
				token = tokenClass;
				addChar();
				break;
			}
			case "IF_STMT": {
				token = tokenClass;
				addChar();
				break;
			}
			case "WHILE_STMT": {
				token = tokenClass;
				addChar();
				break;
			}
			case "ELSE": {
				token = tokenClass;
				addChar();
				break;
			}
			case "ID": {
				token = tokenClass;
				addChar();
				break;
			}
			case "DATATYPE": {
				token = "DATATYPE";
				addChar();
				addDeclare();
			}
			case "UNKNOWN": {
				token = lookup(nextToken);
				if (token == "ASSIGNMENT_OP") {
					addChar();
					addAssign();
					break;
				}
				else
					addChar();
				break;
			}
			case "EOF": {
				token = "EOF";
				addChar();
				break;
			}
			
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + tokenClass);
		};
	}
	
}
