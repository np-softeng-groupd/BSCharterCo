package HomePage.TabControllers;

import HomePage.Data.ReservationData;
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

public class ReservationController implements Initializable {

    //configure textfields for add customer section
    @FXML private TextField aircraftid;
    @FXML private TextField customeraccountnumber;
    @FXML private TextField customername;
    @FXML private DatePicker reservestartdate;
    @FXML private DatePicker reserveenddate;

    @FXML private TextField delete;

    //configure the ReservationData table
    @FXML private TableView<ReservationData> reservationtable;
    @FXML private TableColumn<ReservationData, String> aircraftidcolumn;
    @FXML private TableColumn<ReservationData, String> customeraccountnumcolumn;
    @FXML private TableColumn<ReservationData, String> customernamecolumn;
    @FXML private TableColumn<ReservationData, String> reservestartcolumn;
    @FXML private TableColumn<ReservationData, String> reserveendcolumn;

    private dbConnection dc;
    private ObservableList<ReservationData> data;

    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();
        initializeColumns();

    }

    //allows columns to be editable
    private void initializeColumns(){
        reservationtable.setEditable(true);
        aircraftidcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        customeraccountnumcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        customernamecolumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    /**
     * Loads customer information from clearskies.sqlite database onto customer Tableview
     * Method uses a search query to pull every customer's info from each column and display it row by row for the end user
     * @param event Triggered by Load Data button press
     * @throws SQLException
     */
    @FXML
    private void loadReservationData(ActionEvent event)throws SQLException{
        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Reservation");
            while (rs.next()){
                this.data.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        }
        catch(SQLException e){
            System.err.println("Error " + e);
        }

        //set up columns in the table
        this.aircraftidcolumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("aircraftID"));
        this.customeraccountnumcolumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("customerAccountNum"));
        this.customernamecolumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("customerName"));
        this.reservestartcolumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("dateReservedFrom"));
        this.reserveendcolumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("dateReservedTo"));

        //load table data
        this.reservationtable.setItems(null);
        this.reservationtable.setItems(this.data);
    }

    /**
     * Create new customer and insert it into clearskies.sqlite
     * When add entry button is pressed, any text inside the textboxes will update their respective columns in the DB
     * @param event triggered by Add Entry button press
     */
    @FXML
    private void createReservation(ActionEvent event){
        String sqlInsert = "INSERT INTO Reservation(AircraftID,CustomerAccountNum, CustomerName, ReserveStartDate, ReserveEndDate) VALUES (?,?,?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);

            stmt.setString(1,this.aircraftid.getText());
            stmt.setString(2,this.customeraccountnumber.getText());
            stmt.setString(3,this.customername.getText());
            stmt.setString(4,this.reservestartdate.getEditor().getText());
            stmt.setString(5,this.reserveenddate.getEditor().getText());

            stmt.execute();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method will clear all of the text from the textboxes in the add customer section
     * @param event triggered by Clear Form button press
     */
    @FXML
    private void clearFields(ActionEvent event){
        this.aircraftid.setText("");
        this.customeraccountnumber.setText("");
        this.customername.setText("");
        this.reservestartdate.setValue(null);
        this.reserveenddate.setValue(null);
    }

    /**
     * This method will delete a Reservation from the records after user types in the Reservation's aircraft ID and clicks the delete button
     * @param event triggered by Delete button press
     */
    @FXML
    private void deleteReservation(ActionEvent event){
        String sqlDelete = "DELETE FROM Reservation WHERE AircraftID = ?";

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
     * Below are methods that are called when a user wants to edit current entries in the Reservation table
     * When a user clicks on a cell, the customer belonging to the row of that cell is retrieved
     * That customer's attribute is updated with entered value based on what column the selected cell is in
     * @param editedCell cell that user clicks on in the table
     ****************************************************************************************************/

    @FXML
    private void changeAircraftIDCellEvent(TableColumn.CellEditEvent<ReservationData, String> editedCell){
        ReservationData reservationSelected = reservationtable.getSelectionModel().getSelectedItem();
        reservationSelected.setAircraftID(editedCell.getNewValue().toString());
        String sqlChangeAircraftID= "UPDATE Reservation SET AircraftID= ? WHERE ROWID = ?";

        updateReservationInfo(editedCell, sqlChangeAircraftID);
    }

    @FXML
    private void changeCustomerAccountNumCellEvent(TableColumn.CellEditEvent<ReservationData, String> editedCell){
        ReservationData reservationSelected = reservationtable.getSelectionModel().getSelectedItem();
        reservationSelected.setCustomerAccountNum(editedCell.getNewValue().toString());
        String sqlChangeCustomerAccountNum = "UPDATE Reservation SET CustomerAccountNum= ? WHERE ROWID = ?";

        updateReservationInfo(editedCell, sqlChangeCustomerAccountNum);
    }

    @FXML
    private void changeCustomerNameCellEvent(TableColumn.CellEditEvent<ReservationData, String> editedCell){
        ReservationData reservationSelected = reservationtable.getSelectionModel().getSelectedItem();
        reservationSelected.setCustomerName(editedCell.getNewValue().toString());
        String sqlChangeCustomerName = "UPDATE Reservation SET CustomerName= ? WHERE ROWID = ?";

        updateReservationInfo(editedCell, sqlChangeCustomerName);
    }
    
    
    /**
     * Method gets called when user edits a cell
     * Any changes made to the cell automatically updates the appropriate row and column in clearskies.sqlite
     * @param editedCell cell that user clicks on in the table
     * @param sqlChange sql statement to update a customer's information in DB based on what column is being changed
     */
    private void updateReservationInfo(TableColumn.CellEditEvent<ReservationData, String> editedCell, String sqlChange) {
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
