package agh.cs.project.data;

public enum MapDirection {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public String toString (){
        switch (this) {
            case N: return "Północ";
            case S: return "Południe";
            case W: return "Zachód";
            case E: return "Wschód";
            case NE: return "Północny-wschód";
            case NW: return "Północny-zachód";
            case SE: return "Południowy-wschód";
            case SW: return "Południowy-zachód";
        }
        return null;
    }

    public MapDirection next (){
        switch (this) {
            case N: return NE;
            case NE: return E;
            case E: return SE;
            case SE: return S;
            case S: return SW;
            case SW: return W;
            case W: return NW;
            case NW: return N;
        }
        return null;
    }

    public Vector2d toUnitVector (){
        switch (this) {
            case N: return new Vector2d (0,1);
            case NE: return new Vector2d (1,1);
            case E: return new Vector2d (1,0);
            case SE: return new Vector2d (1,-1);
            case S: return new Vector2d (0,-1);
            case SW: return new Vector2d (-1,-1);
            case W: return new Vector2d (-1,0);
            case NW: return new Vector2d (-1,1);
        }
        return null;
    }

}


