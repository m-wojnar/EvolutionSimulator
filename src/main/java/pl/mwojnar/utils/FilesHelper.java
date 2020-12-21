package pl.mwojnar.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

public class FilesHelper {

    public static void saveToFile(String content) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        var file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                var writer = new PrintWriter(file);
                writer.println(content);
                writer.close();
            } catch (IOException e) {
                showWarning("File is incorrect", e);
            }
        }
    }

    public static Optional<String> readFromFile() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        var file = fileChooser.showOpenDialog(null);
        StringBuilder content = new StringBuilder();

        if (file == null)
            return Optional.empty();

        try {
            var scanner = new Scanner(file);
            while (scanner.hasNextLine())
                content.append(scanner.nextLine());
        } catch (FileNotFoundException e) {
            showWarning("File was not found", e);
            return Optional.empty();
        }

        return Optional.of(content.toString());
    }

    private static void showWarning(String text, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.YES);
        alert.showAndWait();
        System.out.println(e.getMessage());
    }
}
