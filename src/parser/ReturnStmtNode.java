package src.parser;

import src.provided.JottTree;
import src.provided.Token;
import src.provided.TokenType;
import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {
    private ExprNode expr;

    public ReturnStmtNode(ExprNode expr) {
        this.expr = expr;
    }

    @Override
    public String convertToJott() {
        return "return " + expr.convertToJott() + ";";
    }

    @Override
    public String convertToJava(String className) {
        // TODO: Implement Java code generation for ReturnStmtNode
        // You'll need to convert the expression to Java code.
        throw new UnsupportedOperationException("Unimplemented method 'convertToJava'");
    }

    @Override
    public String convertToC() {
        // TODO: Implement C code generation for ReturnStmtNode
        // You'll need to convert the expression to C code.
        throw new UnsupportedOperationException("Unimplemented method 'convertToC'");
    }

    @Override
    public String convertToPython() {
        // TODO: Implement Python code generation for ReturnStmtNode
        // You'll need to convert the expression to Python code.
        throw new UnsupportedOperationException("Unimplemented method 'convertToPython'");
    }

    @Override
    public boolean validateTree() {
        // TODO: Implement tree validation for ReturnStmtNode
        // You can check if the expression is valid here.
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

    public static ReturnStmtNode parse(ArrayList<Token> tokens) throws SyntaxException {
        if (tokens.size() >= 2 && tokens.get(0).getToken().equals("return")) {
            tokens.remove(0); // Remove "return" keyword
            ExprNode expr = ExprNode.parse(tokens);
            if (expr != null && tokens.size() >= 1 && tokens.get(0).getTokenType() == TokenType.SEMICOLON) {
                tokens.remove(0); // Remove the semicolon
                return new ReturnStmtNode(expr);
            } else {
                // Handle error or throw SyntaxException
                System.err.println("Syntax Error in ReturnStmtNode - Invalid Return Statement");
                return null;
            }
        }
        return null; // Return null if "return" keyword is not found
    }
}