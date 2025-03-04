package controller;

import application.WareMapApplication;
import controller.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class InStateController implements Initializable {
    @FXML
    private TextField lbOrder;
    @FXML
    private Button btGenerate;
    @FXML
    private TableView<File> tableViewFiles;
    @FXML
    private TableColumn<File, String> tableColumnArchive;
    @FXML
    private TableColumn<File, File> tableColumnEDIT;
    @FXML
    private TableColumn<File, File> tableColumnREMOVE;
    @FXML
    private TableColumn<File, File> tableColumnPRINT;

    private ObservableList<File> observableList;

    private final List<File> files = new ArrayList<>();

    @FXML
    public void onBtGenerate() {
        //TODO IMPLEMENTATION HERE.
        System.out.println("Clicou");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    public void upDateTableView() {
        File path = new File("src/main/resources/temp/separations/inState");

        this.files.addAll(List.of(Objects.requireNonNull(path.listFiles(File::isFile))));

        observableList = FXCollections.observableArrayList(this.files);
        tableViewFiles.setItems(observableList);
        initAbrirButtons();
        initRemoveButtons();
        initPrintButtons();
    }

    private void initAbrirButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<File, File>() {
            private final Button button = new Button(" Abrir ");

            @Override
            protected void updateItem(File obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> {
                    File archive = new File(obj.getAbsolutePath()); // Substitua pelo caminho correto

                    if (archive.exists()) {
                        try {
                            Desktop.getDesktop().open(archive);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Alerts.showAlert("IO Exception", "Erro ao Tentar abrir o arquivo", e.getMessage(), Alert.AlertType.ERROR);
                        }
                    }
                });
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<File, File>() {
            private final Button button = new Button(" Apagar ");

            @Override
            protected void updateItem(File obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> {
                    File archive = new File(obj.getAbsolutePath());

                    if (archive.exists()) {

                        var alert = new Alerts(Alert.AlertType.CONFIRMATION, "Deseja excluir este arquivo?");
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            if (archive.delete()) {
                                files.clear();
                                upDateTableView();
                            }

                        }

                    }
                });
            }
        });
    }

    private void initPrintButtons() {
        tableColumnPRINT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnPRINT.setCellFactory(param -> new TableCell<File, File>() {
            private final Button button = new Button(" Imprimir ");

            @Override
            protected void updateItem(File obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> {
                    print(obj.getAbsolutePath());
                });
            }
        });
    }

    public void print(String filePath){
        Desktop desktop = Desktop.getDesktop();
        try {
            File file = new File(filePath);
            desktop.print(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeNodes(){
        tableColumnArchive.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        //tableColumnArchive.setCellValueFactory(new PropertyValueFactory<>("path"));
        Stage stage = (Stage) WareMapApplication.getMainScene().getWindow();
        tableViewFiles.prefHeightProperty().bind(stage.heightProperty());
    }
}
