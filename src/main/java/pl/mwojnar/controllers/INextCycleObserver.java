package pl.mwojnar.controllers;

public interface INextCycleObserver {
    void cycleFinished();
    void stoppedAfterNEpochs();
}
