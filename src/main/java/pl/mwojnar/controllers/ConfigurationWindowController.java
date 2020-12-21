package pl.mwojnar.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.mwojnar.Main;
import pl.mwojnar.core.Configuration;
import pl.mwojnar.core.JsonConfiguration;
import pl.mwojnar.utils.FilesHelper;

import java.io.IOException;

public class ConfigurationWindowController {

    @FXML private TextField widthField;
    @FXML private TextField heightField;
    @FXML private TextField startEnergyField;
    @FXML private TextField moveEnergyField;
    @FXML private TextField plantEnergyField;
    @FXML private TextField jungleRatioField;
    @FXML private TextField numOfAnimalsField;
    @FXML private TextField numOfPlantsField;
    @FXML private TextField timeStepField;

    @FXML
    private void startTwoAnimationsAction(ActionEvent event) throws IOException {
        createConfigurationAndMainWindow(event, 2);
    }

    @FXML
    private void startAnimationAction(ActionEvent event) throws IOException {
        createConfigurationAndMainWindow(event, 1);
    }

    private void createConfigurationAndMainWindow(ActionEvent event, int numberOfWindows) throws IOException {
        try {
            createConfigurationFromInput();
        } catch (NumberFormatException e) {
            showWarning("All fields must contain only positive integers", e);
            return;
        }

        for (int i = 0; i < numberOfWindows; i++)
            createMainWindow();

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    private void showWarning(String text, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.YES);
        alert.showAndWait();
        System.out.println(e.getMessage());
    }

    private void createMainWindow() throws IOException {
        var stage = new Stage();
        var scene = new Scene(FXMLLoader.load(Main.class.getResource("main.fxml")));
        stage.getIcons().add(new Image(Main.class.getResource("icon.png").toString()));
        stage.setResizable(false);
        stage.setTitle("Evolution simulator");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void readFromJson() {
        var content = FilesHelper.readFromFile();
        if (content.isEmpty())
            return;

        try {
            var gson = new Gson();
            var jsonConfiguration = gson.fromJson(content.get(), JsonConfiguration.class);
            jsonConfigurationToInputFields(jsonConfiguration);
        } catch (JsonSyntaxException e) {
            showWarning("Incorrect json file", e);
        }
    }

    private void jsonConfigurationToInputFields(JsonConfiguration jsonConfiguration) {
        if (jsonConfiguration == null)
            throw new JsonSyntaxException("Null Json configuration");

        widthField.setText(Integer.toString(jsonConfiguration.getWidth()));
        heightField.setText(Integer.toString(jsonConfiguration.getHeight()));
        startEnergyField.setText(Integer.toString(jsonConfiguration.getStartEnergy()));
        moveEnergyField.setText(Integer.toString(jsonConfiguration.getMoveEnergy()));
        plantEnergyField.setText(Integer.toString(jsonConfiguration.getPlantEnergy()));
        jungleRatioField.setText(Integer.toString(jsonConfiguration.getJungleRatio()));
        numOfAnimalsField.setText(Integer.toString(jsonConfiguration.getStartNumberOfAnimals()));
        numOfPlantsField.setText(Integer.toString(jsonConfiguration.getStartNumberOfGrasses()));
        timeStepField.setText(Integer.toString(jsonConfiguration.getTimeStep()));
    }

    @FXML
    private void writeToJson() {
        try {
            FilesHelper.saveToFile(inputFieldsToJson());
        } catch (NumberFormatException e) {
            showWarning("All fields must contain only positive integers", e);
        }
    }

    private String inputFieldsToJson() {
        var gson = new Gson();
        var jsonConfiguration = new JsonConfiguration(
                textToUInt(widthField),
                textToUInt(heightField),
                textToUInt(startEnergyField),
                textToUInt(moveEnergyField),
                textToUInt(plantEnergyField),
                textToUInt(jungleRatioField),
                textToUInt(numOfAnimalsField),
                textToUInt(numOfPlantsField),
                textToUInt(timeStepField)
        );

        return gson.toJson(jsonConfiguration);
    }

    private void createConfigurationFromInput() {
        Configuration.createInstance(
                textToUInt(widthField),
                textToUInt(heightField),
                textToUInt(startEnergyField),
                textToUInt(moveEnergyField),
                textToUInt(plantEnergyField),
                textToUInt(jungleRatioField),
                textToUInt(numOfAnimalsField),
                textToUInt(numOfPlantsField),
                textToUInt(timeStepField)
        );
    }

    private int textToUInt(TextField textField) {
        int result = Integer.parseInt(textField.getText());
        if (result < 0)
            throw new NumberFormatException("Provided values cannot be negative");

        return result;
    }
}
