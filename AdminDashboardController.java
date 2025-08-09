package library_management_system;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminDashboardController implements Initializable {

    static String[] booksList;

    @FXML
    private ListView<String> listview1;
    @FXML
    private Button btn4;
    @FXML
    private TextField detailsbook;
    @FXML
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button btn7;

    private final String DB_URL = "jdbc:mysql://localhost:3306/li_database";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    @FXML
    private Button logout1;
    @FXML
    private Label label11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBooksFromDatabase();
    }

    @FXML
    private void uploadpdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            detailsbook.setText(selectedFile.getAbsolutePath());
            showAlert(AlertType.INFORMATION, "File Selected", "You have selected: " + selectedFile.getName());
        } else {
            showAlert(AlertType.WARNING, "No File Selected", "Please select a PDF file.");
        }
    }

    @FXML
    private void submit5(ActionEvent event) {
        String bookDetails = detailsbook.getText();

        if (bookDetails.isEmpty()) {
            showAlert(AlertType.WARNING, "Book Details Missing", "Please enter the book details.");
        } else {
            addBookToDatabase(bookDetails);
            detailsbook.clear();
        }
    }

    @FXML
    private void submit6(ActionEvent event) {
        String selectedBook = listview1.getSelectionModel().getSelectedItem();
        String newBookDetails = detailsbook.getText();

        if (selectedBook == null) {
            showAlert(AlertType.WARNING, "No Book Selected", "Please select a book to update.");
        } else if (newBookDetails.isEmpty()) {
            showAlert(AlertType.WARNING, "New Book Details Missing", "Please enter the new book details.");
        } else {
            updateBookInDatabase(selectedBook, newBookDetails);
            detailsbook.clear();
        }
    }

    @FXML
    private void submit7(ActionEvent event) {
        String selectedBook = listview1.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(AlertType.WARNING, "No Book Selected", "Please select a book to delete.");
        } else {
            deleteBookFromDatabase(selectedBook);
        }
    }

    private void loadBooksFromDatabase() {
        listview1.getItems().clear();
        String query = "SELECT book_details FROM books";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String bookDetails = resultSet.getString("book_details");
                listview1.getItems().add(bookDetails);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to load books: " + e.getMessage());
        }
    }

    private void addBookToDatabase(String bookDetails) {
        String query = "INSERT INTO books (book_details) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bookDetails);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                listview1.getItems().add(bookDetails);
                showAlert(AlertType.INFORMATION, "Book Added", "The book has been added successfully.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to add book: " + e.getMessage());
        }
    }

    private void updateBookInDatabase(String oldBookDetails, String newBookDetails) {
        String query = "UPDATE books SET book_details = ? WHERE book_details = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newBookDetails);
            preparedStatement.setString(2, oldBookDetails);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                listview1.getItems().set(listview1.getSelectionModel().getSelectedIndex(), newBookDetails);
                showAlert(AlertType.INFORMATION, "Book Updated", "The book has been updated successfully.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to update book: " + e.getMessage());
        }
    }

    private void deleteBookFromDatabase(String bookDetails) {
        String query = "DELETE FROM books WHERE book_details = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bookDetails);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                listview1.getItems().remove(bookDetails);
                showAlert(AlertType.INFORMATION, "Book Deleted", "The book has been deleted successfully.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to delete book: " + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
private void logoutbtn1(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library_management_system/Admin Login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Admin Login");
        stage.show();
    } catch (IOException e) {
        System.out.println("Error loading Admin_Login.fxml: " + e.getMessage());
    }
}
}
