package HomePage.Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AircraftData {

    private final StringProperty id;
    private final StringProperty type;
    private final StringProperty name;
    private final StringProperty requirescopilot;
    private final StringProperty numberofseats;
    private final StringProperty dailyrate;

    public AircraftData(String id, String type, String name, String requirescopilot, String numberofseats, String dailyrate){

        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.requirescopilot = new SimpleStringProperty(requirescopilot);
        this.numberofseats = new SimpleStringProperty(numberofseats);
        this.dailyrate = new SimpleStringProperty(dailyrate);

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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRequirescopilot() {
        return requirescopilot.get();
    }

    public StringProperty requirescopilotProperty() {
        return requirescopilot;
    }

    public void setRequirescopilot(String requirescopilot) {
        this.requirescopilot.set(requirescopilot);
    }

    public String getNumberofseats() {
        return numberofseats.get();
    }

    public StringProperty numberofseatsProperty() {
        return numberofseats;
    }

    public void setNumberofseats(String numberofseats) {
        this.numberofseats.set(numberofseats);
    }

    public String getDailyrate() {
        return dailyrate.get();
    }

    public StringProperty dailyrateProperty() {
        return dailyrate;
    }

    public void setDailyrate(String dailyrate) {
        this.dailyrate.set(dailyrate);
    }
}
