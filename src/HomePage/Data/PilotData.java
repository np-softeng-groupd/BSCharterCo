package HomePage.Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PilotData {

    private final StringProperty id;
    private final StringProperty lastname;
    private final StringProperty firstname;
    private final StringProperty address;
    private final StringProperty email;
    private final StringProperty phonenumber;
    private final StringProperty dob;

    public PilotData(String id, String lastname, String firstname, String address, String email, String phonenumber, String dob){

        this.id = new SimpleStringProperty(id);
        this.lastname = new SimpleStringProperty(lastname);
        this.firstname = new SimpleStringProperty(firstname);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.phonenumber = new SimpleStringProperty(phonenumber);
        this.dob = new SimpleStringProperty(dob);

    }

    public String getId() {
        return id.get();
    }


    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getLastname() {
        return lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getDob() {
        return dob.get();
    }

    public StringProperty dobProperty() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getPhonenumber() {
        return phonenumber.get();
    }

    public StringProperty phonenumberProperty() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber.set(phonenumber);
    }
}
