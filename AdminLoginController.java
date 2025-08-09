package library_management_system;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController implements Initializable {

    @FXML
    private Label label1;
    @FXML
    private TextField adminid;
    @FXML
    private TextField adminpass;
    @FXML
    private Button btn2;
    @FXML
    private Label label2;
    @FXML
    private Hyperlink link2;
    @FXML
    private Label label11;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void submit1(ActionEvent event) {
      
        String adminIdInput = adminid.getText();
        String adminPassInput = adminpass.getText();

      
        if (adminIdInput.isEmpty() || adminPassInput.isEmpty()) {
            label2.setText("Admin ID and Password cannot be empty.");
            return;
        }

       
        if (adminIdInput.equals("admin") && adminPassInput.equals("admin123")) {
            label2.setText("Login successful!");

            // Redirect to Admin Dashboard on successful login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Admin Dashboard.fxml"));
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Dashboard");
                stage.show();
            } catch (IOException e) {
                System.out.println("Error loading AdminDashboard.fxml: " + e.getMessage());
            }

            // Clear the fields after login
            adminid.clear();
            adminpass.clear();
        } else {
            label2.setText("Invalid Admin ID or Password.");
        }
    }

    @FXML
    private void link2(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("User_Login.fxml"));
            Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login Form");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading User_Login.fxml: " + e.getMessage());
        }
    }
}
