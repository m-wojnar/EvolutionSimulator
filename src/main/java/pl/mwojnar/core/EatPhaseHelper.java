package pl.mwojnar.core;

import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.map.IWorldMap;
import pl.mwojnar.utils.Vector2d;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EatPhaseHelper {

    private final IWorldMap map;

    public EatPhaseHelper(IWorldMap map) {
        this.map = map;
    }

    public void eatPhase() {
        var allEdibles = map.getElements().stream()
                .filter(IMapElement::isEdible)
                .collect(Collectors.toList());

        for (var edibleElement : allEdibles)
            eatAtPosition(edibleElement, edibleElement.getPosition());
    }

    private void eatAtPosition(IMapElement edibleElement, Vector2d position) {
        var consumersAt = getConsumersAtPosition(position);

        if (!consumersAt.isEmpty()) {
            var strongestConsumers = getStrongestConsumers(consumersAt);
            int energyPortion = Configuration.getPlantEnergy() / strongestConsumers.size();
            strongestConsumers.forEach(a -> a.addEnergy(energyPortion));
            map.elementRemoved(edibleElement);
        }
    }

    private List<IMovableElement> getConsumersAtPosition(Vector2d position) {
        return map.elementsAt(position).stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .collect(Collectors.toList());
    }

    private List<IMovableElement> getStrongestConsumers(List<IMovableElement> consumers) {
        int maxEnergy = consumers.stream()
                .max(Comparator.comparing(IMovableElement::getLifeEnergy))
                .orElseThrow().getLifeEnergy();

        return consumers.stream()
                .filter(a -> a.getLifeEnergy() == maxEnergy)
                .collect(Collectors.toList());
    }
}
