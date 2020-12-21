package agh.cs.project.map;

import agh.cs.project.data.*;
import agh.cs.project.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapStatus {
    private ConcurrentHashMap<Vector2d, IMapElement> elements = new ConcurrentHashMap<>();
    private int numberOfAnimals;
    private int numberOfPlants;
    private Genotype dominantGenes;
    private double averageEnergy;
    private double averageLengthOfLife;
    private int deaths;
    private double averageNumberOfChildren;
    private int maxEnergy;

    public MapStatus (JungleMap map, int numberOfAnimals, int startEnergy) {
        fillElements(map);
        this.numberOfAnimals=numberOfAnimals;
        numberOfPlants=0;
        averageEnergy=startEnergy;
        averageLengthOfLife=0;
        deaths=0;
        averageNumberOfChildren=0;
        maxEnergy=startEnergy;
        this.dominantGenes=dominantGenes(map);
    }

    public MapStatus (JungleMap map, MapStatus oldMapStatus) {
        fillElements(map);
        this.numberOfAnimals=oldMapStatus.numberOfAnimals;
        this.numberOfPlants=oldMapStatus.numberOfPlants;
        this.dominantGenes=oldMapStatus.dominantGenes;
        this.averageEnergy=oldMapStatus.averageEnergy;
        this.averageLengthOfLife=oldMapStatus.averageLengthOfLife;
        this.deaths=oldMapStatus.deaths;
        this.averageNumberOfChildren=oldMapStatus.averageNumberOfChildren;
        this.maxEnergy=oldMapStatus.maxEnergy;
    }


    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public double getAverageEnergy() {
        return averageEnergy;
    }

    public double getAverageLengthOfLife() {
        return averageLengthOfLife;
    }

    public Genotype getDominantGenes() {
        return dominantGenes;
    }

    public double getAverageNumberOfChildren() {
        return averageNumberOfChildren;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public ConcurrentHashMap<Vector2d, IMapElement> getElements() {
        return elements;
    }

    private void fillElements (JungleMap map) {
        elements.clear();
        maxEnergy=0;
        HashMap<Vector2d, Plant> plants = map.getPlants();
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        for (Vector2d position : animals.keySet()) {
            elements.put(position, map.getOneStrongest(position));
            maxEnergy = Math.max (maxEnergy, map.getOneStrongest(position).getEnergy());
        }

        for (Map.Entry<Vector2d, Plant> entry : plants.entrySet()) {
            if (!elements.containsKey(entry.getKey()))
                elements.put(entry.getKey(), entry.getValue());
        }
    }

    private Genotype dominantGenes (JungleMap map) {
        HashMap<Genotype, Integer> genotypes = new HashMap<>();
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
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
        int val = -1;
        for (Integer el : genotypes.values())
            if (val<el) val=el;
        for (Map.Entry<Genotype, Integer> entry : genotypes.entrySet())
            if (entry.getValue()==val) return entry.getKey();
        return null;
    }

    public void addDeath (int age) {
        averageLengthOfLife = (averageLengthOfLife*deaths+age)/(deaths+1);
        deaths++;
        numberOfAnimals--;
    }

    public void update (JungleMap map) {
        fillElements(map);
        this.dominantGenes=dominantGenes(map);
        HashMap<Vector2d, Plant> plants = map.getPlants();
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        numberOfPlants=plants.size();
        numberOfAnimals=0;
        averageNumberOfChildren=0;
        averageEnergy=0;
        for (ArrayList<Animal> list : animals.values())
            for (Animal animal : list) {
                numberOfAnimals++;
                averageNumberOfChildren+=animal.getChildren();
                averageEnergy+=animal.getEnergy();
            }
        averageEnergy=averageEnergy/numberOfAnimals;
        averageNumberOfChildren=averageNumberOfChildren/numberOfAnimals;
    }

}

