package com.coderfromscratch.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private final static Logger LOGGER= LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private Socket socket;
    public HttpConnectionWorkerThread(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            //while accepting the bytes from the browser, it is written on inputStream
            inputStream = socket.getInputStream();

            //while sending out the data in bytes it is written on outputStream
            outputStream = socket.getOutputStream();

            // reading
            // since we are disregarding everything from the browser we are not implementing read


            // For making Requests.txt (has issues, as it only gets triggered by terminal (curl command)
//            int _byte;
//            System.out.println("Waiting for request...");
//            while (( _byte = inputStream.read() ) != -1) {
//                System.out.print((char) _byte);
//            }


            //writing
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            if (line == null || line.isEmpty()) return;

            String[] requestLine = line.split(" ");
            String method = requestLine[0];
            String requestedPath = requestLine[1];

            if (requestedPath.equals("/")) {
                requestedPath = "/index.html";
            }

            File file = new File("WebRoot", requestedPath);
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());

                String contentType;
                if (requestedPath.endsWith(".html") || requestedPath.endsWith(".htm")) {
                    contentType = "text/html";
                } else if (requestedPath.endsWith(".css")) {
                    contentType = "text/css";
                } else if (requestedPath.endsWith(".js")) {
                    contentType = "application/javascript";
                } else if (requestedPath.endsWith(".png")) {
                    contentType = "image/png";
                } else if (requestedPath.endsWith(".jpg") || requestedPath.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (requestedPath.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (requestedPath.endsWith(".json")) {
                    contentType = "application/json";
                } else {
                    contentType = "application/octet-stream";
                }

                final String CRLF = "\r\n";
                String response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "Content-Type: " + contentType + CRLF +
                        "Content-Length: " + fileBytes.length + CRLF +
                        CRLF;

                outputStream.write(response.getBytes());
                outputStream.write(fileBytes);
            } else {
                final String CRLF = "\r\n";
                String html = "<html><body><h1>404 Not Found</h1></body></html>";
                String response =
                        "HTTP/1.1 404 Not Found" + CRLF +
                        "Content-Type: text/html" + CRLF +
                        "Content-Length: " + html.getBytes().length + CRLF +
                        CRLF +
                        html + CRLF;

                outputStream.write(response.getBytes());
            }


            //serverSocket.close(); //we dont want to close it,we want it to keep getting connections
            //upon refreshing the page, it won't break the connection now

//            try {
//                sleep(5000);                          //introduced sleep to check if they are loading at the same time or not
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            LOGGER.info("Connection Processed ...");

        } catch (IOException e) {
            LOGGER.error("Problem with Communication",e);
            e.printStackTrace();
        }
        finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {}

            }
        }
    }
}
