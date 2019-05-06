/**
 * Class to handle interactions between PilotData and the views in pilotsTab.fxml
 * @author Igor Kalocay
 */
package HomePage.TabControllers;

import HomePage.Data.PilotData;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PilotController implements Initializable {

    //configure textfields for add pilot section
    @FXML private TextField id;
    @FXML private TextField lastname;
    @FXML private TextField firstname;
    @FXML private TextField address;
    @FXML private TextField email;
    @FXML private TextField phonenumber;
    @FXML private DatePicker dob;

    @FXML private TextField delete;

    //configure the PilotData table
    @FXML private TableView<PilotData> pilottable;
    @FXML private TableColumn<PilotData, String> idcolumn;
    @FXML private TableColumn<PilotData, String> lastnamecolumn;
    @FXML private TableColumn<PilotData, String> firstnamecolumn;
    @FXML private TableColumn<PilotData, String> addresscolumn;
    @FXML private TableColumn<PilotData, String> emailcolumn;
    @FXML private TableColumn<PilotData, String> phonenumbercolumn;
    @FXML private TableColumn<PilotData, String> dobcolumn;

    private dbConnection dc;
    private ObservableList<PilotData> data;

    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();
        initializeColumns();

    }

    //allows columns to be editable
    private void initializeColumns(){
        pilottable.setEditable(true);
        idcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnamecolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnamecolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addresscolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phonenumbercolumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    /**
     * Loads Pilot information from clearskies.sqlite database onto Pilot Tableview
     * Method uses a search query to pull every Pilot's info from each column and display it row by row for the end user
     * @param event Triggered by Load Data button press
     * @throws SQLException
     */
    @FXML
    private void loadPilotData(ActionEvent event)throws SQLException{
        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Pilots");
            while (rs.next()){
                this.data.add(new PilotData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        }
        catch(SQLException e){
            System.err.println("Error " + e);
        }

        //set up columns in the table
        this.idcolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("id"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("lastname"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("firstname"));
        this.addresscolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("address"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("email"));
        this.phonenumbercolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("phonenumber"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<PilotData, String>("dob"));

        //load table data
        this.pilottable.setItems(null);
        this.pilottable.setItems(this.data);
    }

    /**
     * Create new Pilot and insert it into clearskies.sqlite
     * When add entry button is pressed, any text inside the textboxes will update their respective columns in the DB
     * @param event triggered by Add Entry button press
     */
    @FXML
    private void addPilot(ActionEvent event){
        String sqlInsert = "INSERT INTO Pilots(ID,lastname, firstname, address, email, PhoneNumber, dob) VALUES (?,?,?,?,?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);

            stmt.setString(1,this.id.getText());
            stmt.setString(2,this.lastname.getText());
            stmt.setString(3,this.firstname.getText());
            stmt.setString(4,this.address.getText());
            stmt.setString(5,this.email.getText());
            stmt.setString(6,this.phonenumber.getText());
            stmt.setString(7,this.dob.getEditor().getText());

            stmt.execute();
            conn.close();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method will clear all of the text from the textboxes in the add pilot section
     * @param event triggered by Clear Form button press
     */
    @FXML
    private void clearFields(ActionEvent event){
        this.id.setText("");
        this.lastname.setText("");
        this.firstname.setText("");
        this.address.setText("");
        this.email.setText("");
        this.phonenumber.setText("");
        this.dob.setValue(null);
    }

    /**
     * This method will delete a Pilot from the records after user types in a Pilot's id and clicks the delete button
     * @param event triggered by Delete button press
     */
    @FXML
    private void deletePilot(ActionEvent event){
        String sqlDelete = "DELETE FROM Pilots WHERE ID = ?";

        delete(sqlDelete, this.delete);
    }

    @FXML
    private void delete(String sqlDelete, TextField delete) {
        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDelete);

            stmt.setString(1, delete.getText());

            stmt.execute();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /****************************************************************************************************
     * Below are methods that are called when a user wants to edit current entries in the Pilots table
     * When a user clicks on a cell, the Pilot belonging to the row of that cell is retrieved
     * That Pilot's attribute is updated with entered value based on what column the selected cell is in
     * @param editedCell cell that user clicks on in the table
     ****************************************************************************************************/

    @FXML
    private void changeIDCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setId(editedCell.getNewValue().toString());
        String sqlChangeID = "UPDATE Pilots SET ID= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangeID);
    }

    @FXML
    private void changeLastNameCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setLastname(editedCell.getNewValue().toString());
        String sqlChangeLastName = "UPDATE Pilots SET LastName= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangeLastName);
    }

    @FXML
    private void changeFirstNameCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setFirstname(editedCell.getNewValue().toString());
        String sqlChangeFirstName = "UPDATE Pilots SET FirstName= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangeFirstName);
    }

    @FXML
    private void changeAddressCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setAddress(editedCell.getNewValue().toString());
        String sqlChangeAddress = "UPDATE Pilots SET Address= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangeAddress);
    }

    @FXML
    private void changeEmailCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setEmail(editedCell.getNewValue().toString());
        String sqlChangeEmail = "UPDATE Pilots SET Email= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangeEmail);
    }

    @FXML
    private void changePhoneNumberCellEvent(TableColumn.CellEditEvent<PilotData, String> editedCell){
        PilotData pilotSelected = pilottable.getSelectionModel().getSelectedItem();
        pilotSelected.setPhonenumber(editedCell.getNewValue().toString());
        String sqlChangePhoneNumber = "UPDATE Pilots SET PhoneNumber= ? WHERE ROWID = ?";

        updatePilotInfo(editedCell, sqlChangePhoneNumber);
    }


    /**
     * Method gets called when user edits a cell
     * Any changes made to the cell automatically updates the appropriate row and column in clearskies.sqlite
     * @param editedCell cell that user clicks on in the table
     * @param sqlChange sql statement to update a Pilot's information in DB based on what column is being changed
     */
    private void updatePilotInfo(TableColumn.CellEditEvent<PilotData, String> editedCell, String sqlChange) {
        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlChange);

            //set string to new value entered in cell by user
            stmt.setString(1, editedCell.getNewValue());
            //set string to only update the current row being edited
            stmt.setString(2, String.valueOf(editedCell.getTablePosition().getRow() + 1));
            stmt.executeUpdate();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
