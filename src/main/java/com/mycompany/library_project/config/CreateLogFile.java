package com.mycompany.library_project.config;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.cliftonlabs.json_simple.*;
import com.mycompany.library_project.ControllerDAOModel.*;
public class CreateLogFile {
    private String path_log = "log/";
    private String path_config = "config/";
    private String fileConfig = "Server_infor.json";
    private DialogMessage dialog = new DialogMessage();

    public CreateLogFile() {
    }

    public boolean chackFileConfig() {
        File jsonFile = new File(Paths.get(path_config + fileConfig).toAbsolutePath().toString());
        return jsonFile.exists();
    }

    public boolean createFileConfig(String host, String port, String userName, String password) {
        try {
            // FileWriter fileWriter = new FileWriter();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path_config + fileConfig));
            JsonObject server = new JsonObject();
            server.put("host", host);
            server.put("port", port);
            server.put("userName", userName);
            server.put("password", password);
            Jsoner.serialize(server, writer);
            writer.close();
            return true;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການກວດສອບຂໍ້ມູນການເຊື່ອມຕໍ່", e);
            return false;
        }
    }

    public String[] getServerInfor() {
        try {
            String server[] = new String[4];
            // FileReader fileReader = new FileReader(path + fileConfig);
            BufferedReader reader = Files.newBufferedReader(Paths.get(path_config+fileConfig));
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            server[0] = (String) parser.get("host");
            server[1] = (String) parser.get("port");
            server[2] = (String) parser.get("userName");
            server[3] = (String) parser.get("password");

            reader.close();
            return server;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການອ່ານຂໍ້ມູນການເຊື່ອມຕໍ່", e);
            return null;
        }
    }

    public boolean createLogFile(String title, Exception ex) {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            File file = new File(path_log + "log_file.log");
            // Todo: PrintWriter
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.write("\n=======" + dateFormat.format(LocalDateTime.now()) + " (" + title
                    + ")===============================\nSummary: " + ex.getMessage() + "\n");
            ex.printStackTrace(pw);
            fw.close();
            pw.close();
            return true;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຂຽນຂໍ້ມູນ Logfile", e);
            return false;
        }
    }

}
