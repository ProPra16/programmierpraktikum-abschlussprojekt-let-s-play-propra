package de.hhu.propra;

import de.hhu.propra.model.Analyse;

/**
 * Created by Freddy on 07.07.2016.
 */
public class Analysierer {

    private long millisSeitStart;
    private long millisInGreen;
    private long millisInRed;
    private Analyse analyse;


    public Analysierer(long millisSeitStart, long millisInGreen, long millisInRed){
        this.millisInGreen = millisInGreen;
        this.millisSeitStart = millisSeitStart;
        this.millisInRed = millisInRed;
    }

    public void analysiere(){

    }
}
