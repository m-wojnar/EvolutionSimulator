package pl.mwojnar.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import pl.mwojnar.core.Configuration;
import pl.mwojnar.core.SimulationCore;
import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.map.IMapChangeObserver;
import pl.mwojnar.statistics.AnimalStatistics;
import pl.mwojnar.statistics.CumulativeStatistics;
import pl.mwojnar.statistics.StatisticsCore;
import pl.mwojnar.utils.FilesHelper;
import pl.mwojnar.utils.Vector2d;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class MainWindowController implements IMapChangeObserver, INextCycleObserver {

    public static final int EPOCHS_ON_CHART = 300;
    public static final Paint MAP_COLOR = Paint.valueOf("#ffffff");
    public static final Paint JUNGLE_COLOR = Paint.valueOf("#c2ffd2");
    public static final Paint ELEMENT_COLOR = Paint.valueOf("#117824");
    public static final Paint STRONGEST_MOVABLE_COLOR = Paint.valueOf("#fc0356");
    public static final Paint STRONG_MOVABLE_COLOR = Paint.valueOf("#9e0372");
    public static final Paint MOVABLE_COLOR = Paint.valueOf("#65039e");
    public static final Paint WEAK_MOVABLE_COLOR = Paint.valueOf("#034b9e");
    public static final Paint WEAKEST_MOVABLE_COLOR = Paint.valueOf("#020f52");
    public static final Paint MAIN_GENE_MOVABLE_COLOR = Paint.valueOf("#fcba03");

    private SimulationCore simulationCore;
    private StatisticsCore statisticsCore;
    private NextCycleTask task;
    private List<AnimalStatistics> animalStatistics;
    private List<CumulativeStatistics> cumulativeStatistics;

    @FXML private Label numberOfAnimals;
    @FXML private Label numberOfPlants;
    @FXML private Label meanLifeEnergy;
    @FXML private Label meanLifeTime;
    @FXML private Label meanNumberOfChildren;
    @FXML private Label epochNumber;

    @FXML private Label genes0;
    @FXML private Label genes1;
    @FXML private Label genes2;
    @FXML private Label genes3;
    @FXML private Label genes4;
    @FXML private Label genes5;
    @FXML private Label genes6;
    @FXML private Label genes7;

    @FXML private Canvas canvas;
    @FXML private Button resumeButton;
    @FXML private Button stopButton;

    @FXML private LineChart<Integer, Integer> chart;
    @FXML private NumberAxis xAxis;
    private XYChart.Series<Integer, Integer> animalsSeries;
    private XYChart.Series<Integer, Integer> plantsSeries;

    @FXML
    private void initialize() {
        this.animalStatistics = new LinkedList<>();
        this.cumulativeStatistics = new LinkedList<>();
        setupCanvas();

        simulationCore = new SimulationCore();
        statisticsCore = new StatisticsCore();
        simulationCore.getMap().addObserver(this);
        simulationCore.getMap().addObserver(statisticsCore);
        simulationCore.setupSimulation();

        task = new NextCycleTask(simulationCore);
        task.addObserver(this);

        animalsSeries = new XYChart.Series<>();
        animalsSeries.setName("Animals");
        chart.getData().add(animalsSeries);

        plantsSeries = new XYChart.Series<>();
        plantsSeries.setName("Plants");
        chart.getData().add(plantsSeries);

        var timer = new Timer();
        timer.scheduleAtFixedRate(task, Configuration.getTimeStep(), Configuration.getTimeStep());
    }

    private void setupCanvas() {
        // If map is not square, then resize canvas according to width/height ratio
        if (Configuration.getWidth() > Configuration.getHeight())
            canvas.setHeight((canvas.getHeight() * Configuration.getHeight()) / Configuration.getWidth());
        else if (Configuration.getWidth() < Configuration.getHeight())
            canvas.setWidth((canvas.getWidth() * Configuration.getWidth()) / Configuration.getHeight());

        // Fill map
        var graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(MAP_COLOR);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw jungle
        graphicsContext.setFill(JUNGLE_COLOR);
        graphicsContext.fillRect(
                (canvas.getWidth() * Configuration.getJungleLowerLeft().getX()) / Configuration.getWidth(),
                (canvas.getHeight() * Configuration.getJungleLowerLeft().getY()) / Configuration.getHeight(),
                (canvas.getWidth() * Configuration.getJungleWidth()) / Configuration.getWidth(),
                (canvas.getHeight() * Configuration.getJungleHeight()) / Configuration.getHeight()
        );
    }

    @FXML
    private void startSimulation() {
        task.start();
        resumeButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    private void stopSimulation() {
        task.stop();
        resumeButton.setDisable(false);
        stopButton.setDisable(true);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        updatePosition(oldPosition);
        updatePosition(newPosition);
    }

    @Override
    public void elementAdded(IMapElement element) {
        updatePosition(element.getPosition());
    }

    @Override
    public void elementRemoved(IMapElement element) {
        updatePosition(element.getPosition());
    }

    private void updatePosition(Vector2d position) {
        var elementsAt = simulationCore.getMap().elementsAt(position);
        var consumers = elementsAt.stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .sorted(Comparator.comparing(c -> -c.getLifeEnergy()))
                .collect(Collectors.toList());

        if (!consumers.isEmpty())
            drawMovable(consumers.get(0));
        else if (!elementsAt.isEmpty())
            drawElement(elementsAt.get(0));
        else
            clearField(position);
    }

    private void clearField(Vector2d position) {
        var color = MAP_COLOR;
        if (position.follows(Configuration.getJungleLowerLeft()) && position.precedes(Configuration.getJungleUpperRight()))
            color = JUNGLE_COLOR;    // we are inside jungle

        drawRectangle(position, color);
    }

    private void drawElement(IMapElement element) {
        drawCircle(element.getPosition(), ELEMENT_COLOR);
    }

    private void drawMovable(IMovableElement element) {
        drawCircle(element.getPosition(), energyColor(element.getLifeEnergy()));
    }

    private Paint energyColor(int lifeEnergy) {
        if (lifeEnergy <= 0)
            return WEAKEST_MOVABLE_COLOR;

        // Color of movable depends on energy level
        return switch ((2 * Configuration.getStartEnergy()) / lifeEnergy) {
            case 0 -> STRONGEST_MOVABLE_COLOR;
            case 1 -> STRONG_MOVABLE_COLOR;
            case 2 -> MOVABLE_COLOR;
            case 3 -> WEAK_MOVABLE_COLOR;
            default -> WEAKEST_MOVABLE_COLOR;
        };
    }

    private void drawCircle(Vector2d position, Paint fill) {
        if (simulationCore.getMap().isOutside(position))
            throw new IllegalArgumentException("Painting beyond canvas");

        canvas.getGraphicsContext2D().setFill(fill);
        canvas.getGraphicsContext2D().fillOval(
                (canvas.getWidth() * position.getX()) / Configuration.getWidth(),
                (canvas.getHeight() * position.getY()) / Configuration.getHeight(),
                canvas.getWidth() / Configuration.getWidth(),
                canvas.getHeight() / Configuration.getHeight()
        );
    }

    private void drawRectangle(Vector2d position, Paint fill) {
        if (simulationCore.getMap().isOutside(position))
            throw new IllegalArgumentException("Painting beyond canvas");

        canvas.getGraphicsContext2D().setFill(fill);
        canvas.getGraphicsContext2D().fillRect(
                (canvas.getWidth() * position.getX()) / Configuration.getWidth(),
                (canvas.getHeight() * position.getY()) / Configuration.getHeight(),
                (canvas.getWidth()) / Configuration.getWidth(),
                (canvas.getHeight()) / Configuration.getHeight()
        );
    }

    @Override
    public void cycleFinished() {
        statisticsCore.updateGenesCounter(
                simulationCore.getMap().getElements()
                    .stream()
                    .filter(IMovableElement.class::isInstance)
                    .map(IMovableElement.class::cast)
                    .collect(Collectors.toList())
        );

        updateStatistics();
        updateChart();
    }

    private void updateStatistics() {
        setText(numberOfAnimals, statisticsCore.getNumberOfAnimals());
        setText(numberOfPlants, statisticsCore.getNumberOfPlants());
        setText(meanLifeEnergy, statisticsCore.getMeanLifeEnergy());
        setText(meanLifeTime, statisticsCore.getMeanLifeTime());
        setText(meanNumberOfChildren, statisticsCore.getMeanNumberOfChildren());
        setText(epochNumber, task.getEpoch());

        var genes = statisticsCore.getGenesCounter();
        long genesNumber = genes.stream().mapToLong(i -> i).sum();
        setText(genes0, 100 * genes.get(0) / genesNumber);
        setText(genes1, 100 * genes.get(1) / genesNumber);
        setText(genes2, 100 * genes.get(2) / genesNumber);
        setText(genes3, 100 * genes.get(3) / genesNumber);
        setText(genes4, 100 * genes.get(4) / genesNumber);
        setText(genes5, 100 * genes.get(5) / genesNumber);
        setText(genes6, 100 * genes.get(6) / genesNumber);
        setText(genes7, 100 * genes.get(7) / genesNumber);
    }

    private void setText(Label label, Integer i) {
        label.setText(Integer.toString(i));
    }

    private void setText(Label label, Double d) {
        label.setText(Double.toString((double) Math.round(100 * d) / 100));
    }

    private void setText(Label label, Long l) {
        label.setText(Long.toString(l));
    }

    private void updateChart() {
        // Add new values to chart, move axis range and remove old data
        animalsSeries.getData().add(new XYChart.Data<>(task.getEpoch(), statisticsCore.getNumberOfAnimals()));
        plantsSeries.getData().add(new XYChart.Data<>(task.getEpoch(), statisticsCore.getNumberOfPlants()));

        xAxis.setLowerBound(Math.max(0, task.getEpoch() - EPOCHS_ON_CHART));
        xAxis.setUpperBound(xAxis.getLowerBound() + EPOCHS_ON_CHART);

        if (animalsSeries.getData().size() > EPOCHS_ON_CHART)
            animalsSeries.getData().remove(0);

        if (plantsSeries.getData().size() > EPOCHS_ON_CHART)
            plantsSeries.getData().remove(0);
    }

    @FXML
    private void animalsWithMainGene() {
        stopSimulation();

        // Paint on mainGeneMovableColor animals which main gene is equal to general main gene of population
        int mainGene = statisticsCore.mainGeneIndex();
        simulationCore.getMap().getElements()
                .stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .filter(m -> statisticsCore.mainGeneIndex(m.getGenesCounter()) == mainGene)
                .forEach(m -> drawCircle(m.getPosition(), MAIN_GENE_MOVABLE_COLOR));
    }

    @FXML
    private void selectAnimal(MouseEvent event) {
        int x = (int) ((event.getX() * Configuration.getWidth()) / canvas.getWidth());
        int y = (int) ((event.getY() * Configuration.getHeight()) / canvas.getHeight());

        simulationCore.getMap()
                .elementsAt(new Vector2d(x, y))
                .stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .max(Comparator.comparing(IMovableElement::getLifeEnergy))
                .ifPresent(movable -> {
                    stopSimulation();
                    showAnimalInfo(movable);
                    startAnimalStatistics(movable);
                });
    }

    private void showAnimalInfo(IMovableElement element) {
        var genes = element.getGenes().stream().sorted().collect(Collectors.toList());
        var alert = new Alert(
                Alert.AlertType.INFORMATION,
                "Position: " + element.getPosition() +
                "\nLife time: " + element.getLifeTime() + " epochs" +
                "\nEnergy: " + element.getLifeEnergy() +
                "\nCurrent number of children: " + element.getChildrenNumber() +
                "\nGenes: " + genes,
                ButtonType.OK
        );
        alert.setHeaderText("Selected animal");
        alert.showAndWait();
    }

    private void startAnimalStatistics(IMovableElement element) {
        var inputDialog = new TextInputDialog("100");
        inputDialog.setHeaderText("Animal statistics");
        inputDialog.setContentText("Enter number of epochs");

        if (inputDialog.showAndWait().isPresent()) {
            try {
                int n = textToUInt(inputDialog.getEditor());
                animalStatistics.add(new AnimalStatistics(element, task, task.getEpoch() + n));
                task.stopAfter(n);
            }
            catch (NumberFormatException e) {
                showWarning("Provided value must be positive integer", e);
                return;
            }
        }

        startSimulation();
    }

    @FXML
    private void statisticsAfterNEpochs() {
        stopSimulation();
        var inputDialog = new TextInputDialog("100");
        inputDialog.setHeaderText("Cumulative statistics");
        inputDialog.setContentText("Enter number of epochs");

        if (inputDialog.showAndWait().isPresent()) {
            try {
                int n = textToUInt(inputDialog.getEditor());
                cumulativeStatistics.add(new CumulativeStatistics(statisticsCore, task, task.getEpoch() + n));
                task.stopAfter(n);
            }
            catch (NumberFormatException e) {
                showWarning("Provided value must be positive integer", e);
                return;
            }
        }

        startSimulation();
    }

    private void showWarning(String text, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.YES);
        alert.showAndWait();
        System.out.println(e.getMessage());
    }

    @Override
    public void stoppedAfterNEpochs() {
        showAnimalStatistics();
        showCumulativeStatistics();
        startSimulation();
    }

    private void showAnimalStatistics() {
        var animalStatsToRemove = new LinkedList<AnimalStatistics>();
        for (var stats : animalStatistics) {
            // Select statistics that were meant to end this epoch
            if (stats.getEndEpoch() == task.getEpoch()) {
                animalStatsToRemove.add(stats);

                var alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Number of new children: " + stats.getChildrenNumber() +
                                "\nDie epoch: " + (stats.getDieEpoch().isPresent() ? stats.getDieEpoch().get() : "alive"),
                        ButtonType.OK
                );
                alert.setHeaderText("Animal statistics after " + (stats.getEndEpoch() - stats.getStartEpoch()) + " epochs");
                alert.showAndWait();
            }
        }

        // Remove finished statistics
        for (var toRemove : animalStatsToRemove) {
            animalStatistics.remove(toRemove);
            toRemove.getAnimal().removeObserver(toRemove);
        }
    }

    private void showCumulativeStatistics() {
        var cumStatsToRemove = new LinkedList<CumulativeStatistics>();
        for (var stats : cumulativeStatistics) {
            // Select statistics that were meant to end this epoch
            if (stats.getEndEpoch() == task.getEpoch()) {
                cumStatsToRemove.add(stats);
                var alert = new Alert(Alert.AlertType.INFORMATION, stats.toString(), ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Cumulative statistics after " + stats.getN() + " epochs." +
                        "\nDo you want to save them to the file?");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                    FilesHelper.saveToFile(stats.toString());
            }
        }

        // Remove finished statistics
        for (var toRemove : cumStatsToRemove) {
            cumulativeStatistics.remove(toRemove);
            task.removeObserver(toRemove);
        }
    }

    private int textToUInt(TextField textField) {
        int result = Integer.parseInt(textField.getText());
        if (result <= 0)
            throw new NumberFormatException("Provided value must be positive");

        return result;
    }

}
