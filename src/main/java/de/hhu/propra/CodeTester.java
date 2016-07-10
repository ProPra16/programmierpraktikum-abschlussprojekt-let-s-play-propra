package de.hhu.propra;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.property.SimpleStringProperty;
import vk.core.api.*;

public class CodeTester extends SimpleStringProperty {
    private String letzterStandCode = "";
	private String nameAufgabe = "test";
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
            set(tracker.getFehler());
        }
        letzterStandCode = code;
	}

	private String externCompile() {
		String ergebnis = "";
		try {
			String path = getCorrectPath() + "/libs/config/";
            ProcessBuilder pb = new ProcessBuilder(path + "temp_compile.bat", nameAufgabe);
            Process process = pb.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				ergebnis += line + "\n";
			}
		} catch (Exception e) {
			return "Fehler auﬂerhalb: " + e;
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
			String path = getCorrectPath() + "/libs/aufgaben/" + nameAufgabe + "/";
			File subdir = new File(path);
			if (!subdir.exists()){
				if (!subdir.mkdir()){
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

	public String getCorrectPath() throws URISyntaxException {
		String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		path = path.substring(0,path.lastIndexOf("/"));
		path = path.substring(0,path.lastIndexOf("/"));
		path = path.substring(0,path.lastIndexOf("/"));

		return path;
	}

	public void phasenWechselMerken(String von){
		tracker.phasenWechselMerken(von);
	}

    public void setTracker(Tracker tracker){
        this.tracker = tracker;
    }
}
