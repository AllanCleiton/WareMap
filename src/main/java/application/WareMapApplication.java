package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class WareMapApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View.fxml")));
            var scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Comando para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Comando para Linux e macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar o terminal: " + e.getMessage());
        }
    }
}
