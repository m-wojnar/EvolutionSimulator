package pl.mwojnar.elements;

import java.util.List;

public interface IMovableElement extends IMapElement, IElementChangedPublisher {
    void move(int moveEnergy);
    void checkState();
    int getLifeTime();

    int getLifeEnergy();
    void addEnergy(int energy);

    int takeEnergyForNewElement();
    int getChildrenNumber();

    List<Integer> getGenes();
    List<Long> getGenesCounter();
}
