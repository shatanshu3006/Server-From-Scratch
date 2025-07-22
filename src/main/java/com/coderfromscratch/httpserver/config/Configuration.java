package com.coderfromscratch.httpserver.config;

import java.util.Map;

//this is going to be the file we are going to map that json to
public class Configuration {
    private int port;
    private String webroot;
    private String defaultPage;
    private String logLevel;
    private String threadingModel;
    private int threadPoolSize;
    private Map<String,String>mimeTypes;
    private Map<String,String>routes;
    private Map<String,String>errorPages;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getThreadingModel() {
        return threadingModel;
    }

    public void setThreadingModel(String threadingModel) {
        this.threadingModel = threadingModel;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public Map<String, String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(Map<String, String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public Map<String, String> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, String> routes) {
        this.routes = routes;
    }

    public Map<String, String> getErrorPages() {
        return errorPages;
    }

    public void setErrorPages(Map<String, String> errorPages) {
        this.errorPages = errorPages;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    public int getPort() {
        return port;
    }

    public String getWebroot() {
        return webroot;
    }

    public String getMimeTypeForFile(String path) {
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < path.length() - 1) {
            String extension = path.substring(dotIndex + 1);
            if (mimeTypes != null) {
                return mimeTypes.getOrDefault(extension, "application/octet-stream");
            }
        }
        return "application/octet-stream";
    }
}
