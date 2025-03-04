package controller;

import application.WareMapApplication;
import controller.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.SeparationFactory;

import java.io.IOException;
import java.net.URL;
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

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
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
            Alerts.showAlert("IO Exception", "Erro ao carregar a pagina", e.getMessage(), Alert.AlertType.ERROR);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WareMapApplication.charger();
    }


}
