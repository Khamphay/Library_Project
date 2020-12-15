package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentModel {
    private StringProperty depId;
    private StringProperty depName;

    public DepartmentModel(String depId, String depName) {
        this.depId = new SimpleStringProperty(depId);
        this.depName = new SimpleStringProperty(depName);
    }

    public String getDepId() {
        return depId.get();
    }

    public StringProperty depIdProperty() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId.set(depId);
    }

    public String getDepName() {
        return depName.get();
    }

    public StringProperty depNameProperty() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName.set(depName);
    }
}
