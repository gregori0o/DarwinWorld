package map;

import elements.Animal;

public interface IPositionChangeObserver {

    void positionWillChange (Animal animal);
    void positionChanged (Animal animal);
}