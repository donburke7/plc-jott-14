package src;

import java.util.ArrayList;
import java.util.Scanner;

import src.provided.JottParser;
import src.provided.JottTokenizer;
import src.provided.JottTree;
import src.provided.Token;

// Donald Burke
public class RunTranslator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Jott Translator\n");
        System.out.println("Enter filename to begin:");
        String filename = scanner.nextLine();
        
        System.out.println("Which language would you like to translate to?:");
        System.out.println("0: Jott\n" + "1: Python\n" + "2: C\n" + "3: Java");
        int choice = scanner.nextInt();
        while (choice > 3 || choice < 0) {
            System.out.println("Invalid language choice\n");
            System.out.println("Which language would you like to translate to?:");
            System.out.println("0: Jott\n" + "1: Python\n" + "2: C\n" + "3: Java");
            choice = scanner.nextInt();
        }
        translate(filename, choice);

        scanner.close();
    }

    public static void translate(String file, int langChoice) {
        ArrayList<Token> tokens = JottTokenizer.tokenize(file);
        if (tokens != null) {
            JottTree root = JottParser.parse(tokens);
            System.out.println("Translating " + file + " ...");
            
            if (langChoice == 0)
                root.convertToJott();
            else if (langChoice == 1)
                root.convertToPython();
            else if (langChoice == 2)
                root.convertToC();
            else if (langChoice == 3)
                root.convertToJava("");     // empty param for now
        }
    }
}