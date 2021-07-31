
package com.mycompany.library_project.Report;

import java.util.Map;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class CreateReport {

    private DialogMessage dialog = new DialogMessage();
    public JasperDesign design;
    public JasperReport report;
    public JasperViewer viewer;
    public JasperPrint print;
    // public Connection MyConnection.Cont(). = MyConnection.getConnect();
    private String url_report = "bin/";

    public CreateReport() {

    }

    public void showReport(Map<String, Object> map, String reportName, String errorName) {
        try {
            design = JRXmlLoader.load(url_report + reportName);
            report = JasperCompileManager.compileReport(design);
            print = JasperFillManager.fillReport(report, map, HomeController.con);
            viewer = new JasperViewer(print, false);
            viewer.setVisible(true);
        } catch (JRException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລາຍງານ", e);
        }
    }
}