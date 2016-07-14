package de.hhu.propra;

import javafx.beans.property.SimpleStringProperty;
import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Viktor on 06.07.2016.
 */
import vk.core.api.*;
public class TestTester extends SimpleStringProperty {
  private String code;
  private static JavaStringCompiler Testcompiler;
  private static Tracker tracker;
  private String dateiname;
  private String nameTest;
  private static Main main;
  private String letzterStandTestCode = "";
  private Collection<TestFailure> notWorking;
  private static int fehlerhafteTests;
  private static boolean hatFehler;

  public void testeTests(String derCode, String testName) {
    this.code = derCode;
    nameTest=testName;
    String ergebnis = "";
    //Tests.add(TestNr, new Test(DateiScanner));
    dateiname=testName+"_Test";
    CompilationUnit testUnit = new CompilationUnit(dateiname, code, true);

    Testcompiler = CompilerFactory.getCompiler(testUnit);
    //Testcompiler.compileAndRunTests();
    //notWorking.addAll(Testcompiler.getTestResult().getTestFailures());
    //Object[] meinArray = notWorking.toArray();

    try {
      Testcompiler.compileAndRunTests();
    } catch (Exception e) {
      set("Fehler beim Testausfuehren!" + e.toString());
    }
    writeTest(code);
    tracker.aktuellerStandtoFile();
    if (Testcompiler.getCompilerResult().hasCompileErrors()) {
      hatFehler=true;
      set(fehlerString(testUnit));
      //logging(code,letzterStandTestCode,hatFehler,fehlerString(testUnit));
    } else {
      hatFehler=false;
      set(Rueckgabe(testUnit));
    }

    logging(code,letzterStandTestCode, hatFehler, fehlerString(testUnit));
    letzterStandTestCode=code;
  }

  public void writeTest(String testCode) {
    try {
      dateiname=nameTest+"_Test";
      String path = getCorrectPath() + "/aufgaben/" + nameTest + "/";
      File subdir = new File(path);
      if (!subdir.exists()) {
        if (!subdir.mkdir()) {
          set("Konnte Unterverzeichnis nicht erstellen");
          return;
        }
      }

      FileWriter writer = new FileWriter(path + dateiname + ".java");
      writer.write(testCode);
      writer.close();
    } catch (Exception e) {
      set("Konnte keine externen Dateien erstellen: " + e);
    }
  }

  private void logging(String code, String letzterStandTestCode, boolean hatFehler, String fehlerString) {
    boolean trackFehler = tracker.ermittleNeuerung(code, letzterStandTestCode, hatFehler, fehlerString);

    if (trackFehler) {
      set(tracker.getFehler());
    }
  }

  private static String Rueckgabe(CompilationUnit dieserTest) {
    String rueckgabeString = "";
    rueckgabeString += "Fehlgeschlagene Tests: " + Testcompiler.getTestResult().getNumberOfFailedTests();
    rueckgabeString += "\n";
    rueckgabeString += "Erfolgreiche Tests: " + Testcompiler.getTestResult().getNumberOfSuccessfulTests();
    fehlerhafteTests=Testcompiler.getTestResult().getNumberOfFailedTests();
    rueckgabeString += "\n";
    rueckgabeString += "Ignorierte Tests: " + Testcompiler.getTestResult().getNumberOfIgnoredTests();
    return rueckgabeString;
  }

  private static String fehlerString(CompilationUnit dieserTest) {
    String fehlerString = "Codefehler im Test in Zeile";
    for (CompileError error : Testcompiler.getCompilerResult().getCompilerErrorsForCompilationUnit(dieserTest)) {
      fehlerString += "\n" + error.getLineNumber() + ": " + error.getMessage() + ": \n";
      fehlerString += error.getCodeLineContainingTheError() + "\n";
      fehlerString += error.getMessage() + "\n";
    }
    return fehlerString;
  }
  
  public static int getAnzahlFehlerhaft(){
    return fehlerhafteTests;
  }

  public void setKonsolenText(String text) {
    set(text);
  }

  public void setTracker(Tracker tracker) {
    this.tracker = tracker;
  }


  public void setLetzterStandTestCode(String code) {
    this.letzterStandTestCode = code;
  }

  public String getCorrectPath() throws URISyntaxException {
    String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
    path = path.substring(0, path.lastIndexOf("/"));
    path = path.substring(0, path.lastIndexOf("/"));
    path = path.substring(0, path.lastIndexOf("/"));
    if (path.endsWith("build")) {
      path += "/libs";
    } else {
      path += "/build/libs";
    }

    return path;
  }

  public void setNameAufgabe(String nameAufgabe) {
    this.nameTest = nameAufgabe;
  }
}