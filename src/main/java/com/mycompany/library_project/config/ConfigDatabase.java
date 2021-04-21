package com.mycompany.library_project.config;

import java.io.*;

import com.github.cliftonlabs.json_simple.*;

public class ConfigDatabase {
    private String fileConfig = "/Server_infor.json";
    private String path = new File("").getAbsolutePath().concat("/src/main/java/com/mycompany/library_project/config/");

    public ConfigDatabase() {
    }

    public boolean chackFileConfig() {
        File jsonFile = new File(path + fileConfig);
        return jsonFile.exists();
    }

    public boolean createFileConfig(String host, String port, String userName, String password) {
        try {
            FileWriter fileWriter = new FileWriter(path + fileConfig);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            JsonObject server = new JsonObject();
            server.put("host", host);
            server.put("port", port);
            server.put("userName", userName);
            server.put("password", password);
            Jsoner.serialize(server, writer);
            
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getServerInfor() {
        try {
            String server[] = new String[4];
            FileReader fileReader = new FileReader(path + fileConfig);
            BufferedReader reader = new BufferedReader(fileReader);
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            server[0] = (String) parser.get("host");
            server[1] = (String) parser.get("port");
            server[2] = (String) parser.get("userName");
            server[3] = (String) parser.get("password");

            reader.close();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
