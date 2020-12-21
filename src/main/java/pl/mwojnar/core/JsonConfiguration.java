package pl.mwojnar.core;

public class JsonConfiguration {

    private final int width;
    private final int height;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int jungleRatio;
    private final int startNumberOfAnimals;
    private final int startNumberOfGrasses;
    private final int timeStep;

    public JsonConfiguration(
            int width,
            int height,
            int startEnergy,
            int moveEnergy,
            int plantEnergy,
            int jungleRatio,
            int startNumberOfAnimals,
            int startNumberOfGrasses,
            int timeStep
    ) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.startNumberOfAnimals = startNumberOfAnimals;
        this.startNumberOfGrasses = startNumberOfGrasses;
        this.timeStep = timeStep;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getJungleRatio() {
        return jungleRatio;
    }

    public int getStartNumberOfAnimals() {
        return startNumberOfAnimals;
    }

    public int getStartNumberOfGrasses() {
        return startNumberOfGrasses;
    }

    public int getTimeStep() {
        return timeStep;
    }
}
