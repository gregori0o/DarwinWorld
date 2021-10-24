package elements;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    protected final static int size = 32;
    protected final static int numberOfTypes = 8;
    protected int [] genotype = new int [size];
    private int [] genes = new int [numberOfTypes];
    private Random generator = new Random();

    public Genotype () {
        for (int i=0; i<numberOfTypes; i++)
            genes[i] = 1;
        for (int i=numberOfTypes; i<size; i++)
            genes[generator.nextInt(numberOfTypes)] ++;
        int index = 0;
        for (int i=0; i<numberOfTypes; i++)
            for (int j=0; j<genes[i]; j++) {
                genotype[index]=i;
                index++;
            }
    }

    public Genotype (Genotype genesA, Genotype genesB) {
        for (int i=0; i<numberOfTypes; i++)
            genes[i] = 0;
        int div1 = 1 + generator.nextInt(size-3);
        int div2 = div1 + 1 + generator.nextInt(size-div1-1);
        for (int i=0; i<size; i++) {
            if (i<=div1 || div2<i)
                genes[genesA.getGenotype()[i]] ++;
            else
                genes[genesB.getGenotype()[i]] ++;
        }
        for (int i=0; i<numberOfTypes; i++)
            if (genes[i]==0) {
                while (true) {
                    int tmp = generator.nextInt(numberOfTypes);
                    if (genes[tmp]>1) {
                        genes[tmp] --;
                        break;
                    }
                }
                genes[i]++;
            }
        int index = 0;
        for (int i=0; i<numberOfTypes; i++)
            for (int j=0; j<genes[i]; j++) {
                genotype[index]=i;
                index++;
            }
    }

    @Override
    public String toString() {
        String result = "gen:liczba ->";
        for (int i=0; i<numberOfTypes; i++)
            result = result + " " + i + ":" + genes[i] + ";";
        return result;
    }

    public int[] getGenes() {
        return genes;
    }

    public int[] getGenotype() {
        return genotype;
    }

    public boolean equals (Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Genotype))
            return false;
        int [] otherGenes = ((Genotype) other).getGenes();
        for (int i=0; i<numberOfTypes; i++)
            if (this.genes[i]!=otherGenes[i]) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genotype);
    }
}

