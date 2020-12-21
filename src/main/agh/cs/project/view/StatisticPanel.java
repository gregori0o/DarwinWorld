package agh.cs.project.view;

import agh.cs.project.map.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class StatisticPanel extends JPanel {
    private XYSeries dataNumberOfAnimals = new XYSeries("Liczba zwierząt");
    private XYSeries dataNumberOfPlants = new XYSeries("Liczba roślin");
    private XYSeries dataAverageEnergy = new XYSeries("Średnia energia zwierząt");
    private XYSeries dataAverageLengthOfLife = new XYSeries("Średnia długość życia");
    private XYSeries dataAverageNumberOfChildren = new XYSeries("Średnia liczba dzieci");

    private JPanel genes;

    public StatisticPanel () {
        this.setBackground(new Color(255, 255, 255));
        makeCharts();
        genes=new JPanel();
        genes.setBounds(0,650,350,50);
        genes.setBackground(new Color(255,255,255));
        this.add(genes);
    }

    private void makeCharts () {
        XYSeriesCollection xyNumberOfAnimalAndPlants = new XYSeriesCollection();
        xyNumberOfAnimalAndPlants.addSeries(dataNumberOfAnimals);
        xyNumberOfAnimalAndPlants.addSeries(dataNumberOfPlants);
        XYSeriesCollection xyAverageEnergy = new XYSeriesCollection(dataAverageEnergy);
        XYSeriesCollection xyAverageLengthOfLife = new XYSeriesCollection(dataAverageLengthOfLife);
        XYSeriesCollection xyAverageNumberOfChildren = new XYSeriesCollection(dataAverageNumberOfChildren);

        XYDataset xyDatasetFirst =xyNumberOfAnimalAndPlants;
        JFreeChart lineGraphFirst = ChartFactory.createXYLineChart
                ("",  // Title
                        "",           // X-Axis label
                        "",           // Y-Axis label
                        xyDatasetFirst,          // Dataset
                        PlotOrientation.VERTICAL,        //Plot orientation
                        true,                //show legend
                        true,                // Show tooltips
                        false               //url show
                );
        ChartPanel chartPanelFirst = new ChartPanel(lineGraphFirst);
        chartPanelFirst.setBounds(0,0,350,200);
        this.add(chartPanelFirst);

        XYDataset xyDatasetSecond =xyAverageNumberOfChildren;
        JFreeChart lineGraphSecond = ChartFactory.createXYLineChart
                ("",  // Title
                        "",           // X-Axis label
                        "",           // Y-Axis label
                        xyDatasetSecond,          // Dataset
                        PlotOrientation.VERTICAL,        //Plot orientation
                        true,                //show legend
                        true,                // Show tooltips
                        false               //url show
                );
        ChartPanel chartPanelSecond = new ChartPanel(lineGraphSecond);
        chartPanelSecond.setBounds(0,200,350,150);
        this.add(chartPanelSecond);

        XYDataset xyDatasetThird =xyAverageEnergy;
        JFreeChart lineGraphThird = ChartFactory.createXYLineChart
                ("",  // Title
                        "",           // X-Axis label
                        "",           // Y-Axis label
                        xyDatasetThird,          // Dataset
                        PlotOrientation.VERTICAL,        //Plot orientation
                        true,                //show legend
                        true,                // Show tooltips
                        false               //url show
                );
        ChartPanel chartPanelThird = new ChartPanel(lineGraphThird);
        chartPanelThird.setBounds(0,350,350,150);
        this.add(chartPanelThird);

        XYDataset xyDatasetFourth =xyAverageLengthOfLife;
        JFreeChart lineGraphFourth = ChartFactory.createXYLineChart
                ("",  // Title
                        "",           // X-Axis label
                        "",           // Y-Axis label
                        xyDatasetFourth,          // Dataset
                        PlotOrientation.VERTICAL,        //Plot orientation
                        true,                //show legend
                        true,                // Show tooltips
                        false               //url show
                );
        ChartPanel chartPanelFourth = new ChartPanel(lineGraphFourth);
        chartPanelFourth.setBounds(0,500,350,150);
        this.add(chartPanelFourth);
    }

    public void updateCharts(MapStatus status, int day) {
        if (day>500) {
            dataNumberOfPlants.remove(0);
            dataNumberOfAnimals.remove(0);
            dataAverageNumberOfChildren.remove(0);
            dataAverageLengthOfLife.remove(0);
            dataAverageEnergy.remove(0);
        }
        dataNumberOfAnimals.add(day, status.getNumberOfAnimals());
        dataNumberOfPlants.add(day, status.getNumberOfPlants());
        dataAverageEnergy.add(day, status.getAverageEnergy());
        dataAverageLengthOfLife.add(day, status.getAverageLengthOfLife());
        dataAverageNumberOfChildren.add(day, status.getAverageNumberOfChildren());

        genes.removeAll();
        JLabel label = new JLabel();
        label.setText(status.getDominantGenes().toString());
        label.setBounds(5,0,350,50);
        genes.add(label);
        genes.repaint();
    }

}

