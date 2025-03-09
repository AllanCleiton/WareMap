package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class OpenFileController {
    private final String destinationFolder = "src/main/resources/temp"; // Pasta onde o arquivo será salvo


    @FXML private TextField lbPathFild;
    @FXML private Button btnSelectFile;



    @FXML
    public void onBtFileSelection(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo");
        File selectedFile = fileChooser.showOpenDialog(btnSelectFile.getScene().getWindow());

        if (selectedFile != null) {
            processFile(selectedFile);
        }
    }

    @FXML
    public void initialize() {
        // Permitir que o usuário solte um arquivo na caixa de texto
        lbPathFild.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Quando o arquivo é solto, pegamos o caminho e copiamos para a pasta de destino
        lbPathFild.setOnDragDropped(event -> handleFileDrop(event));
    }

    private void handleFileDrop(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            File droppedFile = dragboard.getFiles().get(0);
            processFile(droppedFile);
        }
        event.setDropCompleted(true);
        event.consume();
    }

    private void processFile(File file) {
        try {
            // Criar diretório de destino, se não existir
            File destFolder = new File(destinationFolder);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            // Definir caminho do novo arquivo
            File destFile = new File(destFolder, file.getName());

            // Copiar o arquivo para a pasta de destino
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Atualizar o campo de texto com o novo caminho do arquivo
            lbPathFild.setText(destFile.getAbsolutePath());

            // Exibir alerta de sucesso
            showAlert("Arquivo salvo com sucesso!", Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            showAlert("Erro ao salvar o arquivo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
