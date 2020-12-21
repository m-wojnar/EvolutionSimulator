package pl.mwojnar.controllers;

import javafx.application.Platform;
import pl.mwojnar.core.SimulationCore;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class NextCycleTask extends TimerTask implements INextCyclePublisher {

    private final List<INextCycleObserver> observers;
    private final SimulationCore core;

    private final List<Integer> stopAt;
    private boolean continueSimulation;
    private int epoch;

    public NextCycleTask(SimulationCore core) {
        this.core = core;
        this.continueSimulation = true;
        this.epoch = 0;
        this.stopAt = new LinkedList<>();
        this.observers = new LinkedList<>();
    }

    @Override
    public void run() {
        if (stopAt.contains(epoch)) {
            Platform.runLater(() -> {
                stopAt.remove(Integer.valueOf(epoch));
                continueSimulation = false;
                observers.forEach(INextCycleObserver::stoppedAfterNEpochs);
            });
        }

        if (continueSimulation) {
            Platform.runLater(() -> {
                core.nextCycle();
                observers.forEach(INextCycleObserver::cycleFinished);
                epoch++;
            });
        }
    }

    @Override
    public void addObserver(INextCycleObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(INextCycleObserver observer) {
        observers.remove(observer);
    }

    public void stop() {
        continueSimulation = false;
    }

    public void start() {
        continueSimulation = true;
    }

    public int getEpoch() {
        return epoch;
    }

    public void stopAfter(int n) {
        stopAt.add(epoch + n);
    }
}
