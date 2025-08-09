package library_management_system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Desktop;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UserDashboardController implements Initializable {

    private Label userNameLabel;
    @FXML
    private ListView<String> detailsview;

    private final String DB_URL = "jdbc:mysql://localhost:3306/li_database"; // database aikhane connect hoyeche
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private ObservableList<String> pdfFiles = FXCollections.observableArrayList();
    private Map<String, String> pdfFilePaths = new HashMap<>();
    private String userId;
    @FXML
    private Button search;
    @FXML
    private Button logout2;
    @FXML
    private TextField searchhere;
    @FXML
    private Label label11;

    public void setUserId(String userId) {
        this.userId = userId;
        updateUserDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (userId != null) {
            updateUserDetails();
        }
        loadBookPDFs();
    }

    private void updateUserDetails() {
        if (userNameLabel != null && userId != null) {
            userNameLabel.setText("Welcome, " + userId + "!");
        }
    }

    private void loadBookPDFs() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT book_details FROM books");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            pdfFiles.clear();
            pdfFilePaths.clear();

            int counter = 1;
            while (resultSet.next()) {
                String filePath = resultSet.getString("book_details");
                filePath = filePath.replace("\\", "/");

                File file = new File(filePath);
                if (file.exists()) {
                    pdfFiles.add(counter + ". " + file.getName());
                    pdfFilePaths.put(counter + ". " + file.getName(), filePath);
                    counter++;
                } else {
                    System.out.println("File does not exist: " + filePath);
                }
            }

            detailsview.setItems(pdfFiles);
        } catch (Exception e) {
            System.out.println("Error loading book PDFs: " + e.getMessage());
        }
    }

    @FXML
    private void handlePDFClick(MouseEvent event) {
        String selectedFile = detailsview.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            downloadPDF(selectedFile);
        }
    }

    private void downloadPDF(String fileName) {
        String filePath = pdfFilePaths.get(fileName);
        if (filePath == null) {
            showAlert(AlertType.ERROR, "Error", "File path not found for " + fileName);
            return;
        }

        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            showAlert(AlertType.ERROR, "Error", "File does not exist!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(fileName);
        File destinationFile = fileChooser.showSaveDialog(new Stage());

        if (destinationFile != null) {
            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showAlert(AlertType.INFORMATION, "Success", "Downloaded: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Error", "Error saving PDF: " + e.getMessage());
            }
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void searchbtn(ActionEvent event) {
        String searchQuery = searchhere.getText().toLowerCase();
        ObservableList<String> filteredList = FXCollections.observableArrayList();

        for (String fileName : pdfFiles) {
            if (fileName.toLowerCase().contains(searchQuery)) {
                filteredList.add(fileName);
            }
        }

        detailsview.setItems(filteredList);
    }

    @FXML
    private void logoutbtn2(ActionEvent event) {
        Stage stage = (Stage) logout2.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("User_Login.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
