package de.hhu.propra;

import de.hhu.propra.model.Analyse;
import de.hhu.propra.view.OberflaecheController;

import java.io.BufferedReader;
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
    private long millisSeitStart;
    private long millisBeiStart;
    private long millisInGreen = 35;
    private long millisInRed = 25;
    private Analyse analyse;

    public Tracker(OberflaecheController ofController){
        this.ofController = ofController;
        millisBeiStart = System.currentTimeMillis();
        millisBeiLetztemWechsel = millisBeiStart;
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
         }
         millisBeiLetztemWechsel = aktuelleMillis;
    }

    public void analyseErstellen(){
        millisSeitStart = System.currentTimeMillis() - millisBeiStart;
        analyse = new Analyse(millisSeitStart, millisInGreen, millisInRed);
    }

    public Analyse getAnalyse(){
        return this.analyse;
    }

    public void log(String changes){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String uhrzeit = sdf.format(new Date());
        try{
            /*String path = getCorrectPath() + "/log/";
            FileWriter writer = new FileWriter(path + "log.txt");
            writer.append(uhrzeit + ": " + changes);
            writer.close();*/
            if (changes.trim().length() > 0){
                System.out.println(uhrzeit + ": " + changes);
            }
        } catch (Exception e){
            ofController.appendKonsoleText("Tracking fehlgeschlagen: " + e);
        }
    }

    public String getCorrectPath() throws URISyntaxException {
        String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(0,path.lastIndexOf("/"));

        return path;
    }

    public boolean ermittleNeuerung(String neuerCode, String letzterStandCode) {
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

            /* while (aktZeileAlterCode != null){
                if (lastChange.equals("deleted")){
                    aenderung += "\n\t\t" + aktZeileAlterCode;
                } else {
                    aenderung += "\n\tGeloescht: " + "\n\t\t" +aktZeileAlterCode;
                    lastChange = "deleted";
                }
                aktZeileAlterCode = alterCodeReader.readLine();
            } */
            log(aenderung);
        } catch (IOException e) {
            return true;
        }
        return false;
    }
}
