package agh.cs.project.view;

import agh.cs.project.map.*;
import agh.cs.project.data.*;
import agh.cs.project.elements.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class AnimalTracking implements ActionListener {
    private JFrame newFrame = new JFrame();
    private JButton buttonSubmit;
    private JTextField submitField;
    private Engine cloneEngine;
    private Animal trackedAnimal;
    private int numberOfDays;
    private int numberOfChildren;
    private int numberOfDescendants;
    private int dayDead;
    private int dayStart;

    public AnimalTracking (Engine engine, Vector2d position) {
        newFrame.setSize(450,250);
        newFrame.getContentPane().setBackground(new Color(181, 243, 218));
        newFrame.setLayout(new FlowLayout());
        newFrame.setVisible(true);

        dayStart=engine.getDay();
        cloneEngine=new Engine(engine);
        trackedAnimal= (Animal) cloneEngine.getStatus().getElements().get(position);
        JLabel genotype = new JLabel();
        genotype.setText(trackedAnimal.getGenes().toString());
        newFrame.add(genotype);
        JLabel energy = new JLabel();
        energy.setText("Energia zwierzęcia wynosi: "+trackedAnimal.getEnergy());
        newFrame.add(energy);

        submitField = new JTextField();
        submitField.setPreferredSize(new Dimension(400,100));
        submitField.setFont(new Font("Consolas",Font.PLAIN,50));
        newFrame.add(submitField);
        buttonSubmit = new JButton();
        buttonSubmit.addActionListener(this);
        buttonSubmit.setText("Załaduj liczbę epok");
        newFrame.add(buttonSubmit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonSubmit) {
            String number = submitField.getText();
            try {
                numberOfDays = Integer.parseInt(number);
                start();
                showResult();
            }
            catch (NumberFormatException ex) {
                numberOfDays=0;
            }
            newFrame.dispose();
        }
    }

    private void addDescendants (Animal animal, Set<Animal> descendants) {
        for (Animal child : animal.getChildrenList()) {
            if (descendants.add(child))
                addDescendants(child, descendants);
        }
    }

    private int countDescendants () {
        Set<Animal> descendants = new HashSet<>();
        addDescendants(trackedAnimal, descendants);
        return descendants.size();
    }

    private void start () {
        trackedAnimal.startTracking();
        for (int i=0; i<numberOfDays; i++) {
            cloneEngine.simulate();
        }
        numberOfChildren=trackedAnimal.getChildren();
        if (trackedAnimal.getEnergy()==0)
            dayDead=trackedAnimal.getDayDead();
        else
            dayDead=0;
        numberOfDescendants=countDescendants();
    }

    private void showResult () {
        JFrame result = new JFrame();
        result.setSize(300,200);
        result.getContentPane().setBackground(new Color(181, 243, 218));
        result.setLayout(new FlowLayout());
        result.setVisible(true);

        JLabel children = new JLabel();
        children.setText("Liczba dzieci wynosi: "+numberOfChildren);
        result.add(children);
        JLabel descendants = new JLabel();
        descendants.setText("Liczba potomków wynosi: "+numberOfDescendants);
        result.add(descendants);
        JLabel dead = new JLabel();
        if (dayDead==0)
            dead.setText("Zwierzę nadal żyje");
        else
            dead.setText("Śmierć w epoce: "+dayDead+" ("+(dayDead-dayStart)+" eppok przeżył od zatrzymania)");
        result.add(dead);
    }
}

