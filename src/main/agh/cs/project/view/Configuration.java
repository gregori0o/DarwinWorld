package agh.cs.project.view;

import com.google.gson.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Configuration extends JFrame {
    private int width;
    private int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int numberOfAnimals;
    private int numberOfPlants;
    private int timeStamp;

    private MapFrame firstMap;
    private MapFrame secondMap;

    public Configuration () {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("The Darwin World");
        this.setSize(1800,1000);
        this.setResizable(false);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(236, 207, 172));

    }

    public void start () {
        String url = "parametrs.json";
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;
        try {
            jsonObject = (JsonObject) parser.parse(new FileReader(url));
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex + " This file was not found: " + url);
            return;
        }

        width=jsonObject.get("width").getAsInt();
        height=jsonObject.get("height").getAsInt();
        startEnergy=jsonObject.get("startEnergy").getAsInt();
        moveEnergy=jsonObject.get("moveEnergy").getAsInt();
        plantEnergy=jsonObject.get("plantEnergy").getAsInt();
        jungleRatio=jsonObject.get("jungleRatio").getAsDouble();
        numberOfAnimals=jsonObject.get("numberOfAnimals").getAsInt();
        numberOfPlants=jsonObject.get("numberOfPlants").getAsInt();
        timeStamp=jsonObject.get("timeStamp").getAsInt();

        firstMap = new MapFrame(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, numberOfAnimals, numberOfPlants, timeStamp);
        secondMap = new MapFrame(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, numberOfAnimals, numberOfPlants, timeStamp);
        firstMap.setBounds(0,0,900,1000);
        secondMap.setBounds(900,0,900,1000);
        this.add(firstMap);
        this.add(secondMap);
        this.repaint();
    }
}

