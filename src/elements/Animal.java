package elements;

import map.*;
import data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement {
    private MapDirection direction;
    private Vector2d position;
    private int children = 0;
    private ArrayList <Animal> childrenList = new ArrayList<>();
    private int dayBirth;
    private int dayDead;
    public int energy;
    private Genotype genes;
    private Random generator = new Random();
    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal (JungleMap map, int startEnergy) {
        genes= new Genotype();
        dayBirth=0;
        energy = startEnergy;
        direction = MapDirection.N;
        rotation(generator.nextInt(genes.numberOfTypes));
        position=map.getAnimalPosition();
        map.addAnimal(this);
        addObserver(map);
    }

    public Animal (JungleMap map, Animal parent1, Animal parent2, int dayBirth) {
        this.dayBirth=dayBirth;
        int e1 = parent1.energy / 4;
        parent1.removeEnergy(e1);
        int e2 = parent2.energy / 4;
        parent2.removeEnergy(e2);
        energy = e1+e2;
        genes = new Genotype(parent1.getGenes(), parent2.getGenes());
        direction = MapDirection.N;
        rotation(generator.nextInt(genes.numberOfTypes));
        position = map.getSurroundingPosition (parent1.getPosition());
        map.addAnimal(this);
        addObserver(map);
        parent1.addChild(this);
        parent2.addChild(this);
    }

    public Animal (JungleMap map, Animal oldAnimal) {
        direction=oldAnimal.direction;
        position = new Vector2d(oldAnimal.getPosition().getX(),oldAnimal.getPosition().getY());
        children=oldAnimal.children;
        dayBirth=oldAnimal.dayBirth;
        energy=oldAnimal.energy;
        genes=oldAnimal.genes;
        generator = new Random();
        map.addAnimal(this);
        addObserver(map);
    }

    @Override
    public String toString() {
        return ""+this.energy;
    }

    public ArrayList<Animal> getChildrenList() {
        return childrenList;
    }

    public int getDayDead() {
        return dayDead;
    }

    public int getEnergy () {
        return energy;
    }

    public int getChildren() {
        return children;
    }

    public void addChild (Animal child) {
        childrenList.add(child);
        children++;
    }

    public void startTracking () {
        children=0;
        childrenList.clear();
    }

    public void addEnergy (int energy) {
        this.energy+=energy;
    }

    public void removeEnergy (int energy) {
        this.energy-=energy;
    }

    public Genotype getGenes() {
        return genes;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void move (JungleMap map) {
        positionWillChange(this);
        int gene = generator.nextInt(genes.size);
        rotation (genes.genotype[gene]);
        Vector2d step = direction.toUnitVector();
        position = position.add(step);
        positionChanged(this);
    }

    private void rotation (int angle) {
        while (angle > 0) {
            direction = direction.next();
            angle -= 1;
        }
    }

    public int dead (int currentDay) {
        dayDead = currentDay;
        return dayDead-dayBirth;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        for (IPositionChangeObserver arg : this.observers) {
            if (arg.equals(observer)) {
                this.observers.remove(arg);
                break;
            }
        }
    }

    private void positionWillChange (Animal animal) {
        for (IPositionChangeObserver arg : this.observers) arg.positionWillChange(animal);
    }

    private void positionChanged (Animal animal) {
        for (IPositionChangeObserver arg : this.observers) arg.positionChanged(animal);
    }

}

