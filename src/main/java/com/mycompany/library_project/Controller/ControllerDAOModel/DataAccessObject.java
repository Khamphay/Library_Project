package com.mycompany.library_project.Controller.ControllerDAOModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public interface DataAccessObject {
    public ResultSet findAll() throws SQLException;

    public ResultSet findById(String id) throws SQLException;

    public ResultSet findByName(String name) throws SQLException;

    public ResultSet searchData(String values) throws SQLException;

    public int saveData() throws SQLException, ParseException;

    public int updateData() throws SQLException, ParseException;

    public int deleteData() throws SQLException;
}
