package de.hhu.propra;

import de.hhu.propra.model.Analyse;
import de.hhu.propra.view.OberflaecheController;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Freddy on 07.07.2016.
 */

public class Tracker {
    private OberflaecheController ofController;
    private long millisBeiLetztemWechsel;
    private long millisBeiStart;
    private long millisInGreen = 0;
    private long millisInRefactor = 0;
    private long millisInRed = 0;
    private String fehler;
    private String nameAufgabe;
    private Analyse analyse;

    public Tracker(OberflaecheController ofController, String nameAufgabe){
        this.ofController = ofController;
        millisBeiStart = System.currentTimeMillis();
        millisBeiLetztemWechsel = millisBeiStart;
        this.nameAufgabe = nameAufgabe;
    }

    public void phasenWechselMerken(String von){
         long aktuelleMillis = System.currentTimeMillis();
         switch(von.toLowerCase()){
             case "green":
                    millisInGreen += aktuelleMillis - millisBeiLetztemWechsel;
                 break;
             case "red":
                    millisInRed += aktuelleMillis - millisBeiLetztemWechsel;
                 break;
             case "refactor":
                    millisInRefactor += aktuelleMillis - millisBeiLetztemWechsel;
                 break;
         }
         millisBeiLetztemWechsel = aktuelleMillis;
    }

    public void logPhasenWechsel(String von){
        switch(von.toLowerCase()){
            case "green":
                log("Phase gewechselt: " + millisInGreen/1000 + "s fuer diese Green-Phase benoetigt");
                break;
            case "red":
                log("Phase gewechselt: " + millisInRed/1000 + "s fuer diese Red-Phase benoetigt");
                break;
            case "refactor":
                log("Phase gewechselt: " + millisInRefactor/1000 + "s fuer diese Refactor-Phase benoetigt");
                break;
        }
    }

    public void analyseErstellen(String von){
        phasenWechselMerken(von);
        analyse = new Analyse(millisInGreen, millisInRed, millisInRefactor);
    }

    public Analyse getAnalyse(){
        return this.analyse;
    }

    public boolean log(String changes){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String uhrzeit = sdf.format(new Date());
        try{
            String path = getCorrectPath() + "/aufgaben/" + nameAufgabe;
            FileWriter writer = new FileWriter(path + "/log.txt", true);
            if (changes.trim().length() > 0){
                writer.write(uhrzeit + ": " + changes + "\n");
            }
            writer.close();
        } catch (Exception e){
            fehler = "Fehler beim loggen: " + e;
            return false;
        }
        return true;
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

    public boolean ermittleNeuerung(String neuerCode, String letzterStandCode, boolean isFehler, String fehler) {
        String aenderung = "";
        String lastChange = "";
        BufferedReader alterCodeReader = new BufferedReader(new StringReader(letzterStandCode));
        BufferedReader neuerCodeReader = new BufferedReader(new StringReader(neuerCode));

        try {
            String aktZeileAlterCode = alterCodeReader.readLine();
            String aktZeileNeuerCode = neuerCodeReader.readLine();

            while (aktZeileNeuerCode != null) {
                if (lastChange.equals("added")){
                    if (aktZeileNeuerCode.trim().length() > 0) {
                        while ((aktZeileAlterCode != null) && (aktZeileAlterCode.trim().length() == 0)) {
                            aktZeileAlterCode= alterCodeReader.readLine();
                        }
                        if (aktZeileAlterCode == null) {
                            aenderung += "\n\t\t" + aktZeileNeuerCode;
                            aktZeileNeuerCode = neuerCodeReader.readLine();
                        } else {
                            if (!aktZeileNeuerCode.equals(aktZeileAlterCode)) {
                                aenderung += "\n\tGeaendert:\n\t\talt: " + aktZeileAlterCode + "\n\t\tneu: " + aktZeileNeuerCode;
                                lastChange = "change";
                            }
                            aktZeileAlterCode = alterCodeReader.readLine();
                            aktZeileNeuerCode = neuerCodeReader.readLine();
                        }
                    } else {
                        aktZeileNeuerCode = neuerCodeReader.readLine();
                    }
                } else {
                    if (aktZeileNeuerCode.trim().length() > 0) {
                        while ((aktZeileAlterCode != null) && (aktZeileAlterCode.trim().length() == 0)) {
                            aktZeileAlterCode = alterCodeReader.readLine();
                        }
                        if (aktZeileAlterCode == null) {
                            aenderung += "\n\tHinzugefuegt: " + "\n\t\t" + aktZeileNeuerCode;
                            aktZeileNeuerCode = neuerCodeReader.readLine();
                            lastChange = "added";
                        } else {
                            if (!aktZeileNeuerCode.equals(aktZeileAlterCode)) {
                                if (aktZeileAlterCode.trim().length() == 0){
                                    aenderung += aenderung += "\n\tGeloescht: " + "\n\t\t" + aktZeileAlterCode;
                                }
                                aenderung += "\n\tGeaendert:\n\t\talt: " + aktZeileAlterCode.trim() + "\n\t\tneu: " + aktZeileNeuerCode.trim();
                            }
                            aktZeileAlterCode = alterCodeReader.readLine();
                            aktZeileNeuerCode = neuerCodeReader.readLine();
                        }
                    } else {
                        aktZeileNeuerCode = neuerCodeReader.readLine();
                    }
                }
            }

            while (aktZeileAlterCode != null){
                if (aktZeileAlterCode.trim().length() > 0) {
                    if (lastChange.equals("deleted")) {
                        aenderung += "\n\t\t" + aktZeileAlterCode;
                    } else {
                        aenderung += "\n\tGeloescht: " + "\n\t\t" + aktZeileAlterCode;
                        lastChange = "deleted";
                    }
                }
                aktZeileAlterCode = alterCodeReader.readLine();
            }

            if (isFehler){
                aenderung += "\nNicht kompilierbar, Fehler:" + fehler;
            }

            if (!log(aenderung)){
                return true;
            }
        } catch (IOException e) {
            fehler = "Fehler beim loggen: " + e;
            return true;
        }
        return false;
    }

    public String getFehler(){
        return this.fehler;
    }

    public void setNameAufgabe(String nameAufgabe){
        this.nameAufgabe = nameAufgabe;
    }

    public void setMillisBeiLetztemWechsel(long millisBeiLetztemWechsel){
        this.millisBeiLetztemWechsel = millisBeiLetztemWechsel;
    }
}
