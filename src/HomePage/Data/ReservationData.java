package HomePage.Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationData {

    private final StringProperty aircraftID;
    private final StringProperty customerAccountNum;
    private final StringProperty customerName;
    private final StringProperty dateReservedFrom;
    private final StringProperty dateReservedTo;

    public ReservationData(String aircraftID, String customerAccountNum, String customerName, String dateReservedFrom, String dateReservedTo){

        this.aircraftID = new SimpleStringProperty(aircraftID);
        this.customerAccountNum = new SimpleStringProperty(customerAccountNum);
        this.customerName = new SimpleStringProperty(customerName);
        this.dateReservedFrom = new SimpleStringProperty(dateReservedFrom);
        this.dateReservedTo = new SimpleStringProperty(dateReservedTo);

    }

    public String getAircraftID() {
        return aircraftID.get();
    }

    public StringProperty aircraftIDProperty() {
        return aircraftID;
    }

    public void setAircraftID(String aircraftID) {
        this.aircraftID.set(aircraftID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getDateReservedFrom() {
        return dateReservedFrom.get();
    }

    public StringProperty dateReservedFromProperty() {
        return dateReservedFrom;
    }

    public void setDateReservedFrom(String dateReservedFrom) {
        this.dateReservedFrom.set(dateReservedFrom);
    }

    public String getDateReservedTo() {
        return dateReservedTo.get();
    }

    public StringProperty dateReservedToProperty() {
        return dateReservedTo;
    }

    public void setDateReservedTo(String dateReservedTo) {
        this.dateReservedTo.set(dateReservedTo);
    }

    public String getCustomerAccountNum() {
        return customerAccountNum.get();
    }

    public StringProperty customerAccountNumProperty() {
        return customerAccountNum;
    }

    public void setCustomerAccountNum(String customerAccountNum) {
        this.customerAccountNum.set(customerAccountNum);
    }
}
