package de.hhu.propra;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Collection;
import javafx.beans.property.SimpleStringProperty;
import vk.core.api.*;

public class CodeTester extends SimpleStringProperty {
    private String letzterStandCode = "";
	private String nameAufgabe;
	private JavaStringCompiler compiler;
    private boolean getestetUndFehlerfrei;
	private String code;
    private static Tracker tracker;
	private String dateiname;

	public void testCode(String code, String tabname){
        getestetUndFehlerfrei = false;
        set("");
		this.code = code;
		dateiname = tabname;
		boolean fehler = false;
		CompilationUnit unit = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit);

		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
            set("Fehler beim Codeausfuehren: " + e);
			return;
		}
        writeExternalFile(code);
		tracker.aktuellerStandtoFile();
		if (compiler.getCompilerResult().hasCompileErrors()){
			set(fehlerString(unit));
			fehler = true;
			logging(code, letzterStandCode, fehler, fehlerString(unit));
            return;
		}

		logging(code, letzterStandCode, fehler, fehlerString(unit));
        set(externCompile() + "\nBuild succesfull: " + compiler.getCompilerResult().getCompileDuration().toMillis());
        getestetUndFehlerfrei = true;
        letzterStandCode = code;
	}

	private String externCompile() {
		String ergebnis = "";
		try {
			String path = getCorrectPath() + "/config/";
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

	private Collection<CompileError> ermittleFehler(CompilationUnit unit){
		return compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit);
	}

	private String fehlerString(CompilationUnit unit){
		String fehler = "";
		fehler = "Dein Quellcode enthaelt Fehler";
		for (CompileError error : ermittleFehler(unit)){
			fehler += "\n\tin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n\t";
			fehler += error.getCodeLineContainingTheError() + "\n\t";
		}
		return fehler;
	}

    public void writeExternalFile(String code) {
		try {
			String path = getCorrectPath() + "/aufgaben/" + nameAufgabe + "/";
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
        if (path.endsWith("build")){
            path+="/libs";
        }
        else {
            path += "/build/libs";
        }

		return path;
	}

	private void logging(String code, String letzterStandCode, boolean fehler, String fehlerString){
		boolean trackFehler = tracker.ermittleNeuerung(code, letzterStandCode, fehler, fehlerString);

		if (trackFehler){
			set(tracker.getFehler());
		}
	}

	public void setKonsolenText(String text){
		set(text);
	}

    public void setTracker(Tracker tracker){
        this.tracker = tracker;
    }


    public void setLetzterStandCode(String code){
        this.letzterStandCode = code;
    }

    public boolean isGetestetUndFehlerfrei(){
        return this.getestetUndFehlerfrei;
    }

    public void setGetestetUndFehlerfrei(boolean bool){
        this.getestetUndFehlerfrei = bool;
    }

	public void setNameAufgabe(String nameAufgabe){
		this.nameAufgabe = nameAufgabe;
	}

    public void setDateiname(String dateiname){
        this.dateiname = dateiname;
    }
	}
