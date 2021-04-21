package com.mycompany.library_project.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.github.cliftonlabs.json_simple.*;

public class ConfigDatabase {
    private String fileConfig = "Server_infor.json";
    private String path = "\\config";

    public ConfigDatabase() {
    }

    public boolean chackFileConfig() {
        File jsonFile = new File(path + fileConfig);
        return jsonFile.exists();
    }

    public boolean createFileConfig(String serverName, String userName, String password) {
        try {
            FileWriter fileWriter = new FileWriter(path + fileConfig);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            JsonObject server = new JsonObject();
            server.put("serverName", serverName);
            server.put("userName", userName);
            server.put("password", password);
            Jsoner.serialize(server, writer);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getServerInfor() {
        try {
            String server[] = new String[3];
            FileReader fileReader = new FileReader(path + fileConfig);
            BufferedReader reader = new BufferedReader(fileReader);
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            server[0] = (String) parser.get("serverName");
            server[1] = (String) parser.get("userName");
            server[2] = (String) parser.get("password");
            return server;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
