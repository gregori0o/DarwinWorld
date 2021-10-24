package elements;

import map.*;
import data.*;

public class Plant implements IMapElement {
    private Vector2d position;

    public Plant (JungleMap map, Vector2d position) {
        this.position=position;
        map.addPlant(this);
    }

    public Plant (JungleMap map, Plant oldPlant) {
        position = new Vector2d(oldPlant.getPosition().getX(),oldPlant.getPosition().getY());
        map.addPlant(this);
    }

    public Vector2d getPosition() {
        return position;
    }
}

