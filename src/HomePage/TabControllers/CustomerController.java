package HomePage.TabControllers;

import HomePage.Data.CustomerData;
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

public class CustomerController implements Initializable {

    //configure textfields for add customer section
    @FXML private TextField accountnumber;
    @FXML private TextField lastname;
    @FXML private TextField firstname;
    @FXML private TextField address;
    @FXML private TextField email;
    @FXML private TextField phonenumber;
    @FXML private DatePicker dob;

    @FXML private TextField delete;

    //configure the CustomerData table
    @FXML private TableView<CustomerData> customertable;
    @FXML private TableColumn<CustomerData, String> accountnumbercolumn;
    @FXML private TableColumn<CustomerData, String> lastnamecolumn;
    @FXML private TableColumn<CustomerData, String> firstnamecolumn;
    @FXML private TableColumn<CustomerData, String> addresscolumn;
    @FXML private TableColumn<CustomerData, String> emailcolumn;
    @FXML private TableColumn<CustomerData, String> phonenumbercolumn;
    @FXML private TableColumn<CustomerData, String> dobcolumn;

    private dbConnection dc;
    private ObservableList<CustomerData> data;

    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();
        initializeColumns();

    }

    //allows columns to be editable
    private void initializeColumns(){
        customertable.setEditable(true);
        accountnumbercolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnamecolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnamecolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addresscolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phonenumbercolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dobcolumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    /**
     * Loads customer information from clearskies.sqlite database onto customer Tableview
     * Method uses a search query to pull every customer's info from each column and display it row by row for the end user
     * @param event Triggered by Load Data button press
     * @throws SQLException
     */
    @FXML
    private void loadCustomerData(ActionEvent event)throws SQLException{
        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Customer");
            while (rs.next()){
                this.data.add(new CustomerData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        }
        catch(SQLException e){
            System.err.println("Error " + e);
        }

        //set up columns in the table
        this.accountnumbercolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("accountnumber"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("lastname"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("firstname"));
        this.addresscolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("address"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("email"));
        this.phonenumbercolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("phonenumber"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<CustomerData, String>("dob"));

        //load table data
        this.customertable.setItems(null);
        this.customertable.setItems(this.data);
    }

    /**
     * Create new customer and insert it into clearskies.sqlite
     * When add entry button is pressed, any text inside the textboxes will update their respective columns in the DB
     * @param event triggered by Add Entry button press
     */
    @FXML
    private void addCustomer(ActionEvent event){
        String sqlInsert = "INSERT INTO Customer(AccountNumber,LastName, FirstName, Address, Email, PhoneNumber, DOB) VALUES (?,?,?,?,?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);

            stmt.setString(1,this.accountnumber.getText());
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
     * This method will delete a Customer from the records after user types in a Customer's id and clicks the delete button
     * @param event triggered by Delete button press
     */
    @FXML
    private void deleteCustomer(ActionEvent event){
        String sqlDelete = "DELETE FROM Customer WHERE AccountNumber = ?";

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

    /**
     * This method will clear all of the text from the textboxes in the add customer section
     * @param event triggered by Clear Form button press
     */
    @FXML
    private void clearFields(ActionEvent event){
        this.accountnumber.setText("");
        this.lastname.setText("");
        this.firstname.setText("");
        this.address.setText("");
        this.email.setText("");
        this.phonenumber.setText("");
        this.dob.setValue(null);
    }


    /****************************************************************************************************
     * Below are methods that are called when a user wants to edit current entries in the customers table
     * When a user clicks on a cell, the customer belonging to the row of that cell is retrieved
     * That customer's attribute is updated with entered value based on what column the selected cell is in
     * @param editedCell cell that user clicks on in the table
     ****************************************************************************************************/

    @FXML
    private void changeAccountNumberCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setAccountnumber(editedCell.getNewValue().toString());
        String sqlChangeAccountNum = "UPDATE Customers SET AccountNumber= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangeAccountNum);
    }

    @FXML
    private void changeLastNameCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setLastname(editedCell.getNewValue().toString());
        String sqlChangeLastName = "UPDATE Customers SET LastName= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangeLastName);
    }

    @FXML
    private void changeFirstNameCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setFirstname(editedCell.getNewValue().toString());
        String sqlChangeFirstName = "UPDATE Customers SET FirstName= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangeFirstName);
    }

    @FXML
    private void changeAddressCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setAddress(editedCell.getNewValue().toString());
        String sqlChangeAddress = "UPDATE Customers SET Address= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangeAddress);
    }

    @FXML
    private void changeEmailCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setEmail(editedCell.getNewValue().toString());
        String sqlChangeEmail = "UPDATE Customers SET Email= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangeEmail);
    }

    @FXML
    private void changePhoneNumberCellEvent(TableColumn.CellEditEvent<CustomerData, String> editedCell){
        CustomerData customerSelected = customertable.getSelectionModel().getSelectedItem();
        customerSelected.setPhonenumber(editedCell.getNewValue().toString());
        String sqlChangePhoneNumber = "UPDATE Customers SET PhoneNumber= ? WHERE ROWID = ?";

        updateCustomerInfo(editedCell, sqlChangePhoneNumber);
    }


    /**
     * Method gets called when user edits a cell
     * Any changes made to the cell automatically updates the appropriate row and column in clearskies.sqlite
     * @param editedCell cell that user clicks on in the table
     * @param sqlChange sql statement to update a customer's information in DB based on what column is being changed
     */
    private void updateCustomerInfo(TableColumn.CellEditEvent<CustomerData, String> editedCell, String sqlChange) {
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
