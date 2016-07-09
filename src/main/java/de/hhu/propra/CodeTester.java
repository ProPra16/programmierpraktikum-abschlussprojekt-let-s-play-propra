package de.hhu.propra;

import java.io.*;
import java.net.URISyntaxException;

import javafx.beans.property.SimpleStringProperty;
import vk.core.api.*;

public class CodeTester extends SimpleStringProperty {
    private String letzterStandCode = "";
	private JavaStringCompiler compiler;
    private static Tracker tracker;
	private String dateiname;

	public void testCode(String code, String tabname){
        set("");
		dateiname = tabname;
		CompilationUnit unit = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit);

		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
            set("Fehler beim Codeausfuehren: " + e);
			return;
		}

		if (compiler.getCompilerResult().hasCompileErrors()){
			set(fehlerString(unit, tabname));
            return;
		}

        writeExternalFile(code);
        set(externCompile());

        boolean trackFehler = tracker.ermittleNeuerung(code, letzterStandCode);
        if (trackFehler){
            set("Fehler beim Tracking");
        }
        letzterStandCode = code;
	}

	private String externCompile() {
		String ergebnis = "";
		try {
			String path = getCorrectPath() + "/libs/code/";
			Process process = Runtime.getRuntime().exec(path + "temp_compile.bat");

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				ergebnis += line + "\n";
			}
		} catch (Exception e) {
			return "Fehler au�erhalb: " + e;
		}
		return ergebnis;
	}

	private String fehlerString(CompilationUnit unit, String klasse){
		String fehler = "";
		fehler = "Klasse " + klasse + ": Dein Quellcode enthaelt Fehler";
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			fehler += "\n\tin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n\t";
			fehler += error.getCodeLineContainingTheError() + "\n\t";
			fehler += error.getMessage() + "\n\t";
		}
		return fehler;
	}

    public void writeExternalFile(String code) {
		try {
			String path = getCorrectPath() + "/libs/code/";

            FileWriter writer = new FileWriter(path + dateiname + ".java");
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            set("Konnte keine externen Dateien erstellen: " + e);
        }
    }

    public void speichern(){

    }

	public String getCorrectPath() throws URISyntaxException {
		String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		path = path.substring(0,path.lastIndexOf("/"));
		path = path.substring(0,path.lastIndexOf("/"));
		path = path.substring(0,path.lastIndexOf("/"));

		return path;
	}


    public void setTracker(Tracker tracker){
        this.tracker = tracker;
    }
}
