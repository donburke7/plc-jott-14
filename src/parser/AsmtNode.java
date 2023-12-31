package src.parser;

// import src.provided.JottTree;
import src.provided.Token;
import src.provided.TokenType;
import java.util.ArrayList;

// Clarke Kennedy
public class AsmtNode implements BodyStmtNode {
    private Type type;
    private IdNode name;
    private ExprNode expr;

    public AsmtNode(Type type, IdNode name, ExprNode expr) {
        this.type = type;
        this.name = name;
        this.expr = expr;
    }

    public Type getRetType() {
        return null;
    }

    public boolean isReturnable() {
        return false;
    }

    @Override
    public String convertToJott() {
        if (type == null) {
            return name.convertToJott() + " = " + expr.convertToJott() + ";";
        }
        return type.name() + " " + name.convertToJott() + " = " + expr.convertToJott() + ";";
    }

    @Override
    public String convertToJava(String className) {
        if (type == null) {
            return name.convertToJava(className) + " = " + expr.convertToJava(className) + ";";
        }
        String s = "";
        switch (type.name()) {
            case "Integer":
                s = "int";
                break;
            case "Double":
                s = "double";
                break;
            case "String":
                s = "String";
                break;
            case "Boolean":
                s = "boolean";
                break;
        }
        return s + " " + name.convertToJava(className) + " = " + expr.convertToJava(className) + ";";
    }

    @Override
    public String convertToC() {
        if (type == null) {
            return name.convertToC() + " = " + expr.convertToC() + ";";
        }
        String s = "";
        switch (type.name()) {
            case "Integer":
                s = "int";
                break;
            case "Double":
                s = "double";
                break;
            case "String":
                s = "char*";
                break;
            case "Boolean":
                s = "int";
                break;
        }
        return s + " " + name.convertToC() + " = " + expr.convertToC() + ";";
    }

    @Override
    public String convertToPython(int depth) {
        return name.convertToPython(depth) + " = " + expr.convertToPython(depth);
    }

    @Override
    public boolean validateTree() throws SemanticException{
        expr.validateTree();
        if (type != null) {
            if (FuncNode.varTable.containsKey(name.convertToJott())) {
                throw new SemanticException("Semantic Error in AsmtNode, variable already declared: " + name.convertToJott(), name.getToken());
            }
            name.validateName();
            FuncNode.varTable.put(name.convertToJott(), new VarInfo(type, false));
        }
        else if (!FuncNode.varTable.containsKey(name.convertToJott())) {
            throw new SemanticException("Semantic Error in AsmtNode, variable not declared: " + name.convertToJott(), name.getToken());
        }
        if (FuncNode.varTable.get(name.convertToJott()).type != expr.getType()) {
            throw new SemanticException("Semantic Error in AsmtNode, type mismatch: " + FuncNode.varTable.get(name.convertToJott()).type.name() + " and " + expr.getType(), name.getToken());
        }
        FuncNode.varTable.get(name.convertToJott()).initialized = true;
        return true;
    }
    
    public static AsmtNode parse(ArrayList<Token> tokens) throws SyntaxException{
        if (tokens.size() == 0){
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxException("Syntax Error in AsmtNode, expected id or keyword", tokens.get(0));
        }
        Token t = tokens.get(0);

        Type type = null;
        switch (t.getToken()) {
            case "Double":
                type = Type.Double;
                tokens.remove(0);
                break;
            case "Integer":
                type = Type.Integer;
                tokens.remove(0);
                break;
            case "String":
                type = Type.String;
                tokens.remove(0);
                break;
            case "Boolean":
                type = Type.Boolean;
                tokens.remove(0);
                break;
        }

        IdNode name = IdNode.parse(tokens);
        if(name == null) {
            throw new SyntaxException("Syntax Error in AsmtNode, reached end of file", t);
        }
        if (tokens.size() == 0){
            return null;
        }
        t = tokens.get(0);
        if (t.getTokenType() != TokenType.ASSIGN) {
            throw new SyntaxException("Syntax Error in AsmtNode, expected assignment operator", tokens.get(0));
        }
        tokens.remove(0);

        ExprNode expr = ExprNode.parse(tokens);
        if(expr == null) {
            throw new SyntaxException("Syntax Error in AsmtNode, reached end of file", t);
        }
        if (tokens.size() == 0){
            return null;
        }
        if (tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxException("Syntax Error in AsmtNode, expected semicolon", tokens.get(0));
        }
        tokens.remove(0);

        return new AsmtNode(type, name, expr);
    }

    public static void main(String[] args) throws SyntaxException{
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token("Integer", "test", 1, TokenType.ID_KEYWORD));
        tokens.add(new Token("x", "test", 1, TokenType.ID_KEYWORD));
        tokens.add(new Token("=", "test", 1, TokenType.ASSIGN));
        tokens.add(new Token("5", "test", 1, TokenType.NUMBER));
        tokens.add(new Token(";", "test", 1, TokenType.SEMICOLON));
        AsmtNode v = null;
        v = AsmtNode.parse(tokens);
        System.out.println(v.convertToJott());
    }

    @Override
    public Token getToken() {
        return name.getToken();
    }
}
