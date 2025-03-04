package controller;

import application.WareMapApplication;
import controller.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.IntegrationService;
import model.services.SeparationFactory;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ViewController implements Initializable {
    SeparationFactory factory = null;
    String path = "src/main/resources/temp"; //src/main/resources/temp
    Properties log = new Properties();

    @FXML
    private MenuItem btInState;

    @FXML
    public void onMenuItemInStateAction(){
        loadView("/inState.fxml", InStateController::upDateTableView);
    }

    private synchronized <T>  void loadView(String absolutName, Consumer<T> initializerAction){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
        try {
            VBox newVBox = loader.load();
            var mainScene = WareMapApplication.getMainScene(); //pega a mainScene principal
            var mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();  //Pega uma referencia para o ScrollPane da MainView

            var mainMenu = mainVBox.getChildren().getFirst(); // guarda o primeiro filho do mainVBox, que é o MenuBar

            mainVBox.getChildren().clear(); //exclue todos os filhos da mainVBox

            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializerAction.accept(controller);

        } catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Erro ao carregar a pagina", e.getMessage(), Alert.AlertType.ERROR);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        charger();
    }

    private  void charger(){

        try (InputStream input = new FileInputStream(path + "/config/logs/log.properties")){
            log.load(input);

            if(!(log.getProperty("exit").equals("true"))) {

                Alert choice = new Alert(Alert.AlertType.CONFIRMATION, "Deseja iniciar a partir da ultima cessão?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = choice.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.YES){
                    factory = new SeparationFactory(new IntegrationService(path, 1), null);
                }else {
                    factory = new SeparationFactory(new IntegrationService(path, 0), null);
                }
            }else {
                factory = new SeparationFactory(new IntegrationService(path, 0), null);
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
}
