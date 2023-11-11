package src.parser;

import java.util.ArrayList;

import src.provided.JottTree;
import src.provided.Token;
import src.provided.TokenType;

// Donald Burke
public class FuncParamsNode implements JottTree {
    ArrayList<IdNode> paramNames;
    ArrayList<Type> paramTypes;

    public FuncParamsNode(ArrayList<IdNode> id, ArrayList<Type> types) {
        this.paramNames = id;
        this.paramTypes = types;
    }

    @Override
    public String convertToJott() {
        String out = "";
        if (this.paramNames.size() > 0) {
            out += this.paramNames.get(0).convertToJott() + ":" + this.paramTypes.get(0).name();
            for (int i = 1; i < this.paramNames.size(); i++) {
                out += ", " + this.paramNames.get(i).convertToJott() + ":" + this.paramTypes.get(i).name();
            }
        }
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
        for (int i = 0; i < paramNames.size(); i++) {
            IdNode name = paramNames.get(i);
            name.validateName();
            if (FuncNode.varTable.containsKey(name.getName())) { throw new SemanticException("Semantic Error in FuncParamsNode, param name already used.", name.getToken()); }
            FuncNode.varTable.put(name.getName(), new VarInfo(paramTypes.get(i), true));
        }
        return true;
    }

    public static FuncParamsNode parse(ArrayList<Token> tokens) throws SyntaxException {
        ArrayList<IdNode> params = new ArrayList<IdNode>();
        ArrayList<Type> pTypes = new ArrayList<Type>();
        if (tokens.size() > 0 && tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            IdNode param = IdNode.parse(tokens);
            params.add(param);
            if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncParamsNode, ran out of tokens before ':'.", param.getToken()); }

            if (tokens.get(0).getTokenType() != TokenType.COLON) {
                throw new SyntaxException("Syntax error in FuncParamsNode, expected ':'.", tokens.get(0));
            }
            tokens.remove(0);

            if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncParamsNode, ran out of tokens before type.", param.getToken()); }
            Token t = tokens.get(0);
            Type paramtype = null;

            switch (t.getToken()) {
                case "Double":
                    paramtype = Type.Double;
                    break;
                case "Integer":
                    paramtype = Type.Integer;
                    break;
                case "String":
                    paramtype = Type.String;
                    break;
                case "Boolean":
                    paramtype = Type.Boolean;
                    break;
                default:
                    throw new SyntaxException("Syntax Error in FuncParamsNode, invalid type keyword.", t);
            }
            tokens.remove(0);
            pTypes.add(paramtype);

            while (tokens.size() != 0 && tokens.get(0).getTokenType() == TokenType.COMMA) {
                t = tokens.remove(0); // eat comma
                if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncParamsNode, ran out of tokens before parameter name.", t); }
                // repeat the stuff above the while loop
                param = IdNode.parse(tokens);
                params.add(param);
                if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncParamsNode, ran out of tokens", param.getToken()); }

                if (tokens.get(0).getTokenType() != TokenType.COLON) {
                    throw new SyntaxException("Syntax error in FuncParamsNode, expected ':'.", tokens.get(0));
                }
                tokens.remove(0);

                if (tokens.size() == 0) { throw new SyntaxException("Syntax error in FuncParamsNode, ran out of tokens before type.", param.getToken()); }
                t = tokens.get(0);
                paramtype = null;

                switch (t.getToken()) {
                case "Double":
                    paramtype = Type.Double;
                    break;
                case "Integer":
                    paramtype = Type.Integer;
                    break;
                case "String":
                    paramtype = Type.String;
                    break;
                case "Boolean":
                    paramtype = Type.Boolean;
                    break;
                default:
                    throw new SyntaxException("Syntax Error in FuncParamsNode, invalid type keyword.", t);
                }
                pTypes.add(paramtype);
                tokens.remove(0);
            }
        }

        return new FuncParamsNode(params, pTypes);
    }
    
}
