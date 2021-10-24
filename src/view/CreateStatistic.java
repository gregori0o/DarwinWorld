package view;

import map.*;
import data.*;
import elements.*;

import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateStatistic implements ActionListener {
    protected class Statistic {
        protected double numberOfAnimals;
        protected double numberOfPlants;
        protected double averageEnergy;
        protected double averageLengthOfLife;
        protected double averageNumberOfChildren;
        protected int [] dominantGenotype;
    }

    private JFrame newFrame = new JFrame();
    private JButton buttonSubmit;
    private JTextField submitField;
    private int numberOfDays;
    private Engine cloneEngine;
    private Statistic statistic;
    private HashMap<Genotype, Integer> genotypes = new HashMap<>();

    public CreateStatistic (Engine engine) {
        newFrame.setSize(450,200);
        newFrame.getContentPane().setBackground(new Color(181, 243, 218));
        newFrame.setLayout(new FlowLayout());
        newFrame.setVisible(true);
        buttonSubmit = new JButton();
        buttonSubmit.addActionListener(this);
        buttonSubmit.setText("Załaduj liczbę epok");
        newFrame.add(buttonSubmit);
        submitField = new JTextField();
        submitField.setPreferredSize(new Dimension(400,100));
        submitField.setFont(new Font("Consolas",Font.PLAIN,50));
        newFrame.add(submitField);
        cloneEngine=new Engine(engine);
        statistic = new Statistic();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonSubmit) {
            String number = submitField.getText();
            try {
                numberOfDays = Integer.parseInt(number);
            }
            catch (NumberFormatException ex) {
                numberOfDays=0;
            }
            newFrame.dispose();
            start();
            makeFile();
        }
    }

    private void start () {
        if (numberOfDays==0) {
            statistic.numberOfAnimals=cloneEngine.getStatus().getNumberOfAnimals();
            statistic.numberOfPlants=cloneEngine.getStatus().getNumberOfPlants();
            statistic.dominantGenotype=cloneEngine.getStatus().getDominantGenes().getGenotype();
            statistic.averageEnergy=cloneEngine.getStatus().getAverageEnergy();
            statistic.averageLengthOfLife=cloneEngine.getStatus().getAverageLengthOfLife();
            statistic.averageNumberOfChildren=cloneEngine.getStatus().getAverageNumberOfChildren();
            return;
        }
        statistic.numberOfAnimals=0;
        statistic.numberOfPlants=0;
        statistic.averageEnergy=0;
        statistic.averageLengthOfLife=0;
        statistic.averageNumberOfChildren=0;
        for (int i=0; i<numberOfDays; i++) {
            cloneEngine.simulate();
            statistic.numberOfAnimals+=cloneEngine.getStatus().getNumberOfAnimals();
            statistic.numberOfPlants+=cloneEngine.getStatus().getNumberOfPlants();
            statistic.averageEnergy+=cloneEngine.getStatus().getAverageEnergy();
            statistic.averageLengthOfLife+=cloneEngine.getStatus().getAverageLengthOfLife();
            statistic.averageNumberOfChildren+=cloneEngine.getStatus().getAverageNumberOfChildren();
            addGenes();
        }
        statistic.numberOfAnimals/=numberOfDays;
        statistic.numberOfPlants/=numberOfDays;
        statistic.averageEnergy/=numberOfDays;
        statistic.averageLengthOfLife/=numberOfDays;
        statistic.averageNumberOfChildren/=numberOfDays;
        int val = -1;
        for (Integer el : genotypes.values())
            if (val<el) val=el;
        for (Map.Entry<Genotype, Integer> entry : genotypes.entrySet())
            if (entry.getValue()==val) {
                statistic.dominantGenotype=entry.getKey().getGenotype();
                break;
            }
    }

    private void addGenes () {
        HashMap<Vector2d, ArrayList<Animal>> animals = cloneEngine.getMap().getAnimals();
        for (ArrayList<Animal> list : animals.values())
            for (Animal animal : list) {
                if (genotypes.containsKey(animal.getGenes())) {
                    int tmp=genotypes.get(animal.getGenes());
                    tmp++;
                    genotypes.remove(animal.getGenes());
                    genotypes.put(animal.getGenes(), tmp);
                }
                else
                    genotypes.put(animal.getGenes(), 1);
            }
    }

    private void makeFile () {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("statistic.json")) {
            gson.toJson(statistic, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

