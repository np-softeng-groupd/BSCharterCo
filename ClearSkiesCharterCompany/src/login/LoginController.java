/**
 * IGNORE THIS CLASS
 */
package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import HomePage.HomePageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    LoginModel loginModel = new LoginModel();

    @FXML
    private Label dbstatus;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginStatus;

    public void initialize(URL url, ResourceBundle rb){
        if(this.loginModel.isDatabaseConnected()){
            this.dbstatus.setText("Connected to Database");
        }
        else{
            this.dbstatus.setText("Not Connected to Database");
        }
    }

    @FXML
    public void Login(ActionEvent event){
        try{

            if(this.loginModel.isLogin(this.username.getText(), this.password.getText())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                homePageLogin();
            }
            else{
                this.loginStatus.setText("Invalid Username and/or Password");
            }
        }
        catch(Exception localException){
            localException.printStackTrace();
        }

    }

     public void homePageLogin(){
        try{

            Stage userStage = new Stage();
            FXMLLoader homeLoader = new FXMLLoader();
            Pane root = (Pane) FXMLLoader.load(getClass().getResource("/HomePage/homePage.fxml"));
            HomePageController homePageController = (HomePageController)homeLoader.getController();

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("Clear Skies Charter Co.");
            userStage.setResizable(false);
            userStage.show();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
