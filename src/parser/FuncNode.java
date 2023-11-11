package src.parser;

import java.util.ArrayList;
import java.util.HashMap;

import src.provided.JottTree;
import src.provided.Token;
import src.provided.TokenType;

// Donald Burke
public class FuncNode implements JottTree{
    IdNode funcName;
    FuncParamsNode funcParams;
    FuncReturnNode funcReturnType;
    BodyNode funcBody;
    public static HashMap<String, VarInfo> varTable = new HashMap<>();
    
    
    public FuncNode(IdNode id, FuncParamsNode params, FuncReturnNode returnType, BodyNode body) {
        this.funcName = id;
        this.funcParams = params;
        this.funcReturnType = returnType;
        this.funcBody = body;
    }

    @Override
    public String convertToJott() {
        String out = "def ";
        out += this.funcName.convertToJott();
        out += "[";
        out += this.funcParams.convertToJott();
        out += "]:";
        out += this.funcReturnType.convertToJott();
        out += "{";
        out += this.funcBody.convertToJott();
        out += "}";
        return out;
    }

    @Override
    public String convertToJava(String className) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJava'");
    }

    @Override
    public String convertToC() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToC'");
    }

    @Override
    public String convertToPython() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToPython'");
    }

    @Override
    public boolean validateTree() throws SemanticException {
        // check if function name already in defTable (already defined)
        if (ProgramNode.defTable.containsKey(this.funcName.getName()) && !this.funcName.validateName()) {
            throw new SemanticException("Semantic Error in FuncNode, function already defined.", this.funcName.getToken());
        }
        funcBody.validateTree();
        funcParams.validateTree();
        funcReturnType.validateTree();
        
        if (this.funcReturnType.type == Type.Void) { // Check if Void function tries returning
            if (this.funcBody.getRetType() != null) {
                throw new SemanticException("Semantic Error in FuncNode, Void functions should not return anything.", this.funcName.getToken());
            }
        } else { // Check if body return matches the return type
            if (this.funcBody.isReturnable() && this.funcBody.getRetType() != this.funcReturnType.type) {
                throw new SemanticException("Semantic Error in FuncNode, body returns incorrect type.", this.funcName.getToken());
            }
        }

        return true;
    }
    
    public static FuncNode parse(ArrayList<Token> tokens) throws SyntaxException, SemanticException {
        if (tokens.size() == 0) {
            return null;
        }
        
        if (!tokens.get(0).getToken().equals("def")) { throw new SyntaxException("Syntax error in FuncNode, expected def keyword.", tokens.get(0)); }
        varTable = new HashMap<>();
        Token t = tokens.remove(0);
        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing name.", t); }
        IdNode func_name = IdNode.parse(tokens);
        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing '['.", t); }
        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxException("Syntax error in FuncNode, expected '['.", tokens.get(0));
        }
        tokens.remove(0);

        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing parameters.", t); }
        FuncParamsNode fcp = FuncParamsNode.parse(tokens);
        for (int i = 0; i < fcp.paramNames.size(); i++) {
            varTable.put(fcp.paramNames.get(i).convertToJott(), new VarInfo(fcp.paramTypes.get(i), true));
        }
        
        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing ']'.", t); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxException("Syntax error in FuncNode, expected ']'.", tokens.get(0));
        }
        tokens.remove(0);
        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens, before parsing ':'.", t); }
        if (tokens.get(0).getTokenType() != TokenType.COLON) {
            throw new SyntaxException("Syntax error in FuncNode, expected ':'.", tokens.get(0));
        }
        tokens.remove(0);

        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing return type.", t); }
        FuncReturnNode returnType = FuncReturnNode.parse(tokens);

        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing '{'.", t); }
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxException("Syntax error in FuncNode, expected '{'.", tokens.get(0));
        }
        tokens.remove(0);

        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing body.", t); }
        BodyNode body = BodyNode.parse(tokens);

        if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncNode, ran out of tokens before parsing '}'.", t); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxException("Syntax error in FuncNode, expected '}'.", tokens.get(0));
        }
        tokens.remove(0);
        
        FuncNode functionNode = new FuncNode(func_name, fcp, returnType, body);
        functionNode.validateTree();
        return functionNode;
    }

    public static void main(String[] args) throws SyntaxException, SemanticException {
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token("def", "test", 1, TokenType.ID_KEYWORD));
        FuncNode v = null;
        v = FuncNode.parse(tokens);
        System.out.println(v.convertToJott());
    }

}
