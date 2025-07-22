package com.coderfromscratch.httpserver.core.io;

public class WebRootNotFoundException extends Exception{        //chose to extend Exception instead of Throwable
    public WebRootNotFoundException(String message){
        super(message);
    }
}
