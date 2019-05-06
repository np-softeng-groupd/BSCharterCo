/**
 * Class to start up homepage
 * All other interactions between views and the data that they display are handled in their own classes
 * @author Igor Kalocay
 */

package HomePage;

import dbUtil.dbConnection;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    private dbConnection dc;


    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();

    }






}
