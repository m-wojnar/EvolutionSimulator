package pl.mwojnar.controllers;

public interface INextCyclePublisher {
    void addObserver(INextCycleObserver observer);
    void removeObserver(INextCycleObserver observer);
}
