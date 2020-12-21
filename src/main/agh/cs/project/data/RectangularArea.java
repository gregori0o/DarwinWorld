package agh.cs.project.data;

import java.util.Set;
import java.util.HashSet;

public class RectangularArea {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Set<Vector2d> vectors = new HashSet<> ();

    public RectangularArea (int width, int height) {
        lowerLeft = new Vector2d(0,0);
        upperRight = new Vector2d(width-1, height-1);
        makeVectors ();
    }

    public RectangularArea (Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        makeVectors ();
    }

    private void makeVectors () {
        for (int i=lowerLeft.getX(); i<=upperRight.getX(); i++)
            for (int j=lowerLeft.getY(); j<=upperRight.getY(); j++)
                vectors.add(new Vector2d(i,j));
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public int getWidth () {
        return upperRight.getX() - lowerLeft.getX() + 1;
    }

    public int getHeight () {
        return upperRight.getY() - lowerLeft.getY() + 1;
    }

    public Set<Vector2d> getVectors() {
        return vectors;
    }
}
