package de.hhu.propra.model;

import javafx.scene.chart.PieChart;

/**
 * Created by Freddy on 07.07.2016.
 */
public class Analyse {
    private int sekundenInGreen;
    private int sekundenInRed;
    private int sekundenSeitStart;
    private PieChart chart;

    public Analyse(long millisSeitStart, long millisInGreen, long millisInRed){
        this.sekundenInGreen = (int) millisInGreen*1000;
    }





    /* ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("Grapefruit", 13),
                    new PieChart.Data("Oranges", 25),
                    new PieChart.Data("Plums", 10),
                    new PieChart.Data("Pears", 22),
                    new PieChart.Data("Apples", 30)); */

}
