package de.hhu.propra;

import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Created by Viktor on 06.07.2016.
 */
import vk.core.api.*;
public class TestTester {

  private static JavaStringCompiler Testcompiler;
  //private static int TestAnzahl=0;
  public static String derTest(String code,int TestAnzahl) {
    String dateiname = "Test";
    String ergebnis="";
    for (int TestNr = 0; TestNr <= TestAnzahl; TestNr++) {
      CompilationUnit aktuellerTest = new CompilationUnit(dateiname, code, true);
      Testcompiler = CompilerFactory.getCompiler(aktuellerTest);

      try {
        Testcompiler.compileAndRunTests();
      } catch (Exception e) {
        return ("Fehler beim Codeausführen!" + e.toString());
      }

      if (Testcompiler.getCompilerResult().hasCompileErrors()) {
        return fehlerString(aktuellerTest,TestNr);
      }

      try {
        FileWriter writer = new FileWriter("temp/" + dateiname + ".java");
        writer.write(code);
        writer.close();
      } catch (Exception e) {

      }
      ergebnis = "Tests werden durchgeführt...";

      try {
        Process process = Runtime.getRuntime().exec("cmd /C start temp\\temp_compile.cmd");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
          ergebnis += "\n" + line;
        }
      }
      catch (Exception e)
      {
        return e.toString();
      }
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
