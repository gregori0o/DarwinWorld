package map;

import data.*;
import elements.*;

import java.util.*;

public class JungleMap extends AbstractWorldMap {
    private RectangularArea jungleArea;
    private Random generator = new Random();

    public JungleMap (int width, int height, double jungleRatio) {
        area=new RectangularArea(width,height);
        jungleArea = new RectangularArea (new Vector2d((int)(0.5*width*(1-jungleRatio)), (int)(0.5*height*(1-jungleRatio))),
                new Vector2d((int)(0.5*width*(1+jungleRatio)), (int)(0.5*height*(1+jungleRatio))));
    }

    public JungleMap (JungleMap oldJungleMap) {
        this.area= oldJungleMap.area;
        this.jungleArea= oldJungleMap.jungleArea;
        this.generator=new Random();
        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : oldJungleMap.getAnimals().entrySet()) {
            for (Animal element : entry.getValue()) {
                new Animal(this, element);
            }
        }
        for (Map.Entry<Vector2d, Plant> entry : oldJungleMap.getPlants().entrySet()) {
            new Plant (this, entry.getValue());
        }
    }

    public RectangularArea getJungleArea () {
        return jungleArea;
    }

    public Vector2d getAnimalPosition () {
        Set<Vector2d> freeVectors = new HashSet<>();
        for (Vector2d arg : area.getVectors()) {
            freeVectors.add(arg);
        }
        for (Vector2d arg : animals.keySet()) {
            freeVectors.remove(arg);
        }
        for (Vector2d arg : plants.keySet()) {
            freeVectors.remove(arg);
        }
        return freePosition (freeVectors);
    }

    public Vector2d getSurroundingPosition (Vector2d firstPosition) {
        Set<Vector2d> freeVectors = new HashSet<>();
        int x=firstPosition.getX();
        int y=firstPosition.getY();
        for (int i=x-1; i<=x+1; i++)
            for (int j=y-1; j<=y+1; j++) {
                if (i==x && j==y) continue;
                Vector2d vector = normalise(new Vector2d(i,j));
                if (animals.containsKey(vector) || plants.containsKey(vector)) continue;
                freeVectors.add(vector);
            }
        if (freeVectors.isEmpty())
            for (int i=x-1; i<=x+1; i++)
                for (int j=y-1; j<=y+1; j++) {
                    if (i==x && j==y) continue;
                    freeVectors.add(normalise(new Vector2d(i,j)));
                }
        return freePosition (freeVectors);
    }

    public Vector2d getJunglePosition () {
        Set<Vector2d> freeVectors = new HashSet<>();
        for (Vector2d arg : jungleArea.getVectors()) {
            freeVectors.add(arg);
        }
        for (Vector2d arg : animals.keySet()) {
            freeVectors.remove(arg);
        }
        for (Vector2d arg : plants.keySet()) {
            freeVectors.remove(arg);
        }
        return freePosition (freeVectors);
    }

    public Vector2d getGrasslandPosition () {
        Set<Vector2d> freeVectors = new HashSet<>();
        for (Vector2d arg : area.getVectors()) {
            freeVectors.add(arg);
        }
        for (Vector2d arg : jungleArea.getVectors()) {
            freeVectors.remove(arg);
        }
        for (Vector2d arg : animals.keySet()) {
            freeVectors.remove(arg);
        }
        for (Vector2d arg : plants.keySet()) {
            freeVectors.remove(arg);
        }
        return freePosition (freeVectors);
    }

    private Vector2d freePosition(Set<Vector2d> freeVectors) {
        if (freeVectors.isEmpty()) throw new RuntimeException("There is no free space on map.");
        int size = freeVectors.size();
        int index = generator.nextInt(size);
        Iterator<Vector2d> iterator = freeVectors.iterator();
        while (index>0) {
            iterator.next();
            index--;
        }
        return normalise(iterator.next());
    }

}

