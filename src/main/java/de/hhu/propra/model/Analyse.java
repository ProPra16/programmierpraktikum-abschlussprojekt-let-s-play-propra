package de.hhu.propra.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 * Created by Freddy on 07.07.2016.
 */
public class Analyse {
    private int sekundenInGreen = 0;
    private int sekundenInRed = 0;
    private int sekundenSeitStart;
    private PieChart chart;

    public Analyse(long millisSeitStart, long millisInGreen, long millisInRed){
        this.sekundenInGreen = (int) millisInGreen*1000;
        this.sekundenInRed = (int) millisInRed*1000;
        this.sekundenSeitStart = (int) millisSeitStart*1000;
        erstelleDiagramm();
    }

    private void erstelleDiagramm(){
        PieChart.Data rot = new PieChart.Data("Sekunden in Phase Red", sekundenInRed);
        PieChart.Data gruen = new PieChart.Data("Sekunden in Phase Green", sekundenInGreen);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(rot,gruen);
        chart = new PieChart(pieChartData);
        chart.setLegendVisible(false);

        rot.getNode().setStyle("-fx-pie-color: red");
        gruen.getNode().setStyle("-fx-pie-color: green");
    }

    public PieChart getChart(){
        return this.chart;
    }


    /* ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("Grapefruit", 13),
                    new PieChart.Data("Oranges", 25),
                    new PieChart.Data("Plums", 10),
                    new PieChart.Data("Pears", 22),
                    new PieChart.Data("Apples", 30)); */

}