package com.mycompany.library_project.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeModel {
    private StringProperty employeeId;
    private StringProperty firstName;
    private StringProperty sureName;
    private StringProperty gender;
    private StringProperty tel;

    public EmployeeModel(String employeeId, String firstName, String sureName, String gender, String tel) {
        this.employeeId = new SimpleStringProperty(employeeId);
        this.firstName = new SimpleStringProperty(firstName);
        this.sureName = new SimpleStringProperty(sureName);
        this.gender = new SimpleStringProperty(gender);
        this.tel = new SimpleStringProperty(tel);
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getSureName() {
        return sureName.get();
    }

    public StringProperty sureNameProperty() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName.set(sureName);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getTel() {
        return tel.get();
    }

    public StringProperty telProperty() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }
}
