package controller;

import application.WareMapApplication;
import controller.util.Alerts;
import controller.util.Constraints;
import controller.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Quantity11046Controller implements Initializable {
    @FXML
    private TextField textField_7;
    @FXML
    private TextField textField_8;
    @FXML
    private TextField textField_9;
    @FXML
    private TextField textField_10;
    @FXML
    private TextField textField_11;
    @FXML
    private Button btConfirm;
    @FXML
    private Button btCancel;


    @FXML
    public synchronized void onBtConfirmAction(ActionEvent event){


        var alert = new Alerts(Alert.AlertType.CONFIRMATION, "Os dados estão corretos?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            WareMapApplication.factory.setSpecific11046Quantity(
                    Integer.parseInt(textField_7.getText()),
                    Integer.parseInt(textField_8.getText()),
                    Integer.parseInt(textField_9.getText()),
                    Integer.parseInt(textField_10.getText()),
                    Integer.parseInt(textField_11.getText())
            );
            WareMapApplication.factory.setHas11046();
            Utils.currenteStage(event).close();
        }


    }
    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currenteStage(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Constraints.setTextFieldInteger(textField_7);
        Constraints.setTextFieldInteger(textField_8);
        Constraints.setTextFieldInteger(textField_9);
        Constraints.setTextFieldInteger(textField_10);
        Constraints.setTextFieldInteger(textField_11);

        textField_7.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) textField_8.requestFocus();
        });

        textField_8.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) textField_9.requestFocus();
        });

        textField_9.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) textField_10.requestFocus();
        });

        textField_10.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) textField_11.requestFocus();
        });

        textField_11.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) btConfirm.requestFocus();
        });

    }
}
