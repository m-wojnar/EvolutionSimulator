package pl.mwojnar.elements;

public interface IElementChangedPublisher {
    void addObserver(IElementChangeObserver observer);
    void removeObserver(IElementChangeObserver observer);
}
