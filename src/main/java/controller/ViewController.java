package controller;

import application.WareMapApplication;
import controller.util.Alerts;
import controller.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private MenuItem btOpenFile;

    @FXML
    private MenuItem btInState;

    @FXML
    public void onMenuItemOpenAction(ActionEvent event){
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OpenFileForm.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage dialogState = new Stage();
        dialogState.setTitle("Abrir Arquivo");
        dialogState.setScene(new Scene(pane));
        dialogState.setResizable(false); //fala se a janela pode ser redimencionada.
        dialogState.initOwner(stage);
        dialogState.initModality(Modality.APPLICATION_MODAL); //fala que a janela vai ser modal, que deixa ela travada na frente da outra.
        dialogState.initModality(Modality.WINDOW_MODAL);
        dialogState.showAndWait();
    }



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
