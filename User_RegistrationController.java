package library_management_system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class User_RegistrationController implements Initializable {

    @FXML
    private TextField fullname;
    @FXML
    private TextField Cuserid;
    @FXML
    private TextField Cpassid;
    @FXML
    private TextField mobileno;
    @FXML
    private ToggleGroup select;
    @FXML
    private Button btn3;

    private final String DB_URL = "jdbc:mysql://localhost:3306/li_database";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    @FXML
    private Button home;
    @FXML
    private Label label11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void submit3(ActionEvent event) {
        String fullName = fullname.getText();
        String userId = Cuserid.getText();
        String password = Cpassid.getText();
        String mobileNo = mobileno.getText();
        String userType = ((RadioButton) select.getSelectedToggle()).getText();

        if (fullName.isEmpty() || userId.isEmpty() || password.isEmpty() || mobileNo.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Error!", "Please fill in all fields.");
            return;
        }

        if (mobileNo.length() != 11) {
            showAlert(Alert.AlertType.WARNING, "Invalid Mobile Number!", "Please enter a valid 11-digit mobile number.");
            return;
        }

        if (insertUserData(fullName, userId, password, mobileNo, userType)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
            fullname.clear();
            Cuserid.clear();
            Cpassid.clear();
            mobileno.clear();
            navigateToLoginPage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Could not register the user. Please try again.");
        }
    }
// eikhane datagulaa add hocche database a
    private boolean insertUserData(String fullName, String userId, String password, String mobileNo, String userType) {
        String insertQuery = "INSERT INTO users (fullname, user_id, password, mobile_no, user_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, mobileNo);
            preparedStatement.setString(5, userType);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    private void navigateToLoginPage() {   //data successfully add hole user login page a niye jabee
        try {
            URL loginPageUrl = getClass().getResource("User_Login.fxml");
            FXMLLoader loader = new FXMLLoader(loginPageUrl);
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) btn3.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading login page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
private void homebtn(ActionEvent event) {
    try {
     
        FXMLLoader loader = new FXMLLoader(getClass().getResource("User_Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
