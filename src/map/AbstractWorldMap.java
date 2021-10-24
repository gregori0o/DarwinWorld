package map;

import data.*;
import elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public abstract class AbstractWorldMap implements IPositionChangeObserver{
    protected RectangularArea area;
    protected HashMap<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected HashMap<Vector2d, Plant> plants = new HashMap<>();

    public Vector2d normalise (Vector2d position) {
        int x=position.getX();
        while (x<0) {
            x+=area.getWidth();
        }
        x=x%area.getWidth();
        int y=position.getY();
        while (y<0) {
            y+=area.getHeight();
        }
        y=y%area.getHeight();
        return new Vector2d(x,y);
    }

    public RectangularArea getArea() {
        return area;
    }

    public HashMap<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }

    public HashMap<Vector2d, Plant> getPlants() {
        return plants;
    }

    public void addAnimal (Animal element) {
        Vector2d position = normalise(element.getPosition());
        if (!animals.containsKey(position))
            animals.put(position, new ArrayList<>());
        animals.get(position).add(element);
    }

    public void addPlant (Plant element) {
        Vector2d position = normalise(element.getPosition());
        plants.put(position, element);
    }

    public void removeElement (IMapElement element) {
        Vector2d position = normalise(element.getPosition());
        if (element instanceof Plant) {
            plants.remove(position);
        }
        else if (element instanceof Animal) {
            animals.get(position).remove(element);
            if (animals.get(position).isEmpty())
                animals.remove(position);
        }
    }

    public ArrayList<Animal> getStrongest (Vector2d position) {
        ArrayList<Animal> listAnimals = new ArrayList<>();
        int energy = -1;
        for (Animal animal : animals.get(position)) {
            energy = Math.max (energy, animal.getEnergy());
        }
        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == energy)
                listAnimals.add(animal);
        }
        return listAnimals;
    }

    public Animal getOneStrongest (Vector2d position) {
        int energy = -1;
        for (Animal animal : animals.get(position)) {
            energy = Math.max (energy, animal.getEnergy());
        }
        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == energy)
                return animal;
        }
        return null;
    }

    public Animal [] getTwoStrongest (Vector2d position) {
        Animal [] listAnimals = new Animal[2];
        int index = 0;
        int energy = -1;
        for (Animal animal : animals.get(position)) {
            energy = Math.max (energy, animal.getEnergy());
        }
        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == energy) {
                listAnimals[index]=animal;
                index++;
            }
            if (index == 2) return listAnimals;
        }
        int maxEnergy = energy;
        energy = -1;
        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == maxEnergy) continue;
            energy = Math.max (energy, animal.getEnergy());
        }
        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == energy) {
                listAnimals[index]=animal;
                break;
            }
        }
        return listAnimals;
    }

    public void positionWillChange (Animal animal) {
        removeElement(animal);
    }
    public void positionChanged (Animal animal) {
        addAnimal(animal);
    }
}

