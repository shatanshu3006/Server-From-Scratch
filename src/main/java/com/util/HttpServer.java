package com.util;

//Driver Class for the HttpServer
    // we need -> read configuration file
    // need -> open a socket to listen at a port
    // for server to make multiple connections -> need to make it multithreaded
    // need to handle messages -> read request message from client
    // need to go to the file system requested by client -> open & read from file system
    // need -> reply to client (write response messages) (http compliant)

// restrictions to the server (while building sockets to be updated later on )
    // one connection at a time only
    // really dumb, not understanding the HTTP protocol, disregard everything sent by the browser
    // will always serve the same hardcoded page (atleast for the beginning)

//on types of parsers : type 1 -> Lexer (tokenizer) Parser
//                      type 2 -> Lexerless (scannerless) parser    -> ⚠️we use this (to drop any malicious request even before it completely arrives)


import com.coderfromscratch.httpserver.config.Configuration;
import com.coderfromscratch.httpserver.config.ConfigurationManager;
import com.coderfromscratch.httpserver.core.ServerListenerThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private final static Logger LOGGER= LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        //System.out.println("Server Starting ...");
        LOGGER.info("Server Starting ...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/java/resource/http.json");
        Configuration conf=ConfigurationManager.getInstance().getCurrentConfiguration();

        //these are all the configuration values declared in Configuration.java file
//        System.out.println("Using Port: " + conf.getPort());
//        System.out.println("Using Webroot: " + conf.getWebroot());
//        System.out.println("Default Page: " + conf.getDefaultPage());
//        System.out.println("Log Level: " + conf.getLogLevel());
//        System.out.println("Threading Model: " + conf.getThreadingModel());
//        System.out.println("Thread Pool Size: " + conf.getThreadPoolSize());
//        System.out.println("MIME Types: " + conf.getMimeTypes());

          LOGGER.info("Using Port: " + conf.getPort());
          LOGGER.info("Using Webroot: " + conf.getWebroot());

        // implementing server sockets              (this try-catch block is used later in the ServerListenerThread)
//        try {
//            ServerSocket serverSocket= new ServerSocket(conf.getPort());
//            Socket socket=serverSocket.accept();        // it stops and waits for connection
//
//            //while accepting the bytes from the browser, it is written on inputStream
//            InputStream inputStream=socket.getInputStream();
//
//            //while sending out the data in bytes it is written on outputStream
//            OutputStream outputStream=socket.getOutputStream();
//
//            // reading
//            // since we are disregarding everything from the browser we are not implementing read
//
//
//            //writing
//            String html="<html><head><title>Simple JAVA HTTP Server</title></head><body><h1>This page was served using my Simple Java HTTP Server </h1></body></html>";
//
//            final String CRLF="\n\r";   // 13, 10           CRLF -> Control Returns, Line Free
//            String response=
//                    "HTTP/1.1 200 OK" + CRLF +                              // Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
//                    "Content-Length: " + html.getBytes().length + CRLF +    // HEADER
//                            CRLF +
//                            html +
//                            CRLF + CRLF ;
//
//            outputStream.write(response.getBytes());
//
//            inputStream.close();
//            outputStream.close();
//            socket.close();
//            serverSocket.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle later
        }
    }
}
