package com.gruposai.tenantmanager.Util;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
@Configuration
public class LiriumConfig {

    private String tokenUrl;
    private String tenantUrl; // creación
    private String tenantsBaseUrl; // lectura base https://.../api/tenants/
    private String allTenantsBaseUrl;
    private String username;
    private String password;
    private String usernameDB;
    private String passwordDB;
    private String database;

    public String getUsernameDB() {
        return usernameDB;
    }

    public void setUsernameDB(String usernameDB) {
        this.usernameDB = usernameDB;
    }

    public String getPasswordDB() {
        return passwordDB;
    }

    public void setPasswordDB(String passwordDB) {
        this.passwordDB = passwordDB;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getTenantUrl() {
        return tenantUrl;
    }

    public void setTenantUrl(String tenantUrl) {
        this.tenantUrl = tenantUrl;
    }

    public String getTenantsBaseUrl() {
        return tenantsBaseUrl;
    }

    public void setTenantsBaseUrl(String tenantsBaseUrl) {
        this.tenantsBaseUrl = tenantsBaseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAllTenantsBaseUrl() {
        return allTenantsBaseUrl;
    }

    public void setAllTenantsBaseUrl(String allTenantsBaseUrl) {
        this.allTenantsBaseUrl = allTenantsBaseUrl;
    }

    public LiriumConfig() {
        Properties props = new Properties();

        // Ruta absoluta o relativa desde donde se ejecuta el jar
        String configPath = "config.cfg"; // Usa solo el nombre si está junto al .jar

        try (FileInputStream input = new FileInputStream(configPath)) {
            props.load(input);
            this.tokenUrl = props.getProperty("url.token");
            this.tenantUrl = props.getProperty("url.create-tenant");
            this.tenantsBaseUrl = props.getProperty("url.tenants-base");
            this.allTenantsBaseUrl = props.getProperty("url.tenants.base.tenants");
            this.username = props.getProperty("username");
            this.password = props.getProperty("password");
            this.usernameDB = props.getProperty("usernameDB");
            this.passwordDB = props.getProperty("passwordDB");
            this.database = props.getProperty("database");
            // Fallback si no se configuró explícitamente la base: derivar de create-tenant si posible
            if (this.tenantsBaseUrl == null || this.tenantsBaseUrl.trim().isEmpty()) {
                // intentar truncar hasta "/api/tenants/"
                if (this.tenantUrl != null && this.tenantUrl.contains("/api/tenants/")) {
                    this.tenantsBaseUrl = this.tenantUrl.substring(0, this.tenantUrl.indexOf("/api/tenants/") + "/api/tenants/".length());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo config.cfg en " + configPath, e);
        }
    }
}
