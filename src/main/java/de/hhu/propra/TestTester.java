package de.hhu.propra;

import javafx.beans.property.SimpleStringProperty;
import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Viktor on 06.07.2016.
 */
import vk.core.api.*;
public class TestTester extends SimpleStringProperty{
  private String code;
  private static JavaStringCompiler Testcompiler;
  private static Tracker tracker;
  private String dateiname;
  private Collection<TestFailure> notWorking;

  public String TestTester(String derCode) {
    this.code=derCode;
    String ergebnis="";
      //Tests.add(TestNr, new Test(DateiScanner));

    CompilationUnit testUnit = new CompilationUnit("Test", code, true);

    Testcompiler = CompilerFactory.getCompiler(testUnit);

    notWorking.addAll(Testcompiler.getTestResult().getTestFailures());
    Object[] meinArray = notWorking.toArray();


    try {
        Testcompiler.compileAndRunTests();
      } catch (Exception e) {
        return ("Fehler beim Testausfuehren!" + e.toString());
      }

      if (Testcompiler.getCompilerResult().hasCompileErrors()) {
        return fehlerString(testUnit);
      }

      try {
        FileWriter writer = new FileWriter(Main.getCorrectPath() + "/aufgaben/Test.txt");
        writer.write(code);
        writer.close();
      } catch (Exception e) {

      }
      ergebnis = "Tests werden durchgeführt...";
    return (ergebnis);
  }

  private static String Rueckgabe(CompilationUnit dieserTest){
    String rueckgabeString="";
    rueckgabeString+="Fehlgeschlagene Tests: "+Testcompiler.getTestResult().getNumberOfFailedTests();
    rueckgabeString+="\n";
    rueckgabeString+="Erfolgreiche Tests: "+Testcompiler.getTestResult().getNumberOfSuccessfulTests();
    rueckgabeString+="\n";
    rueckgabeString+="Ignorierte Tests: "+Testcompiler.getTestResult().getNumberOfIgnoredTests();
    return rueckgabeString;
  }

  private static String fehlerString(CompilationUnit dieserTest){
    String fehler = "";
    for (CompileError error : Testcompiler.getCompilerResult().getCompilerErrorsForCompilationUnit(dieserTest)){
      fehler += "\nin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n";
      fehler += error.getCodeLineContainingTheError() + "\n";
      fehler += error.getMessage() + "\n";
    }
    return fehler;
  }


}
