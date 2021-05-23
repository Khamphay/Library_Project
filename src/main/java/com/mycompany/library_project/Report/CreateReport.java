
package com.mycompany.library_project.Report;

import java.sql.Connection;
import java.util.Map;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class CreateReport {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    public JasperDesign design;
    public JasperReport report;
    public JasperViewer viewer;
    public JasperPrint print;
    public Connection con = MyConnection.getConnect();
    private String url_report = "/bin/";

    public CreateReport() {

    }

    public void showReport(Map<String, Object> map, String reportName, String nameError) {
        try {
            design = JRXmlLoader.load(url_report + reportName);
            report = JasperCompileManager.compileReport(design);
            print = JasperFillManager.fillReport(report, map, con);
            viewer = new JasperViewer(print, false);
            viewer.setVisible(true);
        } catch (JRException e) {
            alertMessage.showErrorMessage("Report Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile(nameError, e);
        }
    }
}