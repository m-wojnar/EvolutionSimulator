package pl.mwojnar.core;

import pl.mwojnar.elements.Animal;
import pl.mwojnar.utils.MapDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GenesGenerator implements IGenesCreator {

    private final Random random = new Random();

    public List<Integer> getNewGenes() {
        var genes = new ArrayList<Integer>(Animal.GENES_LENGTH);

        for (int j = 0; j < Animal.GENES_LENGTH; j++)
            genes.add(random.nextInt(MapDirection.NUMBER_OF_DIRECTIONS));

        return fixGenes(genes);
    }

    public List<Integer> mixGenes(List<Integer> genes1, List<Integer> genes2) {
        var genes = new ArrayList<Integer>(Animal.GENES_LENGTH);
        int firstCut = random.nextInt(Animal.GENES_LENGTH - 2);
        int secondCut = random.nextInt(Animal.GENES_LENGTH - firstCut - 2) + firstCut + 1;
        int partForGenes2 = random.nextInt(3);

        for (int i = 0; i < Animal.GENES_LENGTH; i++)
            genes.add(getProperPart(genes1, genes2, firstCut, secondCut, i, partForGenes2));

        return fixGenes(genes);
    }

    private Integer getProperPart(List<Integer> genes1, List<Integer> genes2, int firstCut, int secondCut, int i, int partForGenes2) {
        if (i <= firstCut)
            return partForGenes2 == 0 ? genes2.get(i) : genes1.get(i);
        else if (i <= secondCut)
            return partForGenes2 == 1 ? genes2.get(i) : genes1.get(i);
        else
            return partForGenes2 == 2 ? genes2.get(i) : genes1.get(i);
    }

    private List<Integer> fixGenes(List<Integer> genes) {
        List<Integer> missingGenes = getMissingGenes(genes);

        for (var gene : missingGenes) {
            int position;
            do {
                position = random.nextInt(Animal.GENES_LENGTH);
            } while (missingGenes.contains(genes.get(position)));
            genes.set(position, gene);
        }

        return genes;
    }

    private List<Integer> getMissingGenes(List<Integer> genes) {
        var counter = new ArrayList<Integer>(MapDirection.NUMBER_OF_DIRECTIONS);
        for (int i = 0; i < MapDirection.NUMBER_OF_DIRECTIONS; i++)
            counter.add(0);

        genes.forEach(g -> counter.set(g, counter.get(g) + 1));
        var missingGenes = new LinkedList<Integer>();

        for (int i = 0; i < counter.size(); i++) {
            if (counter.get(i) == 0)
                missingGenes.add(i);
        }

        return missingGenes;
    }
}
