package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;

public class CostModel implements DataAccessObject {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    // private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private PreparedStatement ps = null;

    private double cost_register, cost_perday, cost_perbook;

    public CostModel() {
    }

    public CostModel(double cost_register, double cost_perday, double cost_perbook) {
        this.cost_register = cost_register;
        this.cost_perday = cost_perday;
        this.cost_perbook = cost_perbook;
    }

    public double getCost_register() {
        return cost_register;
    }

    public void setCost_register(double cost_register) {
        this.cost_register = cost_register;
    }

    public double getCost_perday() {
        return cost_perday;
    }

    public void setCost_perday(double cost_perday) {
        this.cost_perday = cost_perday;
    }

    public double getCost_perbook() {
        return cost_perbook;
    }

    public void setCost_perbook(double cost_perbook) {
        this.cost_perbook = cost_perbook;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            String sql = "Select * From tbcost;";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Cost Error", e);
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            String sql = "insert into tbcost Values(?,?,?,?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setDouble(2, getCost_register());
            ps.setDouble(3, getCost_perday());
            ps.setDouble(4, getCost_perbook());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Insert Cost Error", e);
            return 0;
        } finally {
            // ps.close();
        }

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            String sql = "Update tbcost set cost_register=?, cost_outofdate=?, cost_lost=? Where id=1;";
            ps = HomeController.con.prepareStatement(sql);
            ps.setDouble(1, getCost_register());
            ps.setDouble(2, getCost_perday());
            ps.setDouble(3, getCost_perbook());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update Cost Error", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }
}
