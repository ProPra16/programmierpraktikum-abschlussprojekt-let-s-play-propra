package de.hhu.propra;

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
    private long millisInGreen;
    private long millisInRed;

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

    public void bearbeitungBeenden(){
        millisSeitStart = System.currentTimeMillis() - millisBeiStart;
        Analysierer analysierer = new Analysierer(millisSeitStart, millisInGreen, millisInRed);
        // analysierer
    }

    public void log(String changes){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String uhrzeit = sdf.format(new Date());

        try{
            FileWriter writer = new FileWriter("");
            writer.append(uhrzeit + ": " + changes);
            writer.close();
        } catch (IOException e){
            ofController.appendKonsoleText("Tracking fehlgeschlagen: " + e);
        }
    }
}
