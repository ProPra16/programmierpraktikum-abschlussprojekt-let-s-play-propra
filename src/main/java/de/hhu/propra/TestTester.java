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

  public void testeTests(String derCode, String testName) {
    this.code = derCode;
    nameTest = testName;
    String ergebnis = "";
    //Tests.add(TestNr, new Test(DateiScanner));
    dateiname=testName+"Test";
    CompilationUnit testUnit = new CompilationUnit("Test", code, true);

    Testcompiler = CompilerFactory.getCompiler(testUnit);
    //Testcompiler.compileAndRunTests();
    //notWorking.addAll(Testcompiler.getTestResult().getTestFailures());
    //Object[] meinArray = notWorking.toArray();

    try {
      Testcompiler.compileAndRunTests();
    } catch (Exception e) {
      set("Fehler beim Testausfuehren!" + e.toString());
    }

    if (Testcompiler.getCompilerResult().hasCompileErrors()) {
      set(fehlerString(testUnit));
    } else {
      set(Rueckgabe(testUnit));
    }
    writeTest();
  }

  private void writeTest() {
    try {
      String path = getCorrectPath() + "/aufgaben/" + nameTest + "/";
      File subdir = new File(path);
      if (!subdir.exists()) {
        if (!subdir.mkdir()) {
          set("Konnte Unterverzeichnis nicht erstellen");
          return;
        }
      }

      FileWriter writer = new FileWriter(path + dateiname + ".java");
      writer.write(code);
      writer.close();
    } catch (Exception e) {
      set("Konnte keine externen Dateien erstellen: " + e);
    }
  }

  private void logging(String code, String letzterStandTestCode, boolean fehler, String fehlerString) {
    boolean trackFehler = tracker.ermittleNeuerung(code, letzterStandTestCode, fehler, fehlerString);

    if (trackFehler) {
      set(tracker.getFehler());
    }
  }

  private static String Rueckgabe(CompilationUnit dieserTest) {
    String rueckgabeString = "";
    rueckgabeString += "Fehlgeschlagene Tests: " + Testcompiler.getTestResult().getNumberOfFailedTests();
    rueckgabeString += "\n";
    rueckgabeString += "Erfolgreiche Tests: " + Testcompiler.getTestResult().getNumberOfSuccessfulTests();
    rueckgabeString += "\n";
    rueckgabeString += "Ignorierte Tests: " + Testcompiler.getTestResult().getNumberOfIgnoredTests();
    return rueckgabeString;
  }

  private static String fehlerString(CompilationUnit dieserTest) {
    String fehler = "Codefehler im Test in Zeile";
    for (CompileError error : Testcompiler.getCompilerResult().getCompilerErrorsForCompilationUnit(dieserTest)) {
      fehler += "\n" + error.getLineNumber() + ": " + error.getMessage() + ": \n";
      fehler += error.getCodeLineContainingTheError() + "\n";
      fehler += error.getMessage() + "\n";
    }
    return fehler;
  }

  public void setKonsolenText(String text) {
    set(text);
  }

  public void setTracker(Tracker tracker) {
    this.tracker = tracker;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  public void setLetzterStandTestCodeCode(String code) {
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
}