package agh.cs.project.map;

import agh.cs.project.elements.Animal;

public interface IPositionChangeObserver {

    void positionWillChange (Animal animal);
    void positionChanged (Animal animal);
}