package library_management_system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class User_LoginController implements Initializable {

    @FXML
    private TextField userid;
    @FXML
    private TextField passid;
    @FXML
    private Label alertlabel;
    @FXML
    private Button btn1;
    @FXML
    private Hyperlink link;
    @FXML
    private Hyperlink link3;

    // Database credentials
    private final String DB_URL = "jdbc:mysql://localhost:3306/li_database";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    @FXML
    private Label label11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void submit(ActionEvent event) {
        String userIdInput = userid.getText();
        String passIdInput = passid.getText();

        if (userIdInput.isEmpty() || passIdInput.isEmpty()) {
            setAlert("User ID and Password cannot be empty!", "red");
            return;
        }

        if (validateCredentials(userIdInput, passIdInput)) {
            setAlert("Login successful!", "green");
            userid.clear();
            passid.clear();
            loadUserDashboard(event, userIdInput);
        } else {
            setAlert("Invalid User ID or Password!", "red");
        }
    }
//database connect
    private boolean validateCredentials(String userId, String password) {
        String query = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return false;
    }

    private void loadUserDashboard(ActionEvent event, String userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library_management_system/User_Dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController dashboardController = loader.getController();
            dashboardController.setUserId(userId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading User_Dashboard.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void link1(ActionEvent event) {
        loadPage(event, "Admin Login.fxml", "Admin Login Form");
    }

    @FXML
    private void link3(ActionEvent event) {
        loadPage(event, "User_Registration.fxml", "User Registration Form");
    }

    private void loadPage(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }

    private void setAlert(String message, String color) {
        alertlabel.setText(message);
        alertlabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
