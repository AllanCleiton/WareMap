package com.allancleiton.waremap.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigManager {
    
    private Properties properties = new Properties();
    private String filePath;

 
    public ConfigManager(String filePath) throws IOException {
        this.filePath = filePath; 
        loadProperties();  // Carrega as propriedades do arquivo no momento da criação.
    }

    
    private void loadProperties() throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input); 
        }
    }

   
    public String getProperty(String key) {
        
        return properties.getProperty(key);
    }

    
    public void setProperty(String key, String value) throws IOException {
        properties.setProperty(key, value);  
        saveProperties();     
    }
   
    public Set<Map.Entry<Object, Object>> entrySet(){
    	return properties.entrySet();
    }
    
    private void saveProperties() throws IOException {
        
        try (OutputStream output = new FileOutputStream(filePath)) {
            // Armazena o objeto Properties no arquivo com uma possível mensagem de comentários (null nesse caso).
            properties.store(output, null);
        }
    }
}
