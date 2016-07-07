package de.hhu.propra;

import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor on 06.07.2016.
 */
import vk.core.api.*;
public class TestTester {
  private static int TestAnzahl=0;
  private List<Test> Tests = new ArrayList<Test>();
  private static JavaStringCompiler Testcompiler;
  private static String testname;
  public static String TesteTest(String code,int TestAnzahl, String tabname) {

    String ergebnis="";
    for (int TestNr = 0; TestNr <= TestAnzahl; TestNr++) {
      //Tests.add(TestNr, new Test(DateiScanner));

      testname = tabname;
      CompilationUnit aktuellerTest = new CompilationUnit(testname, code, true);
      Testcompiler = CompilerFactory.getCompiler(aktuellerTest);

      try {
        Testcompiler.compileAndRunTests();
      } catch (Exception e) {
        return ("Fehler beim Testausführen!" + e.toString());
      }

      if (Testcompiler.getCompilerResult().hasCompileErrors()) {
        return fehlerString(aktuellerTest,TestNr);
      }

      try {
        FileWriter writer = new FileWriter("temp/" + testname + ".java");
        writer.write(code);
        writer.close();
      } catch (Exception e) {

      }
      ergebnis = "Tests werden durchgeführt...";

    }
    return (ergebnis);
  }

  private static String fehlerString(CompilationUnit dieserTest,int TestNr){
    String fehler = "";
    int TestNummer=TestNr+1;
    fehler = "Test "+TestNummer+" enthält Fehler";
    for (CompileError error : Testcompiler.getCompilerResult().getCompilerErrorsForCompilationUnit(dieserTest)){
      fehler += "\nin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n";
      fehler += error.getCodeLineContainingTheError() + "\n";
      fehler += error.getMessage() + "\n";
    }
    return fehler;
  }


}
