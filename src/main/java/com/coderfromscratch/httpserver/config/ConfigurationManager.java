package com.coderfromscratch.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.util.Json.parse;

public class ConfigurationManager {
    //reason for using single design pattern is that we will only need a single ConfigurationManager for the
    //whole project
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager(){

    }

    // Returns the singleton instance of ConfigurationManager
    public static ConfigurationManager getInstance(){
        if(myConfigurationManager==null){
            myConfigurationManager=new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    // Loads the configuration file from the given path, parses it, and stores it in memory
    public void loadConfigurationFile(String filePath)  {
        // Tries to open the file using FileReader
        FileReader fileReader=null;
        try {
            fileReader = new FileReader(filePath);
        }
        catch (IOException e){
            throw new HttpConfigurationException(e);
        }
        StringBuffer sb=new StringBuffer();

        // Reads characters from the file and appends them to the StringBuffer
        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch (Exception e) {
            throw new HttpConfigurationException(e);
        }

        // Parses the JSON string into a JsonNode
        JsonNode config= null;
        try {
            config = parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Error Parsing the Configuration File",e);
        }

        // Converts the JsonNode into a Configuration object
        try {
            myCurrentConfiguration= Json.fromJson(config,Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the Configuration file,internal",e);
        }
    }

    // Returns the currently loaded Configuration object, or throws an exception if none is set
    public Configuration getCurrentConfiguration(){
        if(myCurrentConfiguration==null){
            throw new HttpConfigurationException("No Current Configuration Set!");
        }
        return myCurrentConfiguration;
    }
}
