package pl.mwojnar.core;

import java.util.List;

public interface IGenesCreator {
    List<Integer> getNewGenes();
    List<Integer> mixGenes(List<Integer> genes1, List<Integer> genes2);
}
