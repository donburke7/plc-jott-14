package src.parser;

import java.util.ArrayList;

import src.provided.Token;
import src.provided.TokenType;

public class IfNode implements BodyStmtNode {

    private ExprNode expr;
    private Body body;
    private ArrayList<ElseIfNode> elseiflist;
    private ElseNode else_node;
    
    public IfNode(ExprNode expr, Body body, ArrayList<ElseIfNode> elseiflist, ElseNode else_node) {
        this.expr = expr;
        this.body = body;
        this.elseiflist = elseiflist;
        this.else_node = else_node;
    }

    @Override
    public String convertToJott() {
        String s = "if["+expr.convertToJott()+"]{"+body.convertToJott()+"}";

        for(ElseIfNode node : elseiflist) {
            s.concat(node.convertToJott);
        }

        s.concat(else_node.convertToJott);

        return s;
        
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
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

    public IfNode parse(ArrayList<Token> tokens) throws SyntaxException {
        ArrayList<ElseIfNode> nodes = new ArrayList<>();

        if (tokens.size() == 0 || tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxException("Syntax Error in IfNode");
        }
        tokens.remove(0);

        if (tokens.size() == 0 || tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxException("Syntax Error in IfNode");
        }
        tokens.remove(0);

        ExprNode expr = ExprNode.parse(tokens);
        
        if (tokens.size() == 0 || tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxException("Syntax Error in IfNode");
        }
        tokens.remove(0);

        if (tokens.size() == 0 || tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxException("Syntax Error in IfNode");
        }
        tokens.remove(0);

        Body body = Body.parse(tokens);

        if (tokens.size() == 0 || tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxException("Syntax Error in IfNode");
        }
        tokens.remove(0);

        for(ElseIfNode node : elseiflist) {
            nodes.add(node.parse(tokens));
        }

        ElseNode elsenode = else_node.convertToJott();

        return new IfNode(expr, body, nodes, elsenode);
        
    }
    
}