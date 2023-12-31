package src.parser;

import java.util.ArrayList;
import java.util.HashMap;

import src.provided.JottTree;
import src.provided.Token;
// import src.provided.TokenType;
import src.provided.TokenType;

// Clarke Kennedy
public class ProgramNode implements JottTree {

    private ArrayList<FuncNode> funcDefNodes;
    // Symbol table for function definitions where key is the function name and
    // value is an arr of the parameters' data types; except for the last value
    // which is the function's return type
    public static HashMap<String, Type[]> defTable = new HashMap<>();
    
    public ProgramNode(ArrayList<FuncNode> funcDefNodes) {
        this.funcDefNodes = funcDefNodes;
    }

    @Override
    public String convertToJott() {
        String s = "";

        for(FuncNode node : funcDefNodes) {
            s = s.concat(node.convertToJott())+"\n";
        }

        return s;
    }

    @Override
    public String convertToJava(String className) {
        String s = "public class " + className + " {\n";
        for(FuncNode node : funcDefNodes) {
            s = s.concat(node.convertToJava(className))+"\n";
        }
        s += "}";
        return s;
    }

    @Override
    public String convertToC() {
        String s = "#include <stdio.h>\n#include <string.h>\n#include <stdlib.h>\n#include <math.h>\n\n" +
        "char* concat(char* s1, char* s2) {\n" +
        "\tchar* result = (char*) malloc((strlen(s1)+strlen(s2)+1)*sizeof(char));\n" + 
        "\tstrcpy(result, s1);\n" + 
        "\tstrcat(result, s2);\n" + 
        "\treturn result;\n}\n\n";
        for(FuncNode node : funcDefNodes) {
            s = s.concat(node.convertToC())+"\n";
        }
        return s;
    }

    @Override
    public String convertToPython(int depth) {
        String s = "";
        for(FuncNode node : funcDefNodes) {
            s = s.concat(node.convertToPython(depth))+"\n";
        }
        s = s.concat("if __name__ == '__main__':\n\tmain()");
        return s;
    }

    @Override
    public boolean validateTree() throws SemanticException{
        if(!defTable.containsKey("main")) {
            throw new SemanticException("Semantic Error in ProgramNode, no main function", new Token("", "", 0, TokenType.ID_KEYWORD));
        }
        Type[] mainTypes = defTable.get("main");
        FuncNode mainNode = null;
        for (FuncNode node : funcDefNodes) {
            if (node.getName().equals("main")) {
                mainNode = node;
            }
        }
        if(mainTypes.length != 1) {
            throw new SemanticException("Semantic Error in ProgramNode, main function must have no parameters", mainNode.getToken());
        }
        if(mainTypes[mainTypes.length - 1] != Type.Void) {
            throw new SemanticException("Semantic Error in ProgramNode, main function must return void", mainNode.getToken());
        }
        return true;
    }

    public static ProgramNode parse(ArrayList<Token> tokens) throws SyntaxException, SemanticException {
        defTable = new HashMap<>();
        defTable.put("print", new Type[] {Type.Any, Type.Void});
        defTable.put("concat", new Type[] {Type.String, Type.String, Type.String});
        defTable.put("length", new Type[] {Type.String, Type.Integer});

        ArrayList<FuncNode> funcDefNodes = new ArrayList<>();
        while(tokens.size() > 0) {
            if(!tokens.get(0).getToken().equals("def")) {
                throw new SyntaxException("Syntax Error in ProgramNode, expected def keyword", tokens.get(0));
            }

            FuncNode node = FuncNode.parse(tokens);

            funcDefNodes.add(node);
        }

        ProgramNode programNode = new ProgramNode(funcDefNodes);
        programNode.validateTree();
        return programNode;
        
    }
    
}
