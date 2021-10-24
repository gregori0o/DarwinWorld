package view;

import map.*;
import data.*;
import elements.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapPanel extends JPanel {
    private int ratio;

    public MapPanel(int ratio) {
        this.setBackground(new Color(57, 245, 83));
        this.ratio= ratio;
    }

    public void drawMap (MapStatus status, JungleMap map) {
        this.removeAll();
        int maxEnergy = status.getMaxEnergy();
        if (maxEnergy==0) maxEnergy=1;
        ConcurrentHashMap<Vector2d, IMapElement> elements = status.getElements();
        for (Map.Entry<Vector2d, IMapElement> entry : elements.entrySet()) {
            if (entry.getValue() instanceof Plant) {
                JPanel plant = new JPanel ();
                plant.setBackground(new Color(16, 182, 38));
                plant.setBounds(ratio*entry.getKey().getX(), ratio*entry.getKey().getY(), ratio, ratio);
                this.add(plant);
            }
            if (entry.getValue() instanceof Animal) {
                int currentEnergy = ((Animal) entry.getValue()).getEnergy();
                int blue = 255-(255*currentEnergy/maxEnergy);
                int visibility = 100+(155*currentEnergy/maxEnergy);
                JPanel animal = new JPanel ();
                animal.setBackground(new Color(0, 0, blue, visibility));
                animal.setBounds(ratio*entry.getKey().getX(), ratio*entry.getKey().getY(), ratio, ratio);
                this.add(animal);
            }
        }
        JPanel jungle = new JPanel ();
        jungle.setBackground(new Color(16, 92, 29));
        jungle.setBounds(ratio*map.getJungleArea().getLowerLeft().getX(), ratio*map.getJungleArea().getLowerLeft().getY(), ratio*map.getJungleArea().getWidth(), ratio*map.getJungleArea().getHeight());
        this.add(jungle);
        this.repaint();
    }

    public void markDominantGenotype (Genotype dominantGenotype, JungleMap map) {
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet())
            for (Animal animal : entry.getValue()) {
                if (dominantGenotype.equals(animal.getGenes())) {
                    JPanel animalPanel = new JPanel ();
                    animalPanel.setBackground(new Color(239, 54, 0));
                    animalPanel.setBounds(ratio*entry.getKey().getX(), ratio*entry.getKey().getY(), ratio, ratio);
                    this.add(animalPanel, 0);
                    break;
                }
            }
        this.repaint();
    }
}

