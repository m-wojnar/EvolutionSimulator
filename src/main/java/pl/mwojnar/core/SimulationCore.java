package pl.mwojnar.core;

import pl.mwojnar.map.IWorldMap;
import pl.mwojnar.map.RectangularMap;

public class SimulationCore {

    private final IElementsCreator elementsCreator;
    private final EatPhaseHelper eatPhaseHelper;
    private final IWorldMap map;

    public SimulationCore() {
        this.map = new RectangularMap(Configuration.getMapUpperRight());
        this.elementsCreator = new NewElementsGenerator(map);
        this.eatPhaseHelper = new EatPhaseHelper(map);
    }

    public void setupSimulation() {
        elementsCreator.addFirstAnimals();
        elementsCreator.addFirstGrasses();
    }

    public void nextCycle() {
        map.removeDead();
        map.moveElements();
        eatPhaseHelper.eatPhase();
        elementsCreator.newAnimalsPhase();
        elementsCreator.plantGrasses();
    }

    public IWorldMap getMap() {
        return map;
    }

}