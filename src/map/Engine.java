package map;

import data.*;
import elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class Engine {
    private JungleMap map;
    private MapStatus status;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int minimumEnergyToReproduce;
    private int day;

    public Engine (int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int numberOfAnimals, int numberOfPlants) {
        map = new JungleMap (width, height, jungleRatio);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        minimumEnergyToReproduce = startEnergy/2;
        day=0;
        try {
            for (int i = 0; i < numberOfAnimals; i++)
                new Animal(map, startEnergy);
            for (int i = 0; i < numberOfPlants; i++)
                addPlant();
        } catch (RuntimeException ex) {
            System.out.println(ex + "We can't add all of elements. Change parametrs.json.");
        }
        status = new MapStatus (map, numberOfAnimals, startEnergy);
    }

    public Engine (Engine oldEngine) {
        this.moveEnergy= oldEngine.moveEnergy;
        this.plantEnergy= oldEngine.plantEnergy;
        this.minimumEnergyToReproduce=oldEngine.minimumEnergyToReproduce;
        this.day=oldEngine.day;
        this.map=new JungleMap(oldEngine.getMap());
        this.status=new MapStatus(map, oldEngine.getStatus());
    }

    public JungleMap getMap() {
        return map;
    }

    public MapStatus getStatus() {
        return status;
    }

    public int getDay() {
        return day;
    }

    public void simulate () {
        removeDeadAnimal ();
        moveAnimal ();
        feeding ();
        reproduce ();
        addPlant ();
        day++;
        status.update(map);
    }

    private void removeDeadAnimal() {
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        Set<Animal> animalsSet = new HashSet<>();
        for (ArrayList<Animal> list : animals.values())
            for (Animal animal : list)
                if (animal.getEnergy()<=0) {
                    status.addDeath(animal.dead (day));
                    animalsSet.add(animal);
                }
        for (Animal animal : animalsSet)
            map.removeElement(animal);
    }

    private void moveAnimal() {
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        Set<Animal> animalsSet = new HashSet<>();
        for (ArrayList<Animal> list : animals.values())
            for (Animal animal : list)
                animalsSet.add(animal);
        for (Animal animal : animalsSet) {
            animal.move(map);
            animal.removeEnergy(moveEnergy);
        }
    }

    private void feeding() {
        HashMap<Vector2d, Plant> plants = map.getPlants();
        Set <Vector2d> positions = new HashSet<>();
        for (Vector2d position : plants.keySet())
            positions.add(position);
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        for (Vector2d position : positions) {
            if (animals.containsKey(position)) {
                map.removeElement(plants.get(position));
                ArrayList<Animal> listAnimals = map.getStrongest(position);
                int quantity = listAnimals.size();
                int portion = plantEnergy/quantity;
                for (Animal animal : listAnimals)
                    animal.addEnergy(portion);
            }
        }
    }

    private void reproduce() {
        HashMap<Vector2d, ArrayList<Animal>> animals = map.getAnimals();
        Set<Animal []> parentsSet = new HashSet<>();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animals.entrySet()) {
            if (entry.getValue().size()>=2) {
                Animal [] parents = map.getTwoStrongest(entry.getKey());
                if (parents[0].getEnergy()>=minimumEnergyToReproduce && parents[1].getEnergy()>=minimumEnergyToReproduce)
                    parentsSet.add(parents);
            }
        }
        for (Animal [] parents : parentsSet)
            new Animal(map, parents[0], parents[1], day);
    }

    private void addPlant() {
        try {
            new Plant(map, map.getJunglePosition());
        }
        catch (RuntimeException ex) {}; //brak miejsca na mapie na roślinę a więc pomijam to dodawanie rośliny
        try {
            new Plant(map, map.getGrasslandPosition());
        }
        catch (RuntimeException ex) {};

    }
}
