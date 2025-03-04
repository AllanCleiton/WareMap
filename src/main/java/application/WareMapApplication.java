package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import model.services.IntegrationService;
import model.services.SeparationFactory;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class WareMapApplication extends Application {
    public static SeparationFactory factory = null;
    public static String path = "src/main/resources/temp"; //src/main/resources/temp
    public static Properties log = new Properties();
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

            stage.setOnCloseRequest(event -> {
                factory.sc.close();
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
            });
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Scene getMainScene(){
        return mainScene;
    }

    public static void charger(){

        try (InputStream input = new FileInputStream(path + "/config/logs/log.properties")){
            log.load(input);

            if(!(log.getProperty("exit").equals("true"))) {

                Alert choice = new Alert(Alert.AlertType.CONFIRMATION, "Deseja iniciar a partir da ultima cessão?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = choice.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.YES){
                    factory = new SeparationFactory(new IntegrationService(path, 1));
                }else {
                    factory = new SeparationFactory(new IntegrationService(path, 0));
                }
            }else {
                factory = new SeparationFactory(new IntegrationService(path, 0));
            }

            log.setProperty("exit", "false");
            try (OutputStream output = new FileOutputStream(path + "/config/logs/log.properties")) {
                // Armazena o objeto Properties no arquivo com uma possível mensagem de comentários (null nesse caso).
                log.store(output, null);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Erro ->" + e.getMessage());
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
