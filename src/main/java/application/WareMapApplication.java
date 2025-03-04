package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.Objects;

public class WareMapApplication extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/View.fxml")));
            ScrollPane scrollPane = loader.load();

            //define a largura e a altura do scroll pane como 100% da tela
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);
            stage.setTitle("WareMap Application");
            stage.setScene(mainScene);

            /*stage.setOnCloseRequest(event -> {
                System.out.println(" Saindo.");
                log.setProperty("exit", "true");
                try (OutputStream output = new FileOutputStream(path + "/config/logs/log.properties")) {
                    // Armazena o objeto Properties no arquivo com uma possível mensagem de comentários (null nesse caso).
                    log.store(output, null);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                factory.getRepository().shutDownDb(path);
            });*/
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Scene getMainScene(){
        return mainScene;
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
