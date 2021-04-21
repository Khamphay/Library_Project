package com.mycompany.library_project.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.cliftonlabs.json_simple.*;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;

import javafx.geometry.Pos;

public class ConfigDatabase {
    private String fileConfig = "Server_infor.json";
    private AlertMessage alertMessage = new AlertMessage();

    public ConfigDatabase() {
    }

    public boolean chackFileConfig() {
        File jsonFile = new File(Paths.get(fileConfig).toAbsolutePath().toString());
        return jsonFile.exists();
    }

    public boolean createFileConfig(String host, String port, String userName, String password) {
        try {
            // FileWriter fileWriter = new FileWriter();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileConfig));
            JsonObject server = new JsonObject();
            server.put("host", host);
            server.put("port", port);
            server.put("userName", userName);
            server.put("password", password);
            Jsoner.serialize(server, writer);
            writer.close();
            return true;
        } catch (Exception e) {
            alertMessage.showErrorMessage("Save Configed", "Error: " + e.getMessage(), 4, Pos.TOP_CENTER);
            e.printStackTrace();
            return false;
        }
    }

    public String[] getServerInfor() {
        try {
            String server[] = new String[4];
            // FileReader fileReader = new FileReader(path + fileConfig);
            BufferedReader reader = Files.newBufferedReader(Paths.get(fileConfig));
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            server[0] = (String) parser.get("host");
            server[1] = (String) parser.get("port");
            server[2] = (String) parser.get("userName");
            server[3] = (String) parser.get("password");

            reader.close();
            return server;
        } catch (Exception e) {
            alertMessage.showErrorMessage("Read Configed", "Error: " + e.getMessage(), 4, Pos.TOP_CENTER);
            e.printStackTrace();
            return null;
        }
    }
}
