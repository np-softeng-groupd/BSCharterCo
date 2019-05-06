/**
 * Main class that loads the Homepage
 * Current version skips login as it does not require it
 * Future versions of this program may require user to Login with username and password if needed
 * @author Igor Kalocay
 */

package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {

    public void start(Stage stage) throws Exception{
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/HomePage/homePage.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clear Skies Charter Co. ");
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
