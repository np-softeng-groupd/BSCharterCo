/**
 * Class to handle interactions between AircraftData and the views in aircraftTab.fxml
 */
package HomePage.TabControllers;

import HomePage.Data.AircraftData;
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

public class AircraftController implements Initializable {

    //configure textfields for add aircraft section
    @FXML private TextField id;
    @FXML private TextField type;
    @FXML private TextField name;
    @FXML private TextField requirescopilot;
    @FXML private TextField numberofseats;

    @FXML private TextField delete;

    //configure the AircraftData table
    @FXML private TableView<AircraftData> aircrafttable;
    @FXML private TableColumn<AircraftData, String> aircraftIDColumn;
    @FXML private TableColumn<AircraftData, String> aircraftTypeColumn;
    @FXML private TableColumn<AircraftData, String> aircraftNameColumn;
    @FXML private TableColumn<AircraftData, String> aircraftCopilotcolumn;
    @FXML private TableColumn<AircraftData, String> aircraftSeatsColumn;

    private dbConnection dc;
    private ObservableList<AircraftData> data;

    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();
        initializeColumns();

    }

    //allows columns to be editable
    private void initializeColumns(){
        aircrafttable.setEditable(true);
        aircraftIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        aircraftTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        aircraftNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        aircraftCopilotcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        aircraftSeatsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * Loads Aircraft information from clearskies.sqlite database onto Aircraft Tableview
     * Method uses a search query to pull every Aircraft's info from each column and display it row by row for the end user
     * @param event Triggered by Load Data button press
     * @throws SQLException
     */
    @FXML
    private void loadAircraftData(ActionEvent event)throws SQLException{
        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Aircraft");
            while (rs.next()){
                this.data.add(new AircraftData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        }
        catch(SQLException e){
            System.err.println("Error " + e);
        }

        //set up columns in the table
        this.aircraftIDColumn.setCellValueFactory(new PropertyValueFactory<AircraftData, String>("id"));
        this.aircraftTypeColumn.setCellValueFactory(new PropertyValueFactory<AircraftData, String>("type"));
        this.aircraftNameColumn.setCellValueFactory(new PropertyValueFactory<AircraftData, String>("name"));
        this.aircraftCopilotcolumn.setCellValueFactory(new PropertyValueFactory<AircraftData, String>("requirescopilot"));
        this.aircraftSeatsColumn.setCellValueFactory(new PropertyValueFactory<AircraftData, String>("numberofseats"));

        //load table data
        this.aircrafttable.setItems(null);
        this.aircrafttable.setItems(this.data);
    }

    /**
     * Create new Aircraft and insert it into clearskies.sqlite
     * When add entry button is pressed, any text inside the textboxes will update their respective columns in the DB
     * @param event triggered by Add Entry button press
     */
    @FXML
    private void addAircraft(ActionEvent event){
        String sqlInsert = "INSERT INTO Aircraft(ID, Type, Name, RequiresCopilot, NumberOfSeats) VALUES (?,?,?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);

            stmt.setString(1,this.id.getText());
            stmt.setString(2,this.type.getText());
            stmt.setString(3,this.name.getText());
            stmt.setString(4,this.requirescopilot.getText());
            stmt.setString(5,this.numberofseats.getText());

            stmt.execute();
            conn.close();
            
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method will clear all of the text from the textboxes in the add aircraft section
     * @param event triggered by Clear Form button press
     */
    @FXML
    private void clearFields(ActionEvent event){
        this.id.setText("");
        this.type.setText("");
        this.name.setText("");
        this.requirescopilot.setText("");
        this.numberofseats.setText("");
    }

    /**
     * This method will delete an aircraft from the records after user types in an aircraft id and clicks the delete button
     * @param event triggered by Delete button press
     */
    @FXML
    private void deleteAircraft(ActionEvent event){
        String sqlDelete = "DELETE FROM Aircraft WHERE ID = ?";

        delete(sqlDelete, this.delete);
    }

    @FXML
    private void delete(String sqlDelete, TextField delete) {
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDelete);

            stmt.setString(1, delete.getText());

            stmt.execute();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /****************************************************************************************************
     * Below are methods that are called when a user wants to edit current entries in the Aircraft table
     * When a user clicks on a cell, the Aircraft belonging to the row of that cell is retrieved
     * That Aircraft's attribute is updated with entered value based on what column the selected cell is in
     * @param editedCell cell that user clicks on in the table
     ****************************************************************************************************/

    @FXML
    private void changeIDCellEvent(TableColumn.CellEditEvent<AircraftData, String> editedCell){
        AircraftData aircraftSelected = aircrafttable.getSelectionModel().getSelectedItem();
        aircraftSelected.setId(editedCell.getNewValue().toString());
        String sqlChangeID = "UPDATE Aircraft SET ID= ? WHERE ROWID = ?";

        updateAircraftInfo(editedCell, sqlChangeID);
    }

    @FXML
    private void changeTypeCellEvent(TableColumn.CellEditEvent<AircraftData, String> editedCell){
        AircraftData aircraftSelected = aircrafttable.getSelectionModel().getSelectedItem();
        aircraftSelected.setType(editedCell.getNewValue().toString());
        String sqlChangeType = "UPDATE Aircraft SET Type= ? WHERE ROWID = ?";

        updateAircraftInfo(editedCell, sqlChangeType);
    }

    @FXML
    private void changeNameCellEvent(TableColumn.CellEditEvent<AircraftData, String> editedCell){
        AircraftData aircraftSelected = aircrafttable.getSelectionModel().getSelectedItem();
        aircraftSelected.setName(editedCell.getNewValue().toString());
        String sqlChangeName = "UPDATE Aircraft SET Name= ? WHERE ROWID = ?";

        updateAircraftInfo(editedCell, sqlChangeName);
    }

    @FXML
    private void changeRequiresCopilotCellEvent(TableColumn.CellEditEvent<AircraftData, String> editedCell){
        AircraftData aircraftSelected = aircrafttable.getSelectionModel().getSelectedItem();
        aircraftSelected.setRequirescopilot(editedCell.getNewValue().toString());
        String sqlChangeRequiresCopilot = "UPDATE Aircraft SET RequiresCopilot= ? WHERE ROWID = ?";

        updateAircraftInfo(editedCell, sqlChangeRequiresCopilot);
    }

    @FXML
    private void changeNumberOfSeatsCellEvent(TableColumn.CellEditEvent<AircraftData, String> editedCell){
        AircraftData aircraftSelected = aircrafttable.getSelectionModel().getSelectedItem();
        aircraftSelected.setNumberofseats(editedCell.getNewValue().toString());
        String sqlChangeNumberOfSeats = "UPDATE Aircraft SET NumberOfSeats= ? WHERE ROWID = ?";

        updateAircraftInfo(editedCell, sqlChangeNumberOfSeats);
    }



    /**
     * Method gets called when user edits a cell
     * Any changes made to the cell automatically updates the appropriate row and column in clearskies.sqlite
     * @param editedCell cell that user clicks on in the table
     * @param sqlChange sql statement to update Aircraft's information in DB based on what column is being changed
     */
    private void updateAircraftInfo(TableColumn.CellEditEvent<AircraftData, String> editedCell, String sqlChange) {
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
