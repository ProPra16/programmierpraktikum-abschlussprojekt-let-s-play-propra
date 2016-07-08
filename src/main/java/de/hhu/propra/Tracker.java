package de.hhu.propra;

import de.hhu.propra.model.Analyse;
import de.hhu.propra.view.OberflaecheController;

import java.io.FileWriter;
import java.io.IOException;
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
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String uhrzeit = sdf.format(new Date());

        try{
            String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(1,path.lastIndexOf("/"));
            path = path + "/log/";
            FileWriter writer = new FileWriter(path + "log.txt");
            writer.append(uhrzeit + ": " + changes);
            writer.close();
        } catch (Exception e){
            ofController.appendKonsoleText("Tracking fehlgeschlagen: " + e);
        }
    }
}
