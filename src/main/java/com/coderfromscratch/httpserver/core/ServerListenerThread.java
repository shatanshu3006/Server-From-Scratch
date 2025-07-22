package com.coderfromscratch.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//could've implemented Runnable but Thread works fine as well
public class ServerListenerThread extends Thread{

    private final static Logger LOGGER= LoggerFactory.getLogger(ServerListenerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    //constructor for this class that takes in the port and the webroot.
    //we could have the ConfigurationManager inside the ServerListenerThread directly but we want to update it later so we keep the separation here
    //so we pass the values through the constructor     (port and webroot for now)

    public ServerListenerThread(int port,String webroot) throws IOException {
        this.port=port;
        this.webroot=webroot;

        //moving the serverSocket into thread
        // this.serverSocket = new ServerSocket(this.port); // original socket binding

        // Updated socket creation with reuse address enabled
        this.serverSocket = new ServerSocket();     //Creates a ServerSocket without binding it to any port.
        this.serverSocket.setReuseAddress(true);        //“Even if this port was recently closed and might still be in a TIME_WAIT state, allow me to bind to it again.”
        this.serverSocket.bind(new java.net.InetSocketAddress(this.port));  //Now that setReuseAddress(true) is in place, we bind the socket to the desired port.
    }

    @Override
    public void run() {

        //try-catch block from the HttpServer.java file
        //this here will do whatever we were doing directly from the main method

        try {
            while(serverSocket.isBound() && !serverSocket.isClosed()) {

                // Use the already-initialized serverSocket field
                Socket socket = serverSocket.accept();        // it stops and waits for connection

                LOGGER.info("The connection has been accepted" + socket.getInetAddress());

                HttpConnectionWorkerThread workerThread=new HttpConnectionWorkerThread(socket);
                workerThread.start();

            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket",e);
            e.printStackTrace();
        }
        finally{
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e){
                    //just a catch and not doing anything
                }
            }
        }
    }
}
