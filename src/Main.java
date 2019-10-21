import java.io.FileNotFoundException;
import java.io.IOException;
import lexical.LexicalAnalyser;

public class Main {

  public static void main(String[] args) {
    String sourceFileName = "test6.txt";
    if (args.length > 0) {
      sourceFileName = args[0];
    }

    // Run lexical analyser
    try {
      LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(sourceFileName);
      System.out.println("Executing source file: '" + sourceFileName + "'");
      lexicalAnalyser.run();

      System.out.println("\nSuccessfully completed compilation!\n");
      System.out.println("Tokens:");
      lexicalAnalyser.printTokens();

      System.out.println("\nSymbol table:");
      lexicalAnalyser.printSymbolTable();
    } catch (FileNotFoundException ex) {
      System.out.println("ERROR: Source file \"" + sourceFileName + "\" not found.");
      System.exit(-1);
    } catch (IOException ex) {
      System.out.println("ERROR: Failed to open  \"" + sourceFileName + "\".");
      System.exit(-1);
    } catch (Exception ex) {
      System.out.println(ex);
      System.exit(-1);
    }
  }
}