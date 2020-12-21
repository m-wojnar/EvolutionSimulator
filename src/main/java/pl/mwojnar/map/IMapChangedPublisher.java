package pl.mwojnar.map;

public interface IMapChangedPublisher {
    void addObserver(IMapChangeObserver observer);
    void removeObserver(IMapChangeObserver observer);
}
