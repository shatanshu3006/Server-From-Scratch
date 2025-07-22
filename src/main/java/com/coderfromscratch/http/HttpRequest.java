package com.coderfromscratch.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class HttpRequest extends HttpMessage {


    private HttpMethod method;
//    	•	Purpose: Stores the HTTP method (also called the verb) used in the request.
//      •	Examples: GET, POST, PUT, DELETE


    private String requestTarget;
//    	•	Purpose: Represents the target of the HTTP request, usually the path and query string from the URL.
//	    •	Example: /index.html, /search?q=java


    private String originalHttpVersion;   //this is the literal we get from the request
//    	•	Purpose: Stores the HTTP protocol version used by the client.
//	    •	Examples: HTTP/1.1, HTTP/2


    private HttpVersion bestCompatibleVersion; //this is the best compatible version for this request

    private HashMap<String,String>headers=new HashMap<>();


    // reference to rfc 7231 section 4.1
    // method = token  (method token is case-sensitive)
    // all general purpose servers MUST support the methods GET and HEAD
    // all other methods are OPTIONAL
    HttpRequest(){

    }

    //getter
    public HttpMethod getMethod() {
        return method;
    }

    //getter
    public String getRequestTarget() {
        return requestTarget;
    }

    //getter
    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }

    //getter
    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    public Set<String> getHeaderNames(){
        return headers.keySet();
    }

    public String getHeader(String headerName){
        return headers.get(headerName.toLowerCase());
    }

    //setter
    void setMethod(String methodName) throws HttpParsingException {
        for(HttpMethod method: HttpMethod.values()){
            if(methodName.equals(method.name())){
                this.method=method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }


    //setter
    void setRequestTarget(String requestTarget) throws HttpParsingException {
        //making sure that req target we re giving is not null else will give the parsing exception
        if(requestTarget == null || requestTarget.length()==0){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }


    public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion=originalHttpVersion;
        this.bestCompatibleVersion=HttpVersion.getBestCompatibleVersion(originalHttpVersion);

        if(this.bestCompatibleVersion==null){
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }

    void addHeader(String headerName, String headerField){
        headers.put(headerName.toLowerCase(),headerField);
    }
}
