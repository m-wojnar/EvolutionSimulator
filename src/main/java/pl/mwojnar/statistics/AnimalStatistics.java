package pl.mwojnar.statistics;

import pl.mwojnar.controllers.NextCycleTask;
import pl.mwojnar.elements.IElementChangeObserver;
import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.utils.Vector2d;

import java.util.Optional;

public class AnimalStatistics implements IElementChangeObserver {

    private final IMovableElement animal;
    private final NextCycleTask task;
    private final int startChildrenNumber;
    private Optional<Integer> dieEpoch;
    private final int startEpoch;
    private final int endEpoch;

    public AnimalStatistics(IMovableElement animal, NextCycleTask task, int endEpoch) {
        this.animal = animal;
        this.animal.addObserver(this);
        this.task = task;
        this.startEpoch = task.getEpoch();
        this.endEpoch = endEpoch;
        this.startChildrenNumber = animal.getChildrenNumber();
        this.dieEpoch = Optional.empty();
    }

    @Override
    public void positionChanged(IMovableElement element, Vector2d oldPosition, Vector2d newPosition) { }

    @Override
    public void elementRemoved(IMapElement element) {
        dieEpoch = Optional.of(task.getEpoch());
    }

    public int getChildrenNumber() {
        return animal.getChildrenNumber() - startChildrenNumber;
    }

    public Optional<Integer> getDieEpoch() {
        return dieEpoch;
    }

    public int getEndEpoch() {
        return endEpoch;
    }

    public int getStartEpoch() {
        return startEpoch;
    }

    public IMovableElement getAnimal() {
        return animal;
    }
}
